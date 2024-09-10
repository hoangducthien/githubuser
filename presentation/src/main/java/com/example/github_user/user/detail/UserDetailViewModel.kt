package com.example.github_user.user.detail

import androidx.lifecycle.viewModelScope
import com.example.domain.user.GetUserDetailUseCase
import com.example.github_user.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import collectDefault

@HiltViewModel
class UserDetailViewModel @Inject constructor(private val getUserDetailUseCase: GetUserDetailUseCase): BaseViewModel() {
    private val _userDetailFlow = MutableStateFlow(UIUserDetail())
    val userDetailFlow: StateFlow<UIUserDetail> = _userDetailFlow

    fun getUserDetail(userName: String) {
        updateUIState(STATE_LOADING)
        viewModelScope.launch(coroutineExceptionHandler) {
            getUserDetailUseCase(userName).collectDefault { userDetail ->
                _userDetailFlow.value = userDetail.toUIModel()
            }
            updateUIState(STATE_NORMAL)
        }
    }

}