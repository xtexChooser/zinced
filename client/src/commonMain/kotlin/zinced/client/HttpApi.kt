package zinced.client

@Throws(HttpErrorStatusException::class)
internal expect suspend fun callHttpJsonApi(url: String, method: String, body: String? = null): String

class HttpErrorStatusException(val statusCode: Int): Exception() {
}
