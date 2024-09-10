package com.example.data.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.user.User
import com.example.domain.user.UserDetail

@Entity
class UserDetailEntity (
    @PrimaryKey val login: String,
    val avatar: String? = null,
    val blog: String? = null,
    val location: String? = null,
    val followers: Int = 0,
    val following: Int = 0
)

fun UserDetailEntity.toDomainModel(): UserDetail {
    return UserDetail(login, avatar, blog, location, followers, following)
}

fun UserDetail.toDBModel(): UserDetailEntity {
    return UserDetailEntity(login ?: "", avatar, blog, location, followers, following)
}