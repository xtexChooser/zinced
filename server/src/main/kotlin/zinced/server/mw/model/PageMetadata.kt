/*
 * Copyright 2022 xtexChooser
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package zinced.server.mw.model

import kotlinx.serialization.Serializable
import zinced.server.mw.data.MwQueryResponse

@Serializable
data class PageMetadata(
    val id: PageID,
    val title: PageName,
    val ns: NamespaceID,
    val displayTitle: String,
    val lang: Map<LanguageID, PageName>,
    val anonymousContributors: Int,
    val contributors: Set<Int>,
    val categories: Set<Pair<NamespaceID, PageName>>,
    val templates: Set<Pair<NamespaceID, PageName>>,
    val summary: String,
) {

    companion object {

        fun from(entry: MwQueryResponse.Pages) = PageMetadata(
            id = entry.pageID.toPageID(),
            title = entry.title.toPageName(),
            ns = entry.namespace.toNS(),
            displayTitle = entry.displayTitle!!,
            lang = entry.langLinks.associate { it.lang.toLanguage() to it.title.toPageName() },
            anonymousContributors = entry.anonymousContributors,
            contributors = entry.contributors.map { it.userId }.toSet(),
            categories = entry.categories.map { it.ns.toNS() to it.title.toPageName() }.toSet(),
            templates = entry.templates.map { it.ns.toNS() to it.title.toPageName() }.toSet(),
            summary = entry.summary!!,
        )

    }

}
