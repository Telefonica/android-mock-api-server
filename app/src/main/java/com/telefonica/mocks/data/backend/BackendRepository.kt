package com.telefonica.mocks.data.backend

import com.telefonica.mocks.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class BackendRepository @Inject constructor() {

    var backendUrl = BuildConfig.DEFAULT_ENVIRONMENT.baseUrl

}
