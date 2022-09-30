package zinced.client

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.executeAsync

internal val httpClient = OkHttpClient()

@Throws(HttpErrorStatusException::class)
actual suspend fun callHttpJsonApi(url: String, method: String, body: String?): String {
    val response = httpClient.newCall(
        Request.Builder()
            .url(url)
            .method(method, body?.toRequestBody("application/json".toMediaType()))
            .build()
    ).executeAsync()
    if (!response.isSuccessful) {
        throw HttpErrorStatusException(response.code)
    }
    return response.body.string()
}