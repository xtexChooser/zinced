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
    val ns: Namespace,
    val displayTitle: String,
    val lang: Map<LanguageID, String>,
    val anonymousContributors: Int,
    val contributors: Set<Int>,
    val categories: Set<Pair<Namespace, PageName>>,
    val templates: Set<Pair<Namespace, PageName>>,
    val summary: String,
) {

    companion object {

        fun fromQueryResponse(entry: MwQueryResponse.Query.PagesEntry) = PageMetadata(
            id = PageID(entry.pageId),
            title = PageName(entry.title),
            ns = Namespace(entry.namespace),
            displayTitle = entry.displayTitle!!,
            lang = entry.langLinks.associate { LanguageID(it.lang) to it.title },
            anonymousContributors = entry.anonymousContributors,
            contributors = entry.contributors.map { it.userId }.toSet(),
            categories = entry.categories.map { Namespace(it.ns) to PageName(it.title) }.toSet(),
            templates = entry.templates.map { Namespace(it.ns) to PageName(it.title) }.toSet(),
            summary = entry.summary!!,
        )

    }

}
