package zinced.server.database

import kotlinx.serialization.Serializable

@Serializable
data class DatabaseConfig(
    val properties: Map<String, String> = emptyMap(),
)
