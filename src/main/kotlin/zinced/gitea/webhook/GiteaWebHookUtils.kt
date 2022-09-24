package zinced.gitea.webhook

import io.javalin.http.Context

val Context.giteaDelivery get() = header("X-Gitea-Delivery")!!
val Context.giteaEvent get() = header("X-Gitea-Event")!!
