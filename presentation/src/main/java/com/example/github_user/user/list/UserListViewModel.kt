package com.example.github_user.user.list

import androidx.lifecycle.viewModelScope
import collectDefault
import com.example.domain.user.GetUserListUseCase
import com.example.github_user.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import safe
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(private val userListUseCase: GetUserListUseCase) :
    BaseViewModel() {

    companion object {
        const val DATA_PER_PAGE = 20
    }

    private val _userListFlow = MutableStateFlow<List<UIUser>>(emptyList())
    val userListFlow: StateFlow<List<UIUser>> = _userListFlow

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore

    // to replace data from local cache with remote data from API
    private val cachedDataMap = hashMapOf<String, UIUser>()

    private var hasMore = true
    private var since = 0

    fun firstLoad() {
        updateUIState(STATE_LOADING)
        getListUser()
    }

    // after successful fetch data, since will be set to last user id returned
    private fun getListUser() {
        viewModelScope.launch(coroutineExceptionHandler) {
            userListUseCase(DATA_PER_PAGE, since)
                .collectDefault { userList ->
                    since = userList.last().id
                    hasMore = userList.size == DATA_PER_PAGE
                    userList.forEach {
                        val uiUser = it.toUIModel()
                        cachedDataMap[uiUser.login.safe()] = uiUser
                    }
                    _userListFlow.value = cachedDataMap.values.toList().sortedBy { it.id }
                    updateUIState(STATE_NORMAL)
                    _isLoadingMore.value = false
                }
        }
    }

    // load next page if has more data and state is not loading
    fun loadMore() {
        if (!isLoadingMore.value && hasMore && uiStateFlow.value == STATE_NORMAL) {
            _isLoadingMore.value = true
            getListUser()
        }
    }

}