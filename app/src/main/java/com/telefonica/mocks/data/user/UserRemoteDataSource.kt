package com.telefonica.mocks.data.user

import com.telefonica.mocks.common.ApiResult
import com.telefonica.mocks.common.reponse.RemoteResponse
import com.telefonica.mocks.di.IoDispatcher
import com.telefonica.mocks.model.user.UserBo
import com.telefonica.mocks.model.user.toBo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject


open class UserRemoteDataSource @Inject constructor(
    private val webService: UserWs,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    suspend fun getTinyUserList(): ApiResult<List<UserBo>> = withContext(dispatcher) {
        object : RemoteResponse<List<UserBo>>() {
            override suspend fun requestRemoteCall(): List<UserBo> {
                return webService.getFiveUsers().results.map { it.toBo() }
            }
        }.build().result()
    }

    suspend fun getFullUserList(): ApiResult<List<UserBo>> = withContext(dispatcher) {
        object : RemoteResponse<List<UserBo>>() {
            override suspend fun requestRemoteCall(): List<UserBo> =
                webService.getTenUsers().results.map { it.toBo() }
        }.build().result()
    }
}

