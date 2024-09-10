package com.example.domain.user

import com.google.gson.annotations.SerializedName

class UserDetail(
    @SerializedName("login") val login: String?,
    @SerializedName("avatar_url") val avatar: String?,
    @SerializedName("blog") val blog: String?,
    @SerializedName("location") val location: String?,
    @SerializedName("followers") val followers: Int = 0,
    @SerializedName("following") val following: Int = 0
)