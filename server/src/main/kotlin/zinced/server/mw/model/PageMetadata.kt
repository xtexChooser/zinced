package zinced.server.mw.model

import zinced.common.*
import zinced.server.mw.data.MwQueryResponse

fun PageMetadata.Companion.from(entry: MwQueryResponse.Pages) = PageMetadata(
    id = entry.pageID.toPageID(),
    title = entry.title!!.toPageName(),
    ns = entry.namespace!!.toNS(),
    displayTitle = entry.displayTitle!!,
    lang = entry.langLinks.associate { it.lang.toLanguage() to it.title.toPageName() },
    anonymousContributors = entry.anonymousContributors,
    contributors = entry.contributors.map { it.userId }.toSet(),
    categories = entry.categories.map { it.ns.toNS() to it.title.toPageName() }.toSet(),
    templates = entry.templates.map { it.ns.toNS() to it.title.toPageName() }.toSet(),
    summary = entry.summary!!,
)