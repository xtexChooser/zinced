package zinced.common.issue

import kotlinx.serialization.Serializable

@Serializable
data class PageIssues(
    val errors: PageErrors,
    val alerts: PageAlerts,
    val wants: PageWants,
)

@Serializable
data class PageErrors(
    val a: Int = 0,
)

@Serializable
data class PageAlerts(
    val a: Int = 0,
)

@Serializable
data class PageWants(
    val a: Int = 0,
)
