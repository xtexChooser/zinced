package zinced.server.database.util.json

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.hibernate.type.FormatMapper
import org.hibernate.type.descriptor.WrapperOptions
import org.hibernate.type.descriptor.java.JavaType

@OptIn(InternalSerializationApi::class)
class JsonMapper : FormatMapper {

    override fun <T : Any> toString(value: T, javaType: JavaType<T>, wrapperOptions: WrapperOptions?) =
        Json.encodeToString(javaType.javaTypeClass!!.kotlin.serializer(), value)

    override fun <T : Any> fromString(
        charSequence: CharSequence,
        javaType: JavaType<T>,
        wrapperOptions: WrapperOptions?
    ): T = Json.decodeFromString(javaType.javaTypeClass!!.kotlin.serializer(), charSequence.toString())

}