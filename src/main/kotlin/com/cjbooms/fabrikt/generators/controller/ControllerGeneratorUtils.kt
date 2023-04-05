package com.cjbooms.fabrikt.generators.controller

import com.cjbooms.fabrikt.generators.model.JacksonModelGenerator.Companion.toModelType
import com.cjbooms.fabrikt.model.ControllerType
import com.cjbooms.fabrikt.model.KotlinTypeInfo
import com.cjbooms.fabrikt.util.NormalisedString.camelCase
import com.reprezen.kaizen.oasparser.model3.Operation
import com.reprezen.kaizen.oasparser.model3.Response
import com.reprezen.kaizen.oasparser.model3.SecurityRequirement
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName

object ControllerGeneratorUtils {
    /**
     * This maps the OpenAPI response code with a ClassName object.
     * Pulling out the first response. This assumes first is happy path
     * may need to revisit if we want to have conditional responses
     */
    fun Operation.happyPathResponse(basePackage: String): TypeName {
        // Map of response code to nullable name of schema
        val responseDetails = happyPathResponseObject()
        return responseDetails
            .contentMediaTypes
            .map { it.value?.schema }
            .filterNotNull()
            .firstOrNull()
            ?.let { toModelType(basePackage, KotlinTypeInfo.from(it)) }
            ?: Unit::class.asTypeName()
    }

    private fun Operation.happyPathResponseObject(): Response {
        val toResponseMapping: Map<Int, Response> = responses
            .filter { it.key != "default" }
            .map { (code, body) ->
                code.toInt() to body
            }.toMap()

        // Happy path, just pull out the http code with the lowest value. Later we may have conditional responses
        val code: Int = toResponseMapping.keys.minOrNull()
            ?: throw IllegalStateException("Could not extract the response for $this")

        return toResponseMapping[code]!!
    }

    fun controllerName(resourceName: String) = "$resourceName${ControllerType.SUFFIX}"

    fun methodName(op: Operation, verb: String, isSingleResource: Boolean) =
        op.operationId?.camelCase() ?: httpVerbMethodName(verb, isSingleResource)

    private fun httpVerbMethodName(verb: String, isSingleResource: Boolean) =
        if (isSingleResource) "${verb}ById" else verb


    /**
     * Enum definition for different cases of authentication
     *
     * N0_SECURITY when no @Secured Annotation is to be added neither an authentication input parameter
     * AUTHENTICATION_REQUIRED when @Secured Annotation IS_AUTHENTICATED (for micronaut) is to be added and authentication as input parameter
     * AUTHENTICATION_PROHIBITED when @Secured Annotation IS_ANONYMOUS (for micronaut) is to be added and no authentication input parameter
     * AUTHENTICATION_OPTIONAL when @Secured Annotation IS_AUTHENTICATED and IS_ANONYMOUS (for micronaut) is to be added and authentication as optional input parameter
     */
    enum class SecuritySupport(val allowsAuthenticated: Boolean, val allowsAnonymous: Boolean) {
        NO_SECURITY(false, false),
        AUTHENTICATION_REQUIRED(true, false),
        AUTHENTICATION_PROHIBITED(false, true),
        AUTHENTICATION_OPTIONAL(true, true),
    }


    /**
     * Differentiating the cases of authentication
     *
     * depending on the security settings on an operation the SecuritySupport Enum is defined
     */
    fun List<SecurityRequirement>.securitySupport(): SecuritySupport {

        val containsEmptyObject = this.any{ it.requirements.isEmpty()}
        val containsNonEmptyObject =  this.any{ it.requirements.isNotEmpty()}

        return when {
            (containsEmptyObject && containsNonEmptyObject) -> SecuritySupport.AUTHENTICATION_OPTIONAL
            (containsEmptyObject) -> SecuritySupport.AUTHENTICATION_PROHIBITED
            (containsNonEmptyObject) -> SecuritySupport.AUTHENTICATION_REQUIRED
            else -> SecuritySupport.NO_SECURITY
        }

    }
}
