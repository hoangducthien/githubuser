import com.example.domain.common.WrapperResponse
import kotlinx.coroutines.flow.Flow


fun String?.safe(): String {
    return this ?: ""
}

fun Int?.safe(): Int {
    return this ?: 0
}

suspend fun <T> Flow<WrapperResponse<T>>.collectDefault(action: (value: T) -> Unit) {
    collect { item ->
        if (item.isSuccess) {
            action(item.response!!)
        } else {
            throw item.exception!!
        }
    }
}

