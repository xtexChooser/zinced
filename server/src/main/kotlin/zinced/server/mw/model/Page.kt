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

package zinced.server.mw.model

import kotlinx.coroutines.flow.toList
import kotlinx.serialization.Serializable
import zinced.server.mw.MediaWiki

@JvmInline
@Serializable
value class PageID(val id: Int) {

    override fun toString() = id.toString()

    suspend fun toName() = MediaWiki.getPageName(id = this)
    suspend fun toMetadata() = MediaWiki.queryPageMetadata(id = listOf(this)).toList().single()
    suspend fun toContent() = MediaWiki.getPageContent(id = this)

}

inline fun Number.toPageID() = PageID(toInt())

@JvmInline
@Serializable
value class PageName(val name: String) {

    override fun toString() = name

    suspend fun toID() = MediaWiki.getPageID(title = this)

}

inline fun String.toPageName() = PageName(this)
inline fun CharSequence.toPageName() = PageName(toString())
