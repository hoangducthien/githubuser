package com.example.data

import com.example.data.common.AppDataBase
import com.example.data.user.UserDetailEntity
import com.example.data.user.UserRepositoryImpl
import com.example.data.user.UserService
import com.example.data.user.toDBModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class UserRepositoryTest {

    private val service = mockk<UserService>()
    private val database = mockk<AppDataBase>(relaxed = true)
    private val repository = UserRepositoryImpl(
        service,
        database,
        UnconfinedTestDispatcher()
    )

    @Test
    fun getUserListTest() = runTest {
        val testData = UserListTestData()
        coEvery { service.getUserList(any(), any()) } returns testData.userListData
        repository.getUserList(0, 0).collectLatest {
            Assert.assertEquals(testData.userListData, it.response)
        }
    }

    @Test
    fun getUserDetailTest() = runTest {
        val testData = UserDetailTestData()
        coEvery { service.getUserDetail(any()) } returns testData.userData
        coEvery { database.userDetailDao().getUserDetail(any()) } returns testData.userData.toDBModel()
        repository.getUserDetail("").collectLatest {
            Assert.assertEquals(testData.userData.login, it.response?.login)
        }
    }

}