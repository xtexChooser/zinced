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
package zinced.common

import kotlinx.serialization.Serializable

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
)
