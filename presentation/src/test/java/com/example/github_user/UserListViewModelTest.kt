package com.example.github_user

import com.example.domain.common.WrapperResponse
import com.example.domain.user.GetUserListUseCase
import com.example.domain.user.User
import com.example.github_user.common.BaseViewModel
import com.example.github_user.common.UserListTestData
import com.example.github_user.common.setupDispatcher
import com.example.github_user.user.list.UserListViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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

class UserListViewModelTest {

    val usecase = mockk<GetUserListUseCase>()
    private val viewModel = UserListViewModel(usecase)

    val testData = UserListTestData()

    @Test
    fun testFirstLoad() = runTest {
        setupDispatcher()

        coEvery { usecase(any(), any()) } returns flow {
            emit(WrapperResponse(response = testData.userListData))
        }
        viewModel.firstLoad()
        Assert.assertEquals(BaseViewModel.STATE_NORMAL, viewModel.uiStateFlow.value)
        Assert.assertEquals(20, viewModel.userListFlow.value.size)
        Assert.assertEquals(viewModel.userListFlow.value.first().id, testData.userListData.first().id)
    }

    @Test
    fun testFirstLoadFailed() = runTest {
        setupDispatcher()
        launch(NonCancellable + viewModel.coroutineExceptionHandler) {
            val exceptionMessage = "failed"
            var catchException: Throwable? = null
            viewModel.errorDataFlow.collectLatest {
                catchException = it
            }
            coEvery { usecase(any(), any()) } returns flow {
                emit(WrapperResponse(exception = Exception(exceptionMessage)))
            }

            viewModel.firstLoad()
            advanceUntilIdle()
            Assert.assertEquals(exceptionMessage, catchException?.message)
            Assert.assertEquals(BaseViewModel.STATE_NORMAL, viewModel.uiStateFlow.value)
        }
    }

    @Test
    fun testLoadMore() = runTest {
        setupDispatcher()

        coEvery { usecase(any(), any()) } returns flow {
            emit(WrapperResponse(response = testData.userListData))
        }

        viewModel.loadMore()

        Assert.assertEquals(BaseViewModel.STATE_NORMAL, viewModel.uiStateFlow.value)
        Assert.assertEquals(20, viewModel.userListFlow.value.size)
        Assert.assertEquals(viewModel.userListFlow.value.last().id, testData.userListData.last().id)
    }

    @Test
    fun testLoadMoreFailed() = runTest {
        setupDispatcher()
        launch(NonCancellable + viewModel.coroutineExceptionHandler) {
            val exceptionMessage = "failed"
            var catchException: Throwable? = null
            viewModel.errorDataFlow.collectLatest {
                catchException = it
            }
            coEvery { usecase(any(), any()) } returns flow {
                emit(WrapperResponse(exception = Exception(exceptionMessage)))
            }

            viewModel.loadMore()
            advanceUntilIdle()
            Assert.assertEquals(exceptionMessage, catchException?.message)
            Assert.assertEquals(BaseViewModel.STATE_NORMAL, viewModel.uiStateFlow.value)
        }
    }
}