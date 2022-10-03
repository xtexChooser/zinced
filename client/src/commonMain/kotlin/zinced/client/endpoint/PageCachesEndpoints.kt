package zinced.client.endpoint

import zinced.client.ZincedClient
import zinced.common.PageID
import zinced.common.cache.PageCacheInfo

const val PAGE_CACHES_QUERY_SIZE = 100

suspend fun ZincedClient.listPageCaches(page: Int) =
    callJsonApi<List<PageCacheInfo>>(url = "/v1/page_caches?page=$page", method = "GET")

suspend fun ZincedClient.clearPageCaches() =
    callJsonApi<Boolean>(url = "/v1/page_caches", method = "DELETE")

suspend fun ZincedClient.countPageCaches() =
    callJsonApi<Int>(url = "/v1/page_caches/count", method = "GET")

suspend fun ZincedClient.listOutdatedPageCaches() =
    callJsonApi<List<PageCacheInfo>>(url = "/v1/page_caches/outdated", method = "GET")

suspend fun ZincedClient.clearOutdatedPageCaches() =
    callJsonApi<Boolean>(url = "/v1/page_caches/outdated", method = "DELETE")

suspend fun ZincedClient.getPageCache(id: PageID, queryOnly: Boolean = false) =
    callJsonApi<PageCacheInfo>(url = "/v1/page_caches/${id.id}?queryOnly=$queryOnly", method = "GET")

suspend fun ZincedClient.updatePageCache(id: PageID) =
    callJsonApi<Boolean>(url = "/v1/page_caches/${id.id}", method = "POST")

suspend fun ZincedClient.clearPageCache(id: PageID) =
    callJsonApi<Boolean>(url = "/v1/page_caches/${id.id}", method = "DELETE")
