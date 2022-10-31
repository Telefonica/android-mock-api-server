package com.telefonica.mocks.model.user

import com.squareup.moshi.JsonClass

data class NameBo(
    val title: String,
    val first: String,
    val last: String
)

@JsonClass(generateAdapter = true)
data class NameDto(
    val title: String,
    val first: String,
    val last: String
)
