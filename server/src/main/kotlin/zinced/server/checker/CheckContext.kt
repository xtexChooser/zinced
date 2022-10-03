package zinced.server.checker

import zinced.server.mw.model.PageContent
import zinced.common.PageID
import zinced.common.PageMetadata
import zinced.common.PageName
import zinced.server.mw.wikitext.ParsedWikitext

data class CheckContext(
    val id: PageID,
    val meta: PageMetadata,
    val content: PageContent,
    val wt: ParsedWikitext,
) {

    fun hasTemplate(name: PageName) = wt.hasTemplate(name)
    fun hasTemplate(name: String) = wt.hasTemplate(name)

}
