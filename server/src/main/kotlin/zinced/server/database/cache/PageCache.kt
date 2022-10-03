package zinced.server.database.cache

import jakarta.persistence.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import zinced.server.database.util.KtxInstantConverter
import zinced.common.PageMetadata
import zinced.common.cache.PageCacheInfo
import zinced.common.toPageID

@Entity
@Table(
    name = "mw_page_cache",
    indexes = [
        Index(
            name = "by_id",
            columnList = "id",
            unique = true
        )
    ]
)
@NamedQuery(
    name = "countAllPageCache",
    query = "select count(c) from PageCache as c"
)
@NamedQuery(
    name = "getAllPageCache",
    query = "select c from PageCache as c"
)
@NamedQuery(
    name = "deletePageCache",
    query = "delete from PageCache as c where c.id = :id"
)
@NamedQuery(
    name = "clearPageCaches",
    query = "delete from PageCache"
)
@NamedQuery(
    name = "getPageCachesBefore",
    query = "select c from PageCache as c where c.updated < :time"
)
@NamedQuery(
    name = "clearPageCachesBefore",
    query = "delete from PageCache as c where c.updated < :time"
)
open class PageCache(
    @Id
    open var id: Int,
    @Convert(converter = KtxInstantConverter::class)
    open var updated: Instant = Clock.System.now(),
    @Basic(fetch = FetchType.LAZY)
    @JdbcTypeCode(SqlTypes.JSON)
    open var metadata: PageMetadata,
    @Basic(fetch = FetchType.LAZY, optional = true)
    @JdbcTypeCode(SqlTypes.LONGNVARCHAR)
    @Lob
    open var content: String?,
) {

    fun toInfo(full: Boolean) = if (full)
        PageCacheInfo(
            id = id.toPageID(),
            updated = updated,
            metadata = metadata,
            content = content,
        ) else
        PageCacheInfo(
            id = id.toPageID(),
            updated = updated,
        )

}
