package com.telefonica.mocks.data.user

import com.telefonica.mocks.common.ApiResult
import com.telefonica.mocks.model.user.UserBo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class UserRepository @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource
) {

    open suspend fun getTinyUserList(): ApiResult<List<UserBo>> = remoteDataSource.getTinyUserList()

    open suspend fun getFullUserList(): ApiResult<List<UserBo>> = remoteDataSource.getFullUserList()

}
