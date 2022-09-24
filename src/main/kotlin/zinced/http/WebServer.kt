package zinced.http

import io.javalin.Javalin
import io.javalin.core.util.RouteOverviewPlugin
import kotlinx.serialization.json.Json
import zinced.config.Config
import zinced.gitea.webhook.GiteaWebHooks
import zinced.http.util.KtxSerializationJsonMapper

object WebServer {

    val config get() = Config.web
    val javalin = Javalin.create {
        it.jsonMapper(KtxSerializationJsonMapper())
        it.registerPlugin(RouteOverviewPlugin("/"))
    }

    fun start() {
        GiteaWebHooks.register(javalin)
        javalin.start(config.host, config.port)
    }

}