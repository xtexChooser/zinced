package zinced.frontend.ui

import androidx.compose.runtime.Composable
import app.softwork.routingcompose.HashRouter
import org.jetbrains.compose.web.css.Style
import zinced.frontend.ui.components.elements.P
import zinced.frontend.ui.components.SiteNav
import zinced.frontend.ui.page.AboutPage
import zinced.frontend.ui.style.NavStylesheet
import zinced.frontend.ui.style.Styles

@Composable
fun ZincedFrontend() {
    Style(Styles)
    Style(NavStylesheet)
    HashRouter(initPath = "/about") {
        SiteNav()
        route("/about") { AboutPage() }
        noMatch { P("404 Route Not Found") }
    }
}