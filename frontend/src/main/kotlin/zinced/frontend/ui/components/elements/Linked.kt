@file:Suppress("NOTHING_TO_INLINE")

package zinced.frontend.ui.components.elements

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLAnchorElement

@Composable
inline fun Linked(link: String, noinline attrs: AttrBuilderContext<HTMLAnchorElement>? = null) {
    A(href = link, attrs = attrs) { Text(link) }
}
