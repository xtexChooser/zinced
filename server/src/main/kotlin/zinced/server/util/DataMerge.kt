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
package zinced.server.util

import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.isSuperclassOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

private val dataClassP2pMapping = HashMap<KClass<*>, Map<KParameter, KProperty1<*, *>>>()

inline fun <reified T : Any> T?.merge(other: T?) = merge(other, T::class)

@Suppress("UNCHECKED_CAST")
fun <T : Any> T?.merge(other: T?, type: KClass<out T>): T? {
    if (this == null) {
        return other
    } else if (other == null) {
        return this
    }
    if (type.isSubclassOf(CharSequence::class)) {
        return other
    } else if (type.isData) {
        val cons = type.primaryConstructor ?: error("Data class $type has no primary constructor")
        return cons.callBy(dataClassP2pMapping.getOrElse(type) {
            val properties = type.memberProperties.associateBy { it.name }
            cons.parameters.associateWith {
                properties[it.name] ?: error("Could not match property ${it.name} in $type ")
            }
        }.mapValues { (_, prop) ->
            try {
                (prop as KProperty1<T, *>).get(this).merge(prop.get(other), prop.returnType.jvmErasure)
            } catch (e: Exception) {
                throw IllegalStateException("Error merging ${prop.name} in $type", e)
            }
        })
    } else if (type.isSubclassOf(Collection::class)) {
        val new = (this as Collection<*>).toMutableList()
        new.addAll(other as Collection<*>)
        val cons =
            type.constructors.find { it.parameters.size == 1 && it.parameters.first().type.jvmErasure == Collection::class }
        if (cons != null) {
            return cons.call(new)
        } else {
            if (type.isSuperclassOf(MutableList::class)) {
                return new as T
            }
            error("$type is collection but no constructor with single Collection parameter found, cons: ${type.constructors}")
        }
    } else if (type.isSubclassOf(Map::class)) {
        val new = (this as Map<*, *>).toMutableMap()
        (other as Map<*, *>).forEach { (k, v) ->
            if (k in new) {
                new[k] = new[k].merge(v)
            } else {
                new[k] = v
            }
        }
        val cons =
            type.constructors.find { it.parameters.size == 1 && it.parameters.first().type.jvmErasure == Map::class }
        if (cons != null) {
            return cons.call(new)
        } else {
            if (type.isSuperclassOf(MutableMap::class)) {
                return new as T
            }
            error("$type is map but no constructor with single Map parameter found, cons: ${type.constructors}")
        }
    } else if (type.isSubclassOf(Number::class)) {
        if ((other as Number) == 0) {
            return this
        } else {
            return other
        }
    } else if (type == Any::class) {
        try {
            return merge(other, other::class)
        } catch (e: Exception) {
            throw IllegalStateException("Error trying merging $other with Any as ${other::class}", e)
        }
    }
    throw UnsupportedOperationException("Merging $type objects are not supported")
}