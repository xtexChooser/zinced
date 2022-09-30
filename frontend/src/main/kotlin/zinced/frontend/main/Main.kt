package zinced.frontend.main

import org.jetbrains.compose.web.renderComposable
import zinced.frontend.ApiClient
import zinced.frontend.ui.ZincedFrontend

fun main() {
    console.info("API endpoint: ${ApiClient.baseUrl}")
    renderComposable("root") {
        ZincedFrontend()
    }
}