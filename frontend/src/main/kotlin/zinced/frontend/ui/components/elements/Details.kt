package zinced.frontend.ui.components.elements

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.ElementBuilder
import org.jetbrains.compose.web.dom.TagElement
import org.w3c.dom.HTMLDetailsElement
import org.w3c.dom.HTMLElement

private val Details: ElementBuilder<HTMLDetailsElement> = ElementBuilder.createBuilder("details")

@Composable
fun Details(
    attrs: AttrBuilderContext<HTMLDetailsElement>? = null,
    content: ContentBuilder<HTMLDetailsElement>? = null
) = TagElement(elementBuilder = Details, applyAttrs = attrs, content = content)

private val Summary: ElementBuilder<HTMLElement> = ElementBuilder.createBuilder("summary")

@Composable
fun Summary(
    attrs: AttrBuilderContext<HTMLElement>? = null,
    content: ContentBuilder<HTMLElement>? = null
) = TagElement(elementBuilder = Summary, applyAttrs = attrs, content = content)
