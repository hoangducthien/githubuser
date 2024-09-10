package com.example.domain.user

import com.example.domain.common.WrapperResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserListUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend operator fun invoke(perPage: Int, since: Int): Flow<WrapperResponse<List<User>>> {
        return userRepository.getUserList(perPage, since)
    }
}