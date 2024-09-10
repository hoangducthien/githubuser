package com.example.data.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.user.User

@Entity
class UserEntity (
    @PrimaryKey val login: String,
    val avatar: String?,
    val url: String?,
    val id: Int,
)

fun UserEntity.toDomainModel(): User {
    return User(login, avatar, url, id)
}

fun User.toDBModel(): UserEntity {
    return UserEntity(login ?: "", avatar, url, id)
}