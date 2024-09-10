package com.example.github_user.user.list

import com.example.domain.user.User

class UIUser(
    val login: String?,
    val avatar: String?,
    val url: String?,
    val id: Int,

)

fun User.toUIModel(): UIUser {
    return UIUser(login, avatar, url, id)
}