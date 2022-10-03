@file:Suppress("NOTHING_TO_INLINE")

package zinced.frontend.ui.components.elements

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Pre
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.*

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

@Composable
inline fun Button(text: String, noinline attrs: AttrBuilderContext<HTMLButtonElement>? = null) =
    Button(attrs) { Text(text) }

@Composable
inline fun Pre(text: String, noinline attrs: AttrBuilderContext<HTMLPreElement>? = null) =
    Pre(attrs) { Text(text) }

