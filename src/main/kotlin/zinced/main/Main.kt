package zinced.main

import io.gitea.ApiClient
import io.gitea.ApiException
import io.gitea.Configuration
import io.gitea.api.AdminApi
import io.gitea.auth.ApiKeyAuth
import io.gitea.auth.HttpBasicAuth
import io.gitea.model.CreateIssueOption
import zinced.gitea.Gitea
import zinced.http.WebServer

fun main(args: Array<String>) {
    WebServer.start()
}
