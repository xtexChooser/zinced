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
package zinced.server.mw.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MwQueryResponse(
    val `continue`: MwContinue? = null,
    val query: Query,
) {

    @Serializable
    data class Query(
        @SerialName("allpages")
        val allPages: List<AllPagesEntry>? = null,
        val pages: Map<String, PagesEntry>? = null,
    ) {

        @Serializable
        data class AllPagesEntry(
            @SerialName("pageid")
            val pageId: Int,
            @SerialName("ns")
            val namespace: Int,
            val title: String,
        )

        @Serializable
        data class PagesEntry(
            @SerialName("pageid")
            val pageId: Int,
            @SerialName("ns")
            val namespace: Int,
            val title: String,
            @SerialName("displaytitle")
            val displayTitle: String? = null,
            @SerialName("langlinks")
            val langLinks: List<LangLinksEntry> = emptyList(),
            @SerialName("anoncontributors")
            val anonymousContributors: Int = 0,
            @SerialName("contributors")
            val contributors: List<ContributorsEntry> = emptyList(),
            @SerialName("extract")
            val summary: String? = null,
        ) {

            @Serializable
            data class LangLinksEntry(
                val lang: String,
                @SerialName("*")
                val title: String,
            )

            @Serializable
            data class ContributorsEntry(
                @SerialName("userid")
                val userId: Int,
                val name: String,
            )

        }

    }

}
