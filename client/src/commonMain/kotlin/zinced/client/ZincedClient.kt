package zinced.client

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

class ZincedClient(val baseUrl: String) {

    suspend fun callJsonApi(url: String, method: String, body: String? = null) =
        callHttpJsonApi(baseUrl + url, method, body)

    @OptIn(InternalSerializationApi::class)
    suspend inline fun <reified R : Any> callJsonApi(url: String, method: String) =
        Json.decodeFromString<R>(callJsonApi(url, method, null))

    @OptIn(InternalSerializationApi::class)
    suspend inline fun <reified Q : Any, reified R : Any> callJsonApi(url: String, method: String, body: Q): R {
        val payload = Json.encodeToString(Q::class.serializer(), body)
        return Json.decodeFromString(callJsonApi(url, method, payload))
    }

}