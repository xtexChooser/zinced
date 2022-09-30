package zinced.frontend.ui.components

import androidx.compose.runtime.*
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import zinced.frontend.ui.style.Styles

private val KEY_LOADED_DATA = Any()
private val KEY_DATA_LOADER = Any()

@Composable
fun <T> LoadingData(
    loader: suspend () -> T,
    key: Any? = null,
    loading: @Composable () -> Unit = {
        Span(attrs = { classes(Styles.textBold) }) { Text("加载中...") }
    },
    content: @Composable (T) -> Unit
) {
    var data by remember(KEY_LOADED_DATA, key) { mutableStateOf<T?>(null) }
    if (data == null) {
        LaunchedEffect(KEY_DATA_LOADER, key) {
            data = loader()
        }
        loading()
    } else {
        content(data!!)
    }
}