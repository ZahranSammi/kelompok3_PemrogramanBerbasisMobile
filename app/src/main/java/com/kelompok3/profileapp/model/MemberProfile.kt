package com.kelompok3.profileapp.model

import androidx.annotation.DrawableRes

data class MemberProfile(
    val id: String,
    val fullName: String,
    val nickName: String,
    val nim: String,
    val role: String,
    val roleCategory: String,
    val isLeader: Boolean = false,
    val major: String,
    val phoneNumber: String,
    val email: String,
    val accentColorHex: Long,
    val borderColorHex: Long,
    @param:DrawableRes val avatarRes: Int
)
