package zinced.frontend.ui.components

import androidx.compose.runtime.Composable
import app.softwork.routingcompose.NavLink
import app.softwork.routingcompose.Parameters
import app.softwork.routingcompose.Router
import app.softwork.routingcompose.navigate
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.alt
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import zinced.frontend.ui.style.NavStylesheet
import zinced.frontend.ui.style.Styles
import kotlin.math.max
import kotlin.math.min

@Composable
fun PagedData(pageCount: Int, parameterName: String = "page", content: @Composable (Int) -> Unit) {
    val router = Router.current
    val currentParameters = router.currentPath.parameters?.map

    inline fun buildPath(page: Int) = router.currentPath
        .copy(
            parameters = Parameters.from((currentParameters?.toMutableMap() ?: mutableMapOf()).apply {
                this[parameterName] = listOf(page.toString())
            })
        )
        .toString()

    val currentPage = currentParameters?.get(parameterName)?.singleOrNull()?.toInt() ?: 1
    if (pageCount < 1) {
        Span(attrs = { classes(Styles.textBold) }) { Text("无数据") }
        return
    } else if (currentPage < 1 || currentPage > pageCount) {
        router.navigate(buildPath(1))
        return
    }
    content(currentPage - 1)

    Div(attrs = { classes(NavStylesheet.navBox) }) {
        org.jetbrains.compose.web.dom.Nav {
            if (currentPage != 1) {
                NavLink(to = buildPath(currentPage - 1), attrs = { _ -> classes(NavStylesheet.navLink) }) {
                    Text("上一页")
                }
            }
            Text("第 $currentPage 页")
            if (currentPage != pageCount) {
                NavLink(to = buildPath(currentPage + 1), attrs = { _ -> classes(NavStylesheet.navLink) }) {
                    Text("下一页")
                }
            }
            Span {
                var value = 0
                Input(type = InputType.Number) {
                    alt("转到")
                    onInput {
                        value = it.value?.toInt() ?: 0
                    }
                    onKeyUp {
                        if (it.key == "Enter" && value != 0) {
                            router.navigate(buildPath(value))
                        }
                    }
                }
            }
        }
    }
}