package com.example.data.user

import com.example.data.common.AppDataBase
import com.example.domain.common.WrapperResponse
import com.example.domain.common.executeWrapperAPICall
import com.example.domain.user.User
import com.example.domain.user.UserDetail
import com.example.domain.user.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart

class UserRepositoryImpl(
    private val userService: UserService,
    private val db: AppDataBase,
    private val ioDispatcher: CoroutineDispatcher
) : UserRepository {


    // 1. Get data from DB, if not empty, return cached data
    // 2. Fetch data from API, if success, save data to db and return latest data again
    override suspend fun getUserList(perPage: Int, since: Int): Flow<WrapperResponse<List<User>>> =
        channelFlow {
            send(executeWrapperAPICall {
                userService.getUserList(perPage, since).let { remoteUserList ->
                    val dbUsers = remoteUserList.map {
                        it.toDBModel()
                    }
                    db.userDao().insertAll(dbUsers)
                    remoteUserList
                }
            })
        }.onStart {
            db.userDao().getUsers(perPage, since).let { dbUserList ->
                if (dbUserList.isNotEmpty()) {
                    emit(WrapperResponse(response = dbUserList.map {
                        it.toDomainModel()
                    }))
                }
            }
        }.flowOn(ioDispatcher)

    // 1. Get data from DB, return cached data
    // 2. Fetch data from API, if success, save data to db and return latest data again
    override suspend fun getUserDetail(userName: String): Flow<WrapperResponse<UserDetail>> {
        return channelFlow {
            send(executeWrapperAPICall {
                val remoteResponse = userService.getUserDetail(userName)
                db.userDetailDao().insertUserDetail(remoteResponse.toDBModel())
                remoteResponse
            })
        }.onStart {
            db.userDetailDao().getUserDetail(userName)?.let {
                emit(WrapperResponse(response = it.toDomainModel()))
            }
        }.flowOn(ioDispatcher)
    }
}