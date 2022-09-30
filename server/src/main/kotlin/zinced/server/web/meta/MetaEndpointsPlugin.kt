package zinced.server.web.meta

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.core.plugin.Plugin
import zinced.common.endpoint.ZincedServerInfo
import zinced.server.ZincedServer
import zinced.server.mw.MediaWiki

object MetaEndpointsPlugin : Plugin {

    val serverInfo = ZincedServerInfo(
        software = "Zinced",
        version = ZincedServer.version,
        userAgent = ZincedServer.userAgent,
        mediaWikiApiUrl = MediaWiki.config.apiUrl,
        mediaWikiLanguage = MediaWiki.config.lang,
        mediaWikiViewUrl = MediaWiki.config.viewUrl,
    )

    override fun apply(app: Javalin) {
        app.routes {
            path("/api/v1/meta") {
                get("/server_info") { ctx ->
                    ctx.json(serverInfo)
                }
            }
        }
    }

}