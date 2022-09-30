package zinced.frontend.ui.page

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.DDescription
import org.jetbrains.compose.web.dom.DList
import org.jetbrains.compose.web.dom.Div
import zinced.client.endpoint.getServerInfo
import zinced.frontend.ApiClient
import zinced.frontend.ui.components.LoadingData
import zinced.frontend.ui.components.elements.*

@Composable
fun AboutPage() {
    LoadingData(loader = { ApiClient.getServerInfo() }) { info ->
        DList {
            Div {
                DTerm("前端实现")
                DDescription("Zinced Builtin Web-Frontend")
            }
            Div {
                DTerm("服务端实现")
                DDescription(info.software)
            }
            Div {
                DTerm("服务端版本")
                DDescription(info.version)
            }
            Div {
                DTerm("服务端出口用户代理")
                DDescription { Code(info.userAgent) }
            }
        }
        Details {
            Summary("MediaWiki")
            DList {
                Div {
                    DTerm("API端点URL格式")
                    DDescription { Code(info.mediaWikiApiUrl) }
                }
                Div {
                    DTerm("工作维基语言")
                    DDescription(info.mediaWikiLanguage)
                }
                Div {
                    DTerm("条目浏览URL格式")
                    DDescription { Code(info.mediaWikiViewUrl) }
                }
            }
        }
    }
}
