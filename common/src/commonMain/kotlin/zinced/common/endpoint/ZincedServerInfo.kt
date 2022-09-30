package zinced.common.endpoint

import kotlinx.serialization.Serializable

@Serializable
data class ZincedServerInfo(
    val software: String,
    val version: String,
    val userAgent: String,
    val mediaWikiApiUrl: String,
    val mediaWikiLanguage: String,
    val mediaWikiViewUrl: String,
)
