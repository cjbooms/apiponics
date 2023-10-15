package com.cjbooms.fabrikt.util

import com.cjbooms.fabrikt.model.EnclosingSchemaInfo
import com.cjbooms.fabrikt.model.EnclosingSchemaInfoName
import com.cjbooms.fabrikt.model.EnclosingSchemaInfoOasModel
import com.cjbooms.fabrikt.model.SchemaInfo
import com.cjbooms.fabrikt.util.KaizenParserExtensions.safeName
import com.cjbooms.fabrikt.util.NormalisedString.toModelClassName
import com.reprezen.jsonoverlay.Overlay
import com.reprezen.kaizen.oasparser.model3.Schema
import java.net.URL

/**
 * Model name registry to avoid name collisions
 */
object ModelNameRegistry {
    private val allocatedNames: MutableSet<String> = mutableSetOf()
    private val tagToName: MutableMap<String, String> = mutableMapOf()
    private const val SUFFIX = "Extra"

    /**
     * Registers a new model class name using `schema` and if it is inlined type also based on enclosed schema.
     * The returned value can be queried multiple times by passing `tag` to
     * [ModelNameRegistry.get].
     */
    private fun register(
        schema: Schema,
        enclosingSchema: EnclosingSchemaInfo? = null,
        valueClassName: Boolean = false,
        schemaInfoName: String? = null,
    ): String {
        var suggestion = schema.toModelClassName(schemaInfoName, enclosingSchema?.toModelClassName(), valueClassName)
        while (allocatedNames.contains(suggestion)) {
            suggestion += SUFFIX
        }
        allocatedNames.add(suggestion)

        val tag = resolveTag(schema, enclosingSchema, valueClassName, schemaInfoName)
        val replaced = tagToName.put(tag, suggestion)
        if (replaced != null) {
            // Only allow unique tags to be registered
            throw IllegalArgumentException("tag $tag cannot be used for both '$replaced' and '$suggestion'")
        }

        return suggestion
    }

    private fun Schema.toModelClassName(
        schemaInfoName: String? = null,
        enclosingClassName: String? = null,
        valueClassName: Boolean = false,
    ): String = buildString {
        if (enclosingClassName != null) {
            append(enclosingClassName)
        }
        val modelClassName = schemaInfoName?.toModelClassName() ?: safeName().toModelClassName()
        append(modelClassName)
        if (valueClassName) {
            append("Value")
        }
    }

    private fun EnclosingSchemaInfo.toModelClassName() =
        when (this) {
            is EnclosingSchemaInfoName -> this.name
            is EnclosingSchemaInfoOasModel -> this.schema.toModelClassName()
            else -> ""
        }

    private fun resolveTag(
        schema: Schema,
        enclosingSchema: EnclosingSchemaInfo? = null,
        valueClassName: Boolean = false,
        schemaInfoName: String? = null,
    ): String {
        val overlay = Overlay.of(schema)
        val uri = URL(overlay.jsonReference)
        val typeName = schema.toModelClassName(schemaInfoName, enclosingSchema?.toModelClassName(), valueClassName)

        return "file:${uri.file}#${enclosingSchema?.toModelClassName() ?: ""}$typeName"
    }

    /** Retrieve a model class name created with [ModelNameRegistry.register]. */
    operator fun get(tag: String): Result<String> = runCatching {
        requireNotNull(tagToName[tag]) { "unknown tag: $tag" }
    }

    fun getOrRegister(
        schema: Schema,
        enclosingSchema: EnclosingSchemaInfo? = null,
        valueClassName: Boolean = false,
    ) = this[resolveTag(schema, enclosingSchema, valueClassName)]
        .getOrElse { register(schema, enclosingSchema, valueClassName) }

    fun getOrRegister(
        schemaInfo: SchemaInfo,
    ) =
        this[resolveTag(schemaInfo.schema, schemaInfoName = schemaInfo.name)]
            .getOrElse { register(schemaInfo.schema, schemaInfoName = schemaInfo.name) }

    fun clear() {
        allocatedNames.clear()
        tagToName.clear()
    }
}
