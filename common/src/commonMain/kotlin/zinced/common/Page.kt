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
@file:Suppress("NOTHING_TO_INLINE")

package zinced.common

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
value class PageID(val id: Int) {

    override fun toString() = id.toString()

}

inline fun Number.toPageID() = PageID(toInt())

@JvmInline
@Serializable
value class PageName(val name: String) {

    override fun toString() = name

}

inline fun String.toPageName() = PageName(this)
inline fun CharSequence.toPageName() = PageName(toString())

fun String.lowercaseFirstChar() = replaceFirstChar { it.lowercaseChar() }
