package zinced.server.mw.wikitext

import org.sweble.wikitext.parser.nodes.WtParsedWikitextPage
import org.sweble.wikitext.parser.nodes.WtTemplate
import zinced.common.PageName
import zinced.common.lowercaseFirstChar

data class ParsedWikitext(
    val displayTitle: String,
    val tree: WtParsedWikitextPage
) {

    fun hasTemplate(name: PageName) = hasTemplate(name.name)

    fun hasTemplate(name: String) = hasTemplate0(name.lowercaseFirstChar())

    private fun hasTemplate0(name: String) =
        tree.entityMap.map.any { (_, t) -> t is WtTemplate && t.name.isResolved && t.name.asString.lowercaseFirstChar() == name }

}