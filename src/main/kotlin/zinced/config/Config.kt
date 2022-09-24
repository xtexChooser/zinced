package zinced.config

import com.typesafe.config.ConfigFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.hocon.decodeFromConfig
import zinced.gitea.GiteaConfig
import zinced.http.WebServerConfig
import java.io.File

val Config =
    @OptIn(ExperimentalSerializationApi::class) Hocon.decodeFromConfig<AppConfig>(ConfigFactory.parseFile(File("zinced.conf")))

@Serializable
data class AppConfig(
    val gitea: GiteaConfig,
    val web: WebServerConfig,
)
