package com.example.domain.user

import com.example.domain.common.WrapperResponse
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun  getUserList(perPage: Int, since: Int): Flow<WrapperResponse<List<User>>>

    suspend fun  getUserDetail(userName: String): Flow<WrapperResponse<UserDetail>>

}