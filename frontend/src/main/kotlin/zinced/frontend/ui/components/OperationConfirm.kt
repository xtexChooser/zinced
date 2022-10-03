package zinced.frontend.ui.components

import androidx.compose.runtime.*
import kotlinx.browser.window
import org.jetbrains.compose.web.dom.*
import zinced.frontend.ui.components.elements.*
import zinced.frontend.ui.style.Styles

@Composable
fun OperationConfirm(
    warnText: String,
    warning: @Composable () -> Unit = {
        Span(attrs = { classes(Styles.textBold) }) { Text(warnText) }
    },
    cancel: () -> Unit = { window.history.back() },
    confirm: () -> Unit,
) {
    Div(attrs = { classes(Styles.center) }) {
        warning()
        Div {
            Button("取消") {
                onClick {
                    cancel()
                }
            }
            Button("确认") {
                onClick {
                    confirm()
                }
            }
        }
    }
}

@Composable
fun DangerOperationConfirm(
    warnText: String,
    operate: suspend () -> Unit,
) {
    var confirmed by remember { mutableStateOf(false) }
    if (!confirmed) {
        OperationConfirm(warnText) {
            confirmed = true
        }
    } else {
        var done by remember { mutableStateOf<Result<Unit>?>(null) }
        val key by remember { derivedStateOf { Any() } }
        LaunchedEffect(key) {
            done = runCatching {
                operate()
            }
        }
        if (done != null) {
            Div(attrs = { classes(Styles.center) }) {
                if (done!!.isSuccess) {
                    P("操作完成")
                } else {
                    P(attrs = { classes(Styles.textBold) }) { Text("错误") }
                    Pre(done!!.exceptionOrNull()!!.toString())
                }
                Button("返回") {
                    onClick {
                        window.history.back()
                    }
                }
            }
        } else {
            Div(attrs = { classes(Styles.center) }) {
                P("操作中")
            }
        }
    }
}

@Composable
fun BooleanOperationConfirm(
    warnText: String,
    operate: suspend () -> Boolean,
) = DangerOperationConfirm(warnText) {
    if (!operate()) error("Operation failed")
}
