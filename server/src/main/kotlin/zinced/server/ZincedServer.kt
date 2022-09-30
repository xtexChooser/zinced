package zinced.server

import java.net.InetAddress

object ZincedServer {

    val version = this::class.java.`package`.implementationVersion ?: getDevVersion()
    val userAgent = "Zinced/$version"

    private fun getDevVersion() = try {
        "DEV-${InetAddress.getLocalHost().canonicalHostName}"
    } catch(e: Exception) {
        "DEV"
    }

}