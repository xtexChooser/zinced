package zinced.server.mw.model

import kotlinx.serialization.Serializable
import zinced.server.mw.data.MwParseResponse

@Serializable
data class PageContent(
    val pageID: PageID,
    val title: PageName,
    val wikitext: String,
    val links: List<Link>,
    val externalLinks: List<String>,
    val sections: List<Section>,
) {

    companion object {

        fun from(entry: MwParseResponse.Parse) = PageContent(
            pageID = entry.pageID.toPageID(),
            title = entry.title.toPageName(),
            wikitext = entry.wikitext.text,
            links = entry.links.map { from(it) },
            externalLinks = entry.externalLinks,
            sections = from(entry.sections)
        )

        fun from(entry: MwParseResponse.LinksEntry) = Link(
            ns = entry.ns.toNS(),
            title = entry.title.toPageName(),
            exists = entry.exists != null,
        )

        fun from(entries: Collection<MwParseResponse.SectionsEntry>) = from(entries.toList().listIterator())

        fun from(entries: ListIterator<MwParseResponse.SectionsEntry>): List<Section> {
            val sections = mutableListOf<Section>()
            while (entries.hasNext()) {
                val parent = entries.next()
                val child = mutableListOf<MwParseResponse.SectionsEntry>()
                while (entries.hasNext()) {
                    val next = entries.next()
                    if (next.tocLevel > parent.tocLevel) {
                        child.add(next)
                    } else {
                        entries.previous()
                        break
                    }
                }
                sections.add(
                    Section(
                        headerLevel = parent.level.toInt(),
                        index = parent.index.toInt(),
                        text = parent.line,
                        prefix = parent.number,
                        anchor = parent.anchor,
                        child = if(child.isEmpty()) emptyList() else from(child.listIterator())
                    )
                )
            }
            return sections.toList()
        }

    }

    @Serializable
    data class Link(
        val ns: NamespaceID,
        val title: PageName,
        val exists: Boolean,
    )

    @Serializable
    data class Section(
        val headerLevel: Int,
        val index: Int,
        val text: String,
        val prefix: String,
        val anchor: String,
        val child: List<Section>,
    )

}
