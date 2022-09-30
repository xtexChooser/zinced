package zinced.client

import kotlinx.browser.window
import kotlinx.coroutines.await
import org.w3c.fetch.*

actual suspend fun callHttpJsonApi(url: String, method: String, body: String?): String {
    val response = window.fetch(
        input = url,
        init = RequestInit(
            method = method,
            body = body,
            mode = RequestMode.SAME_ORIGIN,
            cache = RequestCache.NO_CACHE,
            redirect = RequestRedirect.FOLLOW,
            headers = mapOf(
                "Content-Type" to "application/json"
            )
        )
    ).await()
    if (response.ok) {
        return response.text().await()
    } else {
        throw HttpErrorStatusException(response.status.toInt())
    }
}