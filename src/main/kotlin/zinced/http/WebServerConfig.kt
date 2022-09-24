package zinced.http

import kotlinx.serialization.Serializable

@Serializable
data class WebServerConfig(
    val host: String = "0.0.0.0",
    val port: Int,
)
