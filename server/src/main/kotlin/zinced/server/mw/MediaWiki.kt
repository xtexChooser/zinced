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
@file:OptIn(FlowPreview::class)

package zinced.server.mw

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.executeAsync
import zinced.common.*
import zinced.server.ZincedServer
import zinced.server.checker.PageChecker
import zinced.server.config.Config
import zinced.server.database.cache.PageCaches
import zinced.server.mw.data.MwContinue
import zinced.server.mw.data.MwParseResponse
import zinced.server.mw.data.MwQueryResponse
import zinced.server.mw.model.*
import zinced.server.util.merge

object MediaWiki {

    val config get() = Config.mediawiki

    val httpClient = OkHttpClient.Builder()
        .build()
    val json = Json { ignoreUnknownKeys = true }
    val lang = config.lang.toLanguage()
    val logger = KotlinLogging.logger {}

    @OptIn(ExperimentalCoroutinesApi::class)
    val apiParallelismLimit = Dispatchers.Default.limitedParallelism(10)

    fun getApiUrl(lang: LanguageID = MediaWiki.lang) = String.format(config.apiUrl, lang.id)

    suspend fun executeApi(lang: LanguageID = MediaWiki.lang, action: String, params: Map<String, String>): String {
        val res = withContext(apiParallelismLimit) {
            httpClient.newCall(
                Request.Builder()
                    .url(getApiUrl(lang))
                    .post(
                        FormBody.Builder()
                            .add("action", action)
                            .add("format", "json")
                            .add("errorformat", "raw")
                            .apply {
                                params.forEach { (k, v) -> add(k, v) }
                            }
                            .build())
                    .header("User-Agent", ZincedServer.userAgent)
                    .build()
            ).executeAsync()
        }

        check(res.isSuccessful) { "Unable to perform MW api call, lang: $lang, action: $action, params: $params, status: ${res.code} ${res.message}" }
        return res.body.string()
    }

    suspend inline fun <reified R> callApi(
        lang: LanguageID = MediaWiki.lang,
        action: String,
        params: Map<String, String>
    ): R {
        val res = executeApi(lang, action, params)
        try {
            return json.decodeFromString(res)
        } catch (e: Exception) {
            throw IllegalStateException(
                "Unable to parse MW api response, " +
                        "lang: $lang, action: $action, params: $params, response: $res", e
            )
        }
    }

    suspend inline fun <reified R : Any> callApiContinued(
        lang: LanguageID = MediaWiki.lang,
        action: String,
        params: Map<String, String>,
        crossinline continueProvider: (R) -> MwContinue?
    ) = flow {
        val cParams = params.toMutableMap()
        while (true) {
            val result = callApi<R>(lang, action, cParams)
            emit(result)
            val `continue` = continueProvider(result)
            if (`continue` != null) {
                cParams.putAll(`continue`)
            } else {
                break
            }
        }
    }

    suspend inline fun <reified R : Any> callApiMerged(
        lang: LanguageID = MediaWiki.lang,
        action: String,
        params: Map<String, String>,
        continueProvider: (R) -> MwContinue?
    ): R {
        var cParams = params
        var result: R? = null
        while (true) {
            val response = callApi<R>(lang, action, cParams)
            val `continue` = continueProvider(response)
            result = result.merge(response)
            if (`continue` != null) {
                cParams = params + `continue`
            } else {
                return result!!
            }
        }
    }

    suspend fun getAllPages(lang: LanguageID = MediaWiki.lang) =
        callApiContinued(
            lang = lang,
            action = "query",
            params = mapOf(
                "list" to "allpages",
                "apnamespace" to "0",
                "aplimit" to "500",
            ),
            continueProvider = MwQueryResponse::`continue`,
        ).flatMapConcat { result ->
            flow {
                result.query.allPages!!.forEach { emit(it.pageId.toPageID() to it.title.toPageName()) }
            }
        }

    suspend fun queryPageMetadata(lang: LanguageID = MediaWiki.lang, id: Collection<PageID>) =
        channelFlow {
            id.chunked(50).forEach { batchPages ->
                launch {
                    callApiMerged(
                        lang = lang,
                        action = "query",
                        params = mapOf(
                            "prop" to "info|langlinks|contributors|articlesnippet",
                            "inprop" to "displaytitle",
                            "pageids" to batchPages.map { it.id }.joinToString(separator = "|")
                        ),
                        continueProvider = MwQueryResponse::`continue`,
                    ).query.pages!!.values
                        .filter { it.missing == null }
                        .map { PageMetadata.from(it) }
                        .forEach { send(it) }
                }
            }
        }

    suspend fun queryPageMetadata(lang: LanguageID = MediaWiki.lang, id: PageID) = callApiMerged(
        lang = lang,
        action = "query",
        params = mapOf(
            "prop" to "info|langlinks|contributors|articlesnippet",
            "inprop" to "displaytitle",
            "pageids" to id.id.toString()
        ),
        continueProvider = MwQueryResponse::`continue`,
    ).query.pages!!.values.single().takeIf { it.missing == null }?.let { PageMetadata.from(it) }

    suspend fun getPageContent(lang: LanguageID = MediaWiki.lang, id: PageID) =
        PageContent.from(
            callApi<MwParseResponse>(
                lang = lang,
                action = "parse",
                params = mapOf(
                    "prop" to "links|externallinks|sections|parsewarnings|wikitext",
                    "pageid" to id.id.toString(),
                ),
            ).parse
        )

    suspend fun getPageID(lang: LanguageID = MediaWiki.lang, titles: Collection<PageName>) = buildMap {
        coroutineScope {
            titles.chunked(50).forEach { batchPages ->
                launch {
                    callApiContinued(
                        lang = lang,
                        action = "query",
                        params = mapOf(
                            "titles" to batchPages.joinToString(separator = "|") { it.name }
                        ),
                        continueProvider = MwQueryResponse::`continue`,
                    ).collect { result ->
                        putAll(result.query.pages!!.values.associate { it.title!!.toPageName() to it.pageID.toPageID() })
                    }
                }
            }
        }
    }

    suspend fun getPageID(lang: LanguageID = MediaWiki.lang, title: PageName) =
        callApi<MwQueryResponse>(
            lang = lang,
            action = "query",
            params = mapOf(
                "titles" to title.name
            ),
        ).query.pages!!.values.single().pageID.toPageID()

    suspend fun getPageName(lang: LanguageID = MediaWiki.lang, pageID: Collection<PageID>) = buildMap {
        coroutineScope {
            pageID.chunked(50).forEach { batchPages ->
                launch {
                    callApiContinued(
                        lang = lang,
                        action = "query",
                        params = mapOf(
                            "pageids" to batchPages.map { it.id }.joinToString(separator = "|")
                        ),
                        continueProvider = MwQueryResponse::`continue`,
                    ).collect { result ->
                        putAll(result.query.pages!!.values.associate { it.pageID.toPageID() to it.title!!.toPageName() })
                    }
                }
            }
        }
    }

    suspend fun getPageName(lang: LanguageID = MediaWiki.lang, id: PageID) =
        callApi<MwQueryResponse>(
            lang = lang,
            action = "query",
            params = mapOf(
                "pageids" to id.id.toString()
            ),
        ).query.pages!!.values.single().title!!.toPageName()

    @OptIn(ExperimentalStdlibApi::class, ExperimentalCoroutinesApi::class)
    suspend fun walk() {
        val dispatcher = CoroutineScope(
            (currentCoroutineContext()[CoroutineDispatcher] ?: Dispatchers.Default)
                .limitedParallelism(15)
        )
        getAllPages().collect { (id, name) ->
            dispatcher.launch {
                logger.info("Populating cache for $id")
                PageCaches.cache(id)
                logger.info("Checking $id")
                PageChecker.check(id)
                logger.info("Checked $id")
            }
        }
    }

}