package zinced.server.checker

import zinced.server.mw.MediaWiki
import zinced.common.PageID
import zinced.server.mw.wikitext.Wikitext

object PageChecker {

    suspend fun check(id: PageID) {
        val metadata = MediaWiki.queryPageMetadata(id = id) ?: error("Page $id not found")
        val content = MediaWiki.getPageContent(id = id)
        val ctx = CheckContext(
            id = id,
            meta = metadata,
            content = content,
            wt = Wikitext.parse(metadata.displayTitle, content.wikitext)
        )
    }

}