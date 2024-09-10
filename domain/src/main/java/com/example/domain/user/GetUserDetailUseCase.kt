package com.example.domain.user

import com.example.domain.common.WrapperResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserDetailUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend operator fun invoke(userName: String): Flow<WrapperResponse<UserDetail>> {
        return userRepository.getUserDetail(userName)
    }
}