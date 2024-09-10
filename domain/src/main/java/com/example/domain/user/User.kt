package com.example.domain.user

import com.google.gson.annotations.SerializedName

class User(
    @SerializedName("login") val login: String?,
    @SerializedName("avatar_url") val avatar: String?,
    @SerializedName("html_url") val url: String?,
    @SerializedName("id") val id: Int,
)