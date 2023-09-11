package com.telefonica.mocks

import android.app.Application
import com.telefonica.mock.MockHelper
import com.telefonica.mocks.common.Environment
import com.telefonica.mocks.domain.backend.InitBackendUrl
import com.telefonica.mocks.domain.mock.GetUserMocksUseCase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var mockHelper: MockHelper

    @Inject
    lateinit var initBackendUrl: InitBackendUrl

    @Inject
    lateinit var getUserMocksUseCase: GetUserMocksUseCase

    override fun onCreate() {

        if (BuildConfig.DEFAULT_ENVIRONMENT == Environment.DEMO) {
            super.onCreate()
            mockHelper.setUp(enableSsl = true)
            getUserMocksUseCase()
            CoroutineScope(Dispatchers.IO).launch {
                initBackendUrl()
            }
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        if (BuildConfig.DEFAULT_ENVIRONMENT == Environment.DEMO) {
            mockHelper.stopServer()
        }
    }
}
