package zinced.server.database.cache

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import zinced.common.PageID
import zinced.server.database.Database
import zinced.server.mw.MediaWiki
import kotlin.time.Duration.Companion.days
import kotlin.time.ExperimentalTime

object PageCaches {

    suspend fun count() =
        withContext(Dispatchers.IO) {
            Database.transaction {
                createNamedQuery("countAllPageCache")
                    .singleResult as Long
            }
        }

    @Suppress("UNCHECKED_CAST")
    suspend fun getAll(first: Int, count: Int) =
        withContext(Dispatchers.IO) {
            Database.transaction {
                createNamedQuery("getAllPageCache")
                    .setFirstResult(first)
                    .setMaxResults(count)
                    .resultList!! as List<PageCache>
            }
        }

    suspend fun find(id: PageID): PageCache? =
        withContext(Dispatchers.IO) {
            Database.transaction {
                find(PageCache::class.java, id.id)
            }
        }

    suspend fun get(id: PageID) = tryGet(id) ?: error("Page $id not found")

    suspend fun tryGet(id: PageID) = find(id) ?: createCache(id)

    suspend fun cache(id: PageID): PageCache? {
        val current = find(id)
        if (current != null) {
            withContext(Dispatchers.IO) {
                Database.transaction {
                    remove(current)
                }
            }
        }
        return createCache(id)
    }

    suspend fun createCache(id: PageID): PageCache? {
        val metadata = MediaWiki.queryPageMetadata(id = id) ?: return null
        val content = MediaWiki.getPageContent(id = id)
        val cache = PageCache(
            id = id.id,
            metadata = metadata,
            content = content.wikitext.takeIf { it.length <= 8 * 1024 }
        )

        withContext(Dispatchers.IO) {
            Database.transaction {
                persist(cache)
            }
        }
        return cache
    }

    suspend fun delete(id: PageID) {
        withContext(Dispatchers.IO) {
            Database.transaction {
                createNamedQuery("deletePageCache")
                    .setParameter("id", id.id)
                    .executeUpdate()
            }
        }
    }

    suspend fun clear() {
        withContext(Dispatchers.IO) {
            Database.transaction {
                createNamedQuery("clearPageCaches")
                    .executeUpdate()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun getBefore(timestamp: Instant) = withContext(Dispatchers.IO) {
        Database.transaction {
            createNamedQuery("getPageCachesBefore")
                .setParameter("time", timestamp)
                .resultList!! as List<PageCache>
        }.toList()
    }

    suspend fun clearBefore(timestamp: Instant) {
        withContext(Dispatchers.IO) {
            Database.transaction {
                createNamedQuery("clearPageCachesBefore")
                    .setParameter("time", timestamp)
                    .executeUpdate()
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    suspend fun getOutdated() = getBefore(Clock.System.now() - 3.days)

    @OptIn(ExperimentalTime::class)
    suspend fun clearOutdated() = clearBefore(Clock.System.now() - 3.days)

}