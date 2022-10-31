package com.telefonica.mocks.data.user

import com.telefonica.mocks.model.user.UserWrapperDto
import retrofit2.http.GET

interface UserWs {

    @GET("?results=5")
    suspend fun getFiveUsers(): UserWrapperDto

    @GET("?results=10")
    suspend fun getTenUsers(): UserWrapperDto
}
