package zinced.server.web.cache

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.core.plugin.Plugin
import io.javalin.http.BadRequestResponse
import io.javalin.http.HttpCode
import io.javalin.http.NotFoundResponse
import kotlinx.coroutines.runBlocking
import zinced.common.cache.PageCacheInfo
import zinced.common.toPageID
import zinced.server.database.cache.PageCaches
import zinced.server.util.withType
import kotlin.math.min

object PageCachesEndpointsPlugin : Plugin {

    override fun apply(app: Javalin) {
        app.routes {
            path("/api/v1/page_caches") {
                get { ctx ->
                    val count = min(
                        (ctx.queryParam("count") ?: "100").toIntOrNull()
                            ?: throw BadRequestResponse("Invalid count number"), 200
                    )
                    val page =
                        (ctx.queryParam("page") ?: "0").toIntOrNull()
                            ?: throw BadRequestResponse("Invalid page number")
                    runBlocking {
                        ctx.json(
                            PageCaches.getAll(page * count, count)
                                .map { it.toInfo(false) }.withType()
                        )
                    }
                }
                delete { ctx ->
                    runBlocking {
                        PageCaches.clear()
                        ctx.json(true)
                    }
                }
                get("count") { ctx ->
                    runBlocking {
                        ctx.json(PageCaches.count())
                    }
                }
                path("outdated") {
                    get { ctx ->
                        runBlocking {
                            ctx.json(PageCaches.getOutdated().map { it.toInfo(false) }.withType())
                        }
                    }
                    delete { ctx ->
                        runBlocking {
                            PageCaches.clearOutdated()
                            ctx.json(true)
                        }
                    }
                }
                path("{id}") {
                    get { ctx ->
                        val page = ctx.pathParam("id").toIntOrNull()?.toPageID()
                            ?: throw BadRequestResponse("Invalid id number format")
                        val queryOnly = (ctx.queryParam("queryOnly") ?: "false").toBooleanStrictOrNull()
                            ?: throw BadRequestResponse("Invalid queryOnly value")
                        runBlocking {
                            val result =
                                (if (queryOnly) PageCaches.find(page)
                                else PageCaches.tryGet(page)) ?: throw NotFoundResponse()
                            ctx.json(result.toInfo(true))
                        }
                    }
                    post { ctx ->
                        val page = ctx.pathParam("id").toIntOrNull()?.toPageID()
                            ?: throw BadRequestResponse("Invalid id number format")
                        runBlocking {
                            PageCaches.cache(page) ?: throw NotFoundResponse()
                            ctx.status(HttpCode.CREATED)
                                .json(true)
                        }
                    }
                    delete { ctx ->
                        val page = ctx.pathParam("id").toIntOrNull()?.toPageID()
                            ?: throw BadRequestResponse("Invalid id number format")
                        runBlocking {
                            PageCaches.delete(page)
                            ctx.json(true)
                        }
                    }
                }
            }
        }
    }

}