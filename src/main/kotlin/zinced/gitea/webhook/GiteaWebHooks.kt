package zinced.gitea.webhook

import io.javalin.Javalin
import mu.KotlinLogging
import zinced.gitea.Gitea
import zinced.gitea.GiteaMessages
import zinced.gitea.webhook.event.IssuesEvent

object GiteaWebHooks {

    private val logger = KotlinLogging.logger {  }

    fun register(javalin: Javalin) {
        javalin.post("/webhook") { ctx ->
            logger.info { "Get webhook event ${ctx.giteaEvent}(${ctx.giteaDelivery})" }
            when(ctx.giteaEvent) {
                "issues" -> {
                    val event = ctx.bodyAsClass<IssuesEvent>()
                    if(event.action == IssuesEvent.ACTION_OPENED && event.sender.login != Gitea.config.botName) {
                        Gitea.commentIssue(event.number, GiteaMessages.ISSUES_MANUALLY_OPEN)
                    }
                }
            }
            ctx.result("Received")
        }
    }

}