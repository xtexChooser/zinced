package zinced.gitea.webhook.event

import kotlinx.serialization.Serializable

@Serializable
data class Sender(
    val login: String,
)
