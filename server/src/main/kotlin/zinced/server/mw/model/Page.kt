package zinced.server.mw.model

import zinced.common.PageID
import zinced.common.PageName
import zinced.server.mw.MediaWiki

suspend fun PageID.toName() = MediaWiki.getPageName(id = this)
suspend fun PageID.toMetadata() = MediaWiki.queryPageMetadata(id = this)
suspend fun PageID.toContent() = MediaWiki.getPageContent(id = this)

suspend fun PageName.toID() = MediaWiki.getPageID(title = this)
