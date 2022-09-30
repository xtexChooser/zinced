package zinced.frontend

import kotlinx.browser.window
import zinced.client.ZincedClient

val ApiClient = ZincedClient(
    baseUrl = window.location.origin + "/api"
)