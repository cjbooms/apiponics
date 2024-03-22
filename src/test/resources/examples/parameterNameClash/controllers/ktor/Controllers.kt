package examples.parameterNameClash.controllers

import examples.parameterNameClash.controllers.RoutingUtils.getOrFail
import examples.parameterNameClash.models.SomeObject
import io.ktor.http.Headers
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.ParameterConversionException
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.`get`
import io.ktor.server.routing.post
import io.ktor.server.util.getOrFail
import io.ktor.util.converters.DefaultConversionService
import io.ktor.util.reflect.typeInfo
import kotlin.Any
import kotlin.String
import kotlin.Unit

public interface ExampleController {
    /**
     *
     *
     * @param pathB
     * @param queryB
     */
    public suspend fun getById(
        call: ApplicationCall,
        pathB: String,
        queryB: String,
    ): ControllerResult<Unit>

    /**
     *
     *
     * @param bodySomeObject example
     * @param querySomeObject
     */
    public suspend fun post(
        call: ApplicationCall,
        querySomeObject: String,
        bodySomeObject: SomeObject,
    ): ControllerResult<Unit>

    public companion object {
        public fun Route.exampleRoutes(controller: ExampleController) {
            get("/example/{b}") {
                val pathB = call.parameters.getOrFail<kotlin.String>("b")
                val queryB = call.request.queryParameters.getOrFail<kotlin.String>("b")
                val result = controller.getById(call, pathB, queryB)
                call.respond(result.status)
            }
            post("/example") {
                val querySomeObject = call.request.queryParameters.getOrFail<kotlin.String>("someObject")
                val bodySomeObject = call.receive<SomeObject>()
                val result = controller.post(call, querySomeObject, bodySomeObject)
                call.respond(result.status)
            }
        }
    }
}

public object RoutingUtils {
    /**
     * Gets parameter value associated with this name or null if the name is not present.
     * Converting to type R using DefaultConversionService.
     *
     * Throws:
     *   ParameterConversionException - when conversion from String to R fails
     */
    public inline fun <reified R : Any> Parameters.getTyped(name: String): R? {
        val values = getAll(name) ?: return null
        val typeInfo = typeInfo<R>()
        return try {
            @Suppress("UNCHECKED_CAST")
            DefaultConversionService.fromValues(values, typeInfo) as R
        } catch (cause: Exception) {
            throw ParameterConversionException(
                name,
                typeInfo.type.simpleName
                    ?: typeInfo.type.toString(),
                cause,
            )
        }
    }

    /**
     * Gets first value from the list of values associated with a name.
     *
     * Throws:
     *   BadRequestException - when the name is not present
     */
    public fun Headers.getOrFail(name: String): String = this[name] ?: throw
        BadRequestException("Header " + name + " is required")
}

public data class ControllerResult<T>(
    public val status: HttpStatusCode,
    public val message: T,
)
