package com.example.github_user

import com.example.domain.common.WrapperResponse
import com.example.domain.user.GetUserDetailUseCase
import com.example.github_user.common.BaseViewModel
import com.example.github_user.common.UserDetailTestData
import com.example.github_user.common.setupDispatcher
import com.example.github_user.user.detail.UserDetailViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class UserDetailViewModelTest {

    val usecase = mockk<GetUserDetailUseCase>()
    private val viewModel = UserDetailViewModel(usecase)

    @Test
    fun getUserDetailSuccessTest() = runTest {
        setupDispatcher()

        val testData = UserDetailTestData()

        coEvery { usecase(any()) } returns flow {
            emit(WrapperResponse(response = testData.userData))
        }

        viewModel.getUserDetail("")
        Assert.assertEquals(testData.userData.login, viewModel.userDetailFlow.value.login)
        Assert.assertEquals(BaseViewModel.STATE_NORMAL, viewModel.uiStateFlow.value)
    }

    @Test
    fun getUserDetailFailedTest() = runTest {
        setupDispatcher()
        launch(NonCancellable + viewModel.coroutineExceptionHandler) {
            val exceptionMessage = "failed"
            var catchException: Throwable? = null
            viewModel.errorDataFlow.collectLatest {
                catchException = it
            }
            coEvery { usecase(any()) } returns flow {
                emit(WrapperResponse(exception = Exception(exceptionMessage)))
            }

            viewModel.getUserDetail("")
            advanceUntilIdle()
            Assert.assertEquals(exceptionMessage, catchException?.message)
            Assert.assertEquals(BaseViewModel.STATE_NORMAL, viewModel.uiStateFlow.value)
        }
    }


}