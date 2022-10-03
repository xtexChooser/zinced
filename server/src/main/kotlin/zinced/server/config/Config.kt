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
package zinced.server.config

import com.typesafe.config.ConfigFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.hocon.decodeFromConfig
import zinced.server.database.DatabaseConfig
import zinced.server.web.WebServerConfig
import zinced.server.mw.MediaWikiConfig
import java.io.File

val Config =
    @OptIn(ExperimentalSerializationApi::class) Hocon.decodeFromConfig<AppConfig>(ConfigFactory.parseFile(File("zinced.conf")))

@Serializable
data class AppConfig(
    val web: WebServerConfig,
    val mediawiki: MediaWikiConfig,
    val database: DatabaseConfig,
)
