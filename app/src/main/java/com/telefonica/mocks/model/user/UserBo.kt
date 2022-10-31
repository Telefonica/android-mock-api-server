package com.telefonica.mocks.model.user

import com.squareup.moshi.JsonClass

data class UserBo(
    val name: NameBo,
    val email: String,
    val phone: String
)

@JsonClass(generateAdapter = true)
data class UserWrapperDto(
    val results: List<UserDto>
)

@JsonClass(generateAdapter = true)
data class UserDto(
    val name: NameDto?,
    val email: String?,
    val phone: String?
)
