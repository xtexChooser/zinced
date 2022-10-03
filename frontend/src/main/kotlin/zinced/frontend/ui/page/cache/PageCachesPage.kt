package zinced.frontend.ui.page.cache

import androidx.compose.runtime.Composable
import app.softwork.routingcompose.NavLink
import app.softwork.routingcompose.RouteBuilder
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul
import zinced.client.endpoint.*
import zinced.common.PageID
import zinced.common.toPageID
import zinced.frontend.ApiClient
import zinced.frontend.ui.components.*
import kotlin.math.ceil
import kotlin.math.round

@Composable
fun RouteBuilder.PageCachesPage() {
    Nav(
        mapOf(
            "索引" to "/page_caches/index",
            "清空" to "/page_caches/clear",
            "过期缓存" to "/page_caches/outdated",
        )
    )
    // get
    noMatch { Index() }
    route("clear") {
        noMatch { ClearAll() }
        int { ClearPage(it.toPageID()) }
    }
    route("update") {
        int { UpdatePage(it.toPageID()) }
    }
    route("outdated") {
        Nav(
            mapOf(
                "索引" to "/page_caches/outdated",
                "清空" to "/page_caches/outdated/clear",
            )
        )
        noMatch { OutdatedIndex() }
        route("clear") { ClearOutdated() }
    }
}

@Composable
private fun Index() {
    LoadingData(loader = { ceil(ApiClient.countPageCaches().toFloat() / PAGE_CACHES_QUERY_SIZE).toInt() }) { pages ->
        PagedData(pageCount = pages) { page ->
            LoadingData(loader = { ApiClient.listPageCaches(page) }) {
                Ul {
                    it.forEach { info ->
                        Li {
                            Text("${info.id}（更新于 ${info.updated}）|")
                            NavLink("/page_caches/update/${info.id}") { Text("刷新") }
                            Text("|")
                            NavLink("/page_caches/clear/${info.id}") { Text("清除") }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ClearAll() {
    BooleanOperationConfirm("确认要清除所有页面缓存吗？") { ApiClient.clearPageCaches() }
}

@Composable
private fun ClearPage(page: PageID) {
    BooleanOperationConfirm("确认要清除 $page 的缓存吗？") { ApiClient.clearPageCache(page) }
}

@Composable
private fun UpdatePage(page: PageID) {
    BooleanOperationConfirm("确认要刷新 $page 的缓存吗？") { ApiClient.updatePageCache(page) }
}

@Composable
private fun ClearOutdated() {
    BooleanOperationConfirm("确认要清除过时的页面缓存吗？") { ApiClient.clearOutdatedPageCaches() }
}

@Composable
private fun OutdatedIndex() {
    LoadingData(loader = { ApiClient.listOutdatedPageCaches() }) { pages ->
        Ul {
            pages.forEach { info ->
                Li {
                    Text("${info.id}（更新于 ${info.updated}）|")
                    NavLink("/page_caches/update/${info.id}") { Text("刷新") }
                    Text("|")
                    NavLink("/page_caches/clear/${info.id}") { Text("清除") }
                }
            }
        }
    }
}
