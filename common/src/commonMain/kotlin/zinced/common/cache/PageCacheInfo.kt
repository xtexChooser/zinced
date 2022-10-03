package zinced.common.cache

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import zinced.common.PageID
import zinced.common.PageMetadata

@Serializable
data class PageCacheInfo(
    val id: PageID,
    val updated: Instant,
    val metadata: PageMetadata? = null,
    val content: String? = null,
)
