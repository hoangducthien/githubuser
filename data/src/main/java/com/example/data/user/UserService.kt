package com.example.data.user

import com.example.domain.user.User
import com.example.domain.user.UserDetail
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {
    @GET("users")
    suspend fun  getUserList(@Query("per_page") perPage: Int, @Query("since") since: Int): List<User>

    @GET("users/{userName}")
    suspend fun  getUserDetail(@Path("userName") userName: String): UserDetail
}