package zinced.frontend.ui.style

import org.jetbrains.compose.web.css.*

object NavStylesheet : StyleSheet() {

    val navBox by style {
        marginBottom(0.6.em)
        property("border-bottom", "0.2em solid ${Styles.Active}")
    }

    val navLink by style {
        color(Styles.Active)
        padding(2.px, 0.75.em)
        fontSize(110.percent)
    }

    val navLinkActive by style {
        color(Styles.Primary)
        backgroundColor(Styles.Active)
    }

}