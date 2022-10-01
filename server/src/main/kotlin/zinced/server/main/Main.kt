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
package zinced.server.main

import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import zinced.server.mw.MediaWiki
import zinced.server.mw.model.toPageID
import zinced.server.mw.model.toPageName
import zinced.server.web.WebServer

fun main(args: Array<String>) {
    WebServer.start()
    runBlocking {
        MediaWiki.queryPageMetadata(id = setOf(6744.toPageID())).collectLatest {
            println(it)
        }
        println(Json.encodeToString(MediaWiki.getPageContent(id = MediaWiki.getPageID(title = "密度函数".toPageName())).sections))
    }
}
