package com.example.github_user.user.detail

import com.example.domain.user.UserDetail

class UIUserDetail(
    val login: String? = null,
    val avatar: String? = null,
    val blog: String? = null,
    val location: String? = null,
    val followers: Int = 0,
    val following: Int = 0
)


fun UserDetail.toUIModel(): UIUserDetail {
    return UIUserDetail(login, avatar, blog, location, followers, following)
}