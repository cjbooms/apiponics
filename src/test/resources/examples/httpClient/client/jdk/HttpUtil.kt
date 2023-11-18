package examples.httpClient.jdk.client

import com.fasterxml.jackson.core.exc.StreamReadException
import com.fasterxml.jackson.databind.DatabindException
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.IOException
import java.io.InputStream
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse.*
import java.util.function.Supplier

@Throws(ApiException::class)
inline fun <reified T> HttpClient.execute(httpRequest: HttpRequest): ApiResponse<T> {
    val result = this.send(httpRequest, BodyHandlers.ofByteArray())

    return when (result.statusCode()){
        in 200..299 -> ApiResponse(result.statusCode(), result.headers(), result.body()?.let { readJson(T::class.java, it) })
        in 300..399 -> throw ApiRedirectException(result.statusCode(), result.headers(), result.body()?.let { String(it) } ?: "")
        in 400..499 -> throw ApiClientException(result.statusCode(), result.headers(), result.body()?.let { String(it) } ?: "")
        in 500..599 -> throw ApiServerException(result.statusCode(), result.headers(), result.body()?.let { String(it) } ?: "")
        else -> throw ApiException("[${result.statusCode()}]: "/*${response.errorMessage()}*/)
    }
}

fun <W> readJson(targetType: Class<W>?, data: ByteArray): W? {
    return try {
        val objectMapper =
            ObjectMapper()
        objectMapper.readValue(data, targetType)
    } catch (_: IOException) {
        null
    } catch (_: StreamReadException) {
        null
    } catch (_: DatabindException) {
        null
    }
}

fun <W> asJSON(targetType: Class<W>?): BodySubscriber<Supplier<W?>> {
    val upstream = BodySubscribers.ofInputStream()
    return BodySubscribers.mapping(
        upstream
    ) { inputStream: InputStream ->
        toSupplierOfType(
            inputStream,
            targetType
        )
    }
}

fun <W> toSupplierOfType(inputStream: InputStream, targetType: Class<W>?): Supplier<W?> {
    return Supplier {
        try {
            inputStream.use { stream ->
                val objectMapper =
                    ObjectMapper()
                return@Supplier objectMapper.readValue<W>(stream, targetType)
            }
        } catch (e: IOException) {
            return@Supplier null
        }
    }
}

class JsonBodyHandler<W>(val wClass: Class<W>): BodyHandler<Supplier<W?>> {
    override fun apply(responseInfo: ResponseInfo): BodySubscriber<Supplier<W?>> {
        return asJSON(wClass)
    }
}

object Publishers {
    fun <W> jsonBodyPublisher(obj: W): HttpRequest.BodyPublisher {
        val data = ObjectMapper().writeValueAsBytes(obj)
        return BodyPublishers.ofByteArray(data)
    }
}

class Url(val url: String) {
    private val pathParams: MutableList<Pair<String, String>> = mutableListOf()
    private val queryParams: MutableList<Pair<String, String>> = mutableListOf()

    fun addPathParam(param: String, value: Any?): Url {
        if (value != null) {
            this.pathParams.add(param to value.toString())
        }

        return this
    }

    fun addQueryParam(param: String, value: List<String>?, explode: Boolean = true): Url {
        if (value != null) {
            if (explode) {
                value.forEach {
                    this.queryParams.add(param to it)
                }
            } else {
                this.queryParams.add(
                    param to value.joinToString(",")
                )
            }
        }

        return this
    }

    fun <T> addQueryParam(param: String, value: T?): Url {
        if (value != null) this.queryParams.add(param to value.toString())

        return this
    }

    fun toUri(): URI {
        val templatedUrl = pathParams.fold(url) { acc, value ->
            acc.replace("{${value.first}}", value.second)
        }

        val queryParams = this.queryParams.joinToString("&") {
            "${it.first}=${it.second}"
        }.let {
            if (it.isNotEmpty()) {
                "?$it"
            } else {
                it
            }
        }

        return URI.create("$templatedUrl$queryParams")
    }
}
