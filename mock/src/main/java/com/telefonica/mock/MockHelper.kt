package com.telefonica.mock

import android.content.Context
import com.telefonica.mock.di.DaggerMockComponent
import com.telefonica.mock.di.MockApiModule
import javax.inject.Inject

class MockHelper(context: Context) {

    @Inject
    lateinit var mockApiClient: MockApiClient

    @Inject
    lateinit var mockProvider: MockProvider

    init {
        DaggerMockComponent
            .builder()
            .mockApiModule(MockApiModule(context))
            .build()
            .inject(this)
    }

    suspend fun startServer() {
        mockApiClient.startServer()
    }

    fun stopServer() {
        mockApiClient.stopServer()
    }

    suspend fun getBaseUrl(): String = mockApiClient.getBaseUrl()

    fun provideDispatcher(mocks: List<Mock>) {
        mockApiClient.provideDispatcher(mocks)
    }

    fun getMockFromFile(
        path: String,
        method: Method? = null,
        httpResponseCode: Int = 200,
        delayInMillis: Long = 1000,
        localJsonFile: String,
    ): Mock = mockProvider.fromFile(
        path = path,
        method = method,
        httpResponseCode = httpResponseCode,
        delayInMillis = delayInMillis,
        localJsonFile = localJsonFile
    )

    fun getMockFromString(
        path: String,
        method: Method? = null,
        httpResponseCode: Int = 200,
        delayInMillis: Long = 1000,
        body: String,
    ): Mock = mockProvider.fromString(
        path = path,
        method = method,
        httpResponseCode = httpResponseCode,
        delayInMillis = delayInMillis,
        body = body
    )

    fun <T> getMockFromObject(
        path: String,
        method: Method? = null,
        httpResponseCode: Int = 200,
        delayInMillis: Long = 1000,
        dataObject: T,
    ): Mock = mockProvider.fromObject(
        path = path,
        method = method,
        httpResponseCode = httpResponseCode,
        delayInMillis = delayInMillis,
        dataObject = dataObject
    )

    fun <T> getMockFromObject(
        path: String,
        method: Method? = null,
        httpResponseCode: Int = 200,
        delayInMillis: Long = 1000,
        list: List<T>,
    ): Mock = mockProvider.fromObject(
        path = path,
        method = method,
        httpResponseCode = httpResponseCode,
        delayInMillis = delayInMillis,
        list = list
    )
}
