package com.example.domain

import com.example.domain.common.WrapperResponse
import com.example.domain.user.GetUserListUseCase
import com.example.domain.user.User
import com.example.domain.user.UserRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class GetUserListUseCaseTest {

    private val repository = mockk<UserRepository>()

    @Test
    fun getUserList() = runTest {

        val testData = UserListTestData()

        coEvery { repository.getUserList(any(), any()) } returns flow {
            emit(WrapperResponse(response = testData.userListData))
        }

        GetUserListUseCase(repository).invoke(0,0).collectLatest {
            Assert.assertEquals(testData.userListData, it.response)
        }
    }


}