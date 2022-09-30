@file:Suppress("NOTHING_TO_INLINE")

package zinced.frontend.ui.components.elements

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLParagraphElement
import org.w3c.dom.HTMLSpanElement

@Composable
inline fun P(text: String, noinline attrs: AttrBuilderContext<HTMLParagraphElement>? = null) =
    org.jetbrains.compose.web.dom.P(attrs) { Text(text) }

@Composable
inline fun Span(text: String, noinline attrs: AttrBuilderContext<HTMLSpanElement>? = null) =
    org.jetbrains.compose.web.dom.Span(attrs) { Text(text) }

@Composable
inline fun DTerm(text: String, noinline attrs: AttrBuilderContext<HTMLElement>? = null) =
    org.jetbrains.compose.web.dom.DTerm(attrs) { Text(text) }

@Composable
inline fun DDescription(text: String, noinline attrs: AttrBuilderContext<HTMLElement>? = null) =
    org.jetbrains.compose.web.dom.DDescription(attrs) { Text(text) }

@Composable
inline fun Code(text: String, noinline attrs: AttrBuilderContext<HTMLElement>? = null) =
    org.jetbrains.compose.web.dom.Code(attrs) { Text(text) }

@Composable
inline fun Summary(text: String, noinline attrs: AttrBuilderContext<HTMLElement>? = null) =
    Summary(attrs) { Text(text) }

