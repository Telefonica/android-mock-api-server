package com.telefonica.mocks.common.reponse

import com.telefonica.mocks.common.ApiResult
import java.lang.Exception

interface RepositoryResponse<out ResultType> {

    suspend fun result(): ApiResult<ResultType>

}

abstract class RemoteResponse<ResultType> {

    private val response = object: RepositoryResponse<ResultType> {
        override suspend fun result(): ApiResult<ResultType> = execute()
    }

    fun build(): RepositoryResponse<ResultType> = response

    private suspend fun execute(): ApiResult<ResultType> {
        val value: ApiResult<ResultType> = try {
            ApiResult.Success(requestRemoteCall())
        } catch (e: Exception) {
            ApiResult.Error(e)
        }
        return value
    }

    protected abstract suspend fun requestRemoteCall(): ResultType

}
