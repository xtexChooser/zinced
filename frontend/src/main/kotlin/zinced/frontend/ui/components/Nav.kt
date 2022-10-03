package zinced.frontend.ui.components

import androidx.compose.runtime.Composable
import app.softwork.routingcompose.NavLink
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import zinced.frontend.ui.style.NavStylesheet

@Composable
fun Nav(links: Map<String, String>) {
    Div(attrs = { classes(NavStylesheet.navBox) }) {
        org.jetbrains.compose.web.dom.Nav {
            links.forEach { (text, link) ->
                NavLink(to = link, attrs = { active ->
                    classes(NavStylesheet.navLink)
                    if (active) {
                        classes(NavStylesheet.navLinkActive)
                    }
                }) {
                    Text(text)
                }
            }
        }
    }
}

@Composable
fun SiteNav() = Nav(
    mapOf(
        "问题" to "/issues",
        "页面缓存" to "/page_caches",
        "关于" to "/about",
    )
)
