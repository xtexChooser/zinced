package zinced.gitea

import kotlinx.serialization.Serializable

@Serializable
data class GiteaConfig(
    val basePath: String,
    val token: String,
    val repoOwner: String,
    val repoName: String,
    val botName: String,
)
