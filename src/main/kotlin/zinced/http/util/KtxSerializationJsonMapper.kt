package zinced.http.util

import io.javalin.plugin.json.JsonMapper
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.serializer
import java.io.InputStream

class KtxSerializationJsonMapper(private val json: Json = Json {
    ignoreUnknownKeys = true
}) : JsonMapper {

    @OptIn(ExperimentalSerializationApi::class)
    override fun toJsonString(obj: Any) = json.encodeToString(json.serializersModule.serializer(obj.javaClass), obj)

    override fun toJsonStream(obj: Any) = toJsonString(obj).byteInputStream()

    @Suppress("UNCHECKED_CAST")
    @OptIn(ExperimentalSerializationApi::class)
    override fun <T : Any> fromJsonString(value: String, targetClass: Class<T>): T =
        json.decodeFromString(json.serializersModule.serializer(targetClass), value) as T

    @Suppress("UNCHECKED_CAST")
    @OptIn(ExperimentalSerializationApi::class)
    override fun <T : Any> fromJsonStream(value: InputStream, targetClass: Class<T>): T =
        json.decodeFromStream(json.serializersModule.serializer(targetClass), value) as T

}
