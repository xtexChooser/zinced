package zinced.server.mw.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MwParseResponse(
    val parse: Parse,
) {

    @Serializable
    data class Parse(
        val title: String,
        @SerialName("pageid")
        val pageID: Int,
        val links: List<LinksEntry> = emptyList(),
        @SerialName("externallinks")
        val externalLinks: List<String> = emptyList(),
        val sections: List<SectionsEntry> = emptyList(),
        val wikitext: Wikitext,
        @SerialName("parsewarnings")
        val parseWarnings: List<String> = emptyList(),
    )

    @Serializable
    data class LinksEntry(
        val ns: Int,
        val exists: String? = null,
        @SerialName("*")
        val title: String,
    )

    @Serializable
    data class SectionsEntry(
        @SerialName("toclevel")
        val tocLevel: Int,
        val level: String,
        val line: String,
        val number: String,
        val index: String,
        @SerialName("fromtitle")
        val fromTitle: String? = null,
        @SerialName("byteoffset")
        val byteOffset: Int? = null,
        val anchor: String,
    )

    @Serializable
    data class Wikitext(
        @SerialName("*")
        val text: String,
    )

}
