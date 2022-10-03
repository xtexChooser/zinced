/*
 * Copyright 2022 xtexChooser
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package zinced.server.web.util

import io.javalin.plugin.json.JsonMapper
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.serializer
import zinced.server.util.Typed
import java.io.InputStream

private val json: Json = Json {
    ignoreUnknownKeys = true
}

object KtxSerializationJsonMapper : JsonMapper {

    @OptIn(ExperimentalSerializationApi::class)
    override fun toJsonString(obj: Any) =
        if (obj is Typed<*>) {
            json.encodeToString(json.serializersModule.serializer(obj.type), obj.value)
        } else {
            json.encodeToString(json.serializersModule.serializer(obj::class.java), obj)
        }

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
