package zinced.gitea

import io.gitea.ApiClient
import io.gitea.Configuration
import io.gitea.api.IssueApi
import io.gitea.auth.ApiKeyAuth
import io.gitea.model.CreateIssueCommentOption
import okhttp3.OkHttpClient
import okhttp3.Request
import zinced.config.Config

object Gitea {

    val config get() = Config.gitea
    val repoOwner get() = config.repoOwner
    val repoName get() = config.repoName

    val defaultClient: ApiClient get() = Configuration.getDefaultApiClient()
    val httpClient = OkHttpClient.Builder()
        .build()

    init {
        defaultClient.basePath = config.basePath + "api/v1"

        val auth = defaultClient.getAuthentication("AccessToken") as ApiKeyAuth
        auth.apiKey = config.token
    }

    val issueApi = IssueApi()

    fun commentIssue(index: Long, content: String) {
        issueApi.issueCreateComment(config.repoOwner, config.repoName, index, CreateIssueCommentOption().body(content))
    }

}