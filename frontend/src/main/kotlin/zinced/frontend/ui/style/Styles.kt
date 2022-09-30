package zinced.frontend.ui.style

import org.jetbrains.compose.web.css.*

object Styles : StyleSheet() {

    val Gray33 = rgb(51, 51, 51)
    val Gray44 = rgb(68, 68, 68)
    val Gray66 = rgb(102, 102, 102)
    val Gray77 = rgb(119, 119, 119)
    val Gray99 = rgb(153, 153, 153)
    val GrayAA = rgb(170, 170, 170)
    val GrayCC = rgb(204, 204, 204)

    val Blue33CC = rgb(51, 51, 204)

    val Primary = GrayAA
    val Secondary = Gray77
    val Text = Gray33
    val Active = Gray44
    val Background = GrayCC

    init {
        universal style {
            color(Text)
        }
        type("body") style {
            backgroundColor(Background)
            margin(1.em)
            fontFamily("sans-serif")
        }

        type("a") style {
            color(Styles.Blue33CC)
            textDecoration("none")
        }
        type("a") + hover style {
            textDecoration("underline")
        }

        type("details") style {
            border {
                width(0.1.em)
                style(LineStyle.Solid)
                color(Primary)
            }
            borderRadius(3.px)
            padding(0.5.em)
            paddingBottom(0.em)
        }
        type("summary") style {
            fontWeight("bold")
            paddingBottom(0.5.em)
        }
        (type("details") + attr("open")) style {
            padding(0.5.em)
        }
        (type("details") + type("summary")) style {
            property("border-bottom", "0.1em solid $Primary")
            marginBottom(0.5.em)
        }

        type("dt") style {
            fontWeight("bold")
        }
        (type("dt") + after) style {
            property("content", "：")
        }

        type("code") style {
            padding(0.125.cssRem, 0.25.cssRem)
            backgroundColor(Primary)
        }
    }

    val textBold by style {
        fontWeight("bold")
    }

}