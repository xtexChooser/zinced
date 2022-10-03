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
package zinced.server.web

import io.javalin.Javalin
import io.javalin.core.util.RouteOverviewPlugin
import io.javalin.http.staticfiles.Location
import io.javalin.http.util.RedirectToLowercasePathPlugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import zinced.server.config.Config
import zinced.server.web.cache.PageCachesEndpointsPlugin
import zinced.server.web.meta.MetaEndpointsPlugin
import zinced.server.web.util.KtxSerializationJsonMapper
import kotlin.coroutines.CoroutineContext

object WebServer : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    val config get() = Config.web
    val javalin = Javalin.create {
        it.jsonMapper(KtxSerializationJsonMapper)
        it.registerPlugin(RouteOverviewPlugin("/api/v1/routes"))
        it.registerPlugin(RedirectToLowercasePathPlugin())
        it.addStaticFiles("zinced/frontend/dist", Location.CLASSPATH)
        it.registerPlugin(MetaEndpointsPlugin)
        it.registerPlugin(PageCachesEndpointsPlugin)
    }

    fun start() {
        javalin.start(config.host, config.port)
    }

}