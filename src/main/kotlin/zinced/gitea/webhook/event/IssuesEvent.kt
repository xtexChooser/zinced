package zinced.gitea.webhook.event

import io.gitea.model.Issue
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.UseSerializers

@Serializable
data class IssuesEvent(
    val action: String,
    val number: Long,
    val sender: Sender
) {

    companion object {

        const val ACTION_OPENED = "opened"

    }

}
