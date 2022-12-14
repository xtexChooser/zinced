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

import kotlinx.coroutines.runBlocking
import zinced.common.toPageName
import zinced.server.database.Database
import zinced.server.database.cache.PageCaches
import zinced.server.mw.model.toID
import zinced.server.web.WebServer

fun main(args: Array<String>) {
    WebServer.start()
    Database

}
