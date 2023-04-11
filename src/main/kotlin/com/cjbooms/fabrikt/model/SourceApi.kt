package com.cjbooms.fabrikt.model

import com.beust.jcommander.ParameterException
import com.cjbooms.fabrikt.util.KaizenParserExtensions.isNotDefined
import com.cjbooms.fabrikt.util.YamlUtils
import com.cjbooms.fabrikt.validation.ValidationError
import com.reprezen.kaizen.oasparser.model3.OpenApi3
import com.reprezen.kaizen.oasparser.model3.Schema
import java.nio.file.Path
import java.nio.file.Paths
import java.util.logging.Logger

data class SchemaInfo(val name: String, val schema: Schema) {
    val typeInfo: KotlinTypeInfo = KotlinTypeInfo.from(schema, name)
}

data class SourceApi(
    private val rawApiSpec: String,
    val baseDir: Path = Paths.get("").toAbsolutePath()
) {
    companion object {
        private val logger = Logger.getGlobal()

        fun create(
            baseApi: String,
            apiFragments: Collection<String>,
            baseDir: Path = Paths.get("").toAbsolutePath()
        ): SourceApi {
            val combinedApi =
                apiFragments.fold(baseApi) { acc: String, fragment -> YamlUtils.mergeYamlTrees(acc, fragment) }
            return SourceApi(combinedApi, baseDir)
        }
    }

    val openApi3: OpenApi3 = YamlUtils.parseOpenApi(rawApiSpec, baseDir)
    val allSchemas: List<SchemaInfo>

    init {
        validateSchemaObjects(openApi3).let {
            if (it.isNotEmpty()) throw ParameterException("Invalid models or api file:\n${it.joinToString("\n\t")}")
        }
        allSchemas = openApi3.schemas.entries.map { it.key to it.value }
            .plus(openApi3.parameters.entries.map { it.key to it.value.schema })
            .plus(openApi3.responses.entries.flatMap { it.value.contentMediaTypes.entries.map { content -> it.key to content.value.schema } })
            .map { (key, schema) -> SchemaInfo(key, schema) }
    }

    private fun validateSchemaObjects(api: OpenApi3): List<ValidationError> {
        val schemaErrors = api.schemas.entries.fold(emptyList<ValidationError>()) { errors, entry ->
            val name = entry.key
            val schema = entry.value

            val newError =
                if (schema.type == OasType.Object.type) {
                val hasOneOf = schema.oneOfSchemas?.isNotEmpty() == true
                val hasAnyOf = schema.anyOfSchemas?.isNotEmpty() == true
                val hasAllOf = schema.allOfSchemas?.isNotEmpty() == true
                val hasMapping = schema.discriminator.hasMappings()
                if (hasMapping && !hasOneOf && !hasAllOf) {
                    // Polymorphism currently is only supported via the 'mapping' property.
                    // All 'anyOf' schemas have to be removed here to avoid stack overflows later.
                    schema.anyOfSchemas.clear()
                    null
                } else if (hasOneOf || hasAnyOf || hasAllOf) {
                    ValidationError(
                        "'$name' schema contains an invalid combination of properties and `oneOf | anyOf | allOf`. " +
                                "Do not use properties and a combiner at the same level."
                    )
                } else {
                    null
                }
            } else if (schema.type == null && schema.properties?.isNotEmpty() == true) {
                logger.warning("Schema '$name' has 'type: null' but defines properties. Assuming: 'type: object'")
                null
            } else {
                null
            }

            errors + listOfNotNull(newError)
        }

        return api.schemas.map { it.value.properties }.flatMap { it.entries }
            .fold(schemaErrors) { lst, entry ->
                val name = entry.key
                val schema = entry.value
                if (schema.type == OasType.Array.type && schema.itemsSchema.isNotDefined()) {
                    lst + listOf(ValidationError("Array type '$name' cannot be parsed to a Schema. Check your input"))
                } else if (schema.isNotDefined()) {
                    lst + listOf(ValidationError("Property '$name' cannot be parsed to a Schema. Check your input"))
                } else {
                    lst
                }
            }
    }
}
