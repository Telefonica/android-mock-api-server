package com.telefonica.mocks.domain.backend

import com.telefonica.mock.MockHelper
import com.telefonica.mocks.BuildConfig
import com.telefonica.mocks.common.Environment
import com.telefonica.mocks.data.backend.BackendRepository
import javax.inject.Inject

open class InitBackendUrl @Inject constructor(
    private val mockHelper: MockHelper,
    private val backendRepository: BackendRepository,
) {

    open operator fun invoke() {
        backendRepository.backendUrl = when (BuildConfig.DEFAULT_ENVIRONMENT == Environment.DEMO) {
            true -> mockHelper.getBaseUrl()
            false -> BuildConfig.DEFAULT_ENVIRONMENT.baseUrl
        }
    }

    open fun provideBackendUrl(): String = backendRepository.backendUrl

}
