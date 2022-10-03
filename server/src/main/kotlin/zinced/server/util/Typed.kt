package zinced.server.util

import kotlin.reflect.KType
import kotlin.reflect.typeOf

data class Typed<T : Any>(val type: KType, val value: T)

inline fun <reified T : Any> T.withType() = Typed(typeOf<T>(), this)
