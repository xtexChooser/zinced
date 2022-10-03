package zinced.frontend.ui.page.cache

import androidx.compose.runtime.Composable
import app.softwork.routingcompose.NavLink
import app.softwork.routingcompose.RouteBuilder
import org.jetbrains.compose.web.dom.*
import zinced.client.endpoint.*
import zinced.common.PageID
import zinced.common.toPageID
import zinced.frontend.ApiClient
import zinced.frontend.ui.components.BooleanOperationConfirm
import zinced.frontend.ui.components.LoadingData
import zinced.frontend.ui.components.Nav
import zinced.frontend.ui.components.PagedData
import zinced.frontend.ui.components.elements.DDescription
import zinced.frontend.ui.components.elements.DTerm
import zinced.frontend.ui.components.elements.Li
import zinced.frontend.ui.components.elements.Pre
import kotlin.math.ceil

@Composable
fun RouteBuilder.PageCachesPage() {
    Nav(
        mapOf(
            "索引" to "/page_caches/index",
            "清空" to "/page_caches/clear/all",
            "过期缓存" to "/page_caches/outdated",
        )
    )
    route("index") { Index() }
    int { Info(it.toPageID()) }
    route("clear") {
        route("all") { ClearAll() }
        int { ClearPage(it.toPageID()) }
    }
    route("update") {
        int { UpdatePage(it.toPageID()) }
    }
    route("outdated") {
        Nav(
            mapOf(
                "索引" to "/page_caches/outdated/index",
                "清空" to "/page_caches/outdated/clear",
            )
        )
        route("index") { OutdatedIndex() }
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
                            NavLink("/page_caches/${info.id}") { Text("查看") }
                            Text("|")
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
                    NavLink("/page_caches/${info.id}") { Text("查看") }
                    Text("|")
                    NavLink("/page_caches/update/${info.id}") { Text("刷新") }
                    Text("|")
                    NavLink("/page_caches/clear/${info.id}") { Text("清除") }
                }
            }
        }
    }
}

@Composable
private fun Info(page: PageID) {
    LoadingData(loader = { ApiClient.getPageCache(page) }) { info ->
        DList {
            Div {
                DTerm("页面ID")
                DDescription(info.id.toString())
            }
            Div {
                DTerm("最后更新")
                DDescription(info.updated.toString())
            }
            val metadata = info.metadata!!
            Div {
                DTerm("标题")
                DDescription(metadata.title.name)
            }
            Div {
                DTerm("命名空间ID")
                DDescription(metadata.ns.toString())
            }
            Div {
                DTerm("显示标题")
                DDescription(metadata.displayTitle)
            }
            Div {
                DTerm("摘要")
                DDescription(metadata.summary)
            }
            Div {
                DTerm("跨语言链接")
                DDescription {
                    DList {
                        metadata.lang.forEach { (lang, name) ->
                            Div {
                                DTerm(lang.toString())
                                DDescription(name.toString())
                            }
                        }
                    }
                }
            }
            Div {
                DTerm("匿名贡献者")
                DDescription(metadata.anonymousContributors.toString())
            }
            Div {
                DTerm("贡献者")
                DDescription(metadata.contributors.joinToString(separator = " | "))
            }
            Div {
                DTerm("分类")
                DDescription {
                    Ul {
                        metadata.categories.forEach { (ns, name) ->
                            Li("（$ns）$name")
                        }
                    }
                }
            }
            Div {
                DTerm("模板")
                DDescription {
                    Ul {
                        metadata.templates.forEach { (ns, name) ->
                            Li("（$ns）$name")
                        }
                    }
                }
            }
        }
        Pre(info.content ?: "（页面内容过大，未被缓存）")
    }
}
