package com.telefonica.mock

import android.content.Context
import android.os.PatternMatcher.PATTERN_SIMPLE_GLOB
import com.telefonica.mock.di.DaggerMockComponent
import com.telefonica.mock.di.MockApiModule
import javax.inject.Inject

class MockHelper(context: Context) {

    @Inject
    lateinit var mockApiClient: MockedServer

    @Inject
    lateinit var fileReader: FileReader

    init {
        DaggerMockComponent
            .builder()
            .mockApiModule(MockApiModule(context))
            .build()
            .inject(this)
    }

    fun enqueue(block: EnqueuingContext.() -> Unit) = block(EnqueuingContext(this))

    fun stopServer() {
        mockApiClient.stopServer()
    }

    suspend fun getBaseUrl(): String = mockApiClient.getBaseUrl()

    fun setUp(
        port: Int = 0,
        enableSsl: Boolean = false,
    ) {
        mockApiClient.setUp(enableSsl = enableSsl)
        mockApiClient.startServer(port)
    }
}

class EnqueuingContext(val mockHelper: MockHelper) {
    fun whenever(
        path: Path,
        method: Method = Method.Get,
        matchingPattern: Int = PATTERN_SIMPLE_GLOB
    ): MockResponseBuilderWithRequestInfo = MockResponseBuilderWithRequestInfo(mockHelper, RequestInfo(path, method, matchingPattern))
}

class MockResponseBuilderWithRequestInfo(
    private val mockHelper: MockHelper,
    private val requestInfo: RequestInfo,
) {

    fun thenReturn(
        body: String = MockedApiResponse.DEFAULT_BODY,
        httpResponseCode: Int = MockedResponse.DEFAULT_MOCK_HTTP_RESPONSE_CODE,
        delayInMillis: Long = MockedResponse.DEFAULT_MOCK_DELAY_IN_MILLIS,
    ) {
        mockHelper.mockApiClient.enqueue(
            requestInfo = requestInfo,
            mockedResponse = MockedApiResponse(
                body = body,
                httpResponseCode = httpResponseCode,
                delayInMillis = delayInMillis,
            ),
        )
    }
    fun thenReturnFromFile(
        pathFromFile: String,
        httpResponseCode: Int = MockedResponse.DEFAULT_MOCK_HTTP_RESPONSE_CODE,
        delayInMillis: Long = MockedResponse.DEFAULT_MOCK_DELAY_IN_MILLIS,
    ) {
        mockHelper.mockApiClient.enqueue(
            requestInfo = requestInfo,
            mockedResponse = MockedApiResponse(
                body = mockHelper.fileReader.readJsonFile(pathFromFile) ?: MockedApiResponse.DEFAULT_BODY,
                httpResponseCode = httpResponseCode,
                delayInMillis = delayInMillis,
            )
        )
    }

    fun thenReturnFromRawFile(
        fileName: String,
        httpResponseCode: Int = MockedResponse.DEFAULT_MOCK_HTTP_RESPONSE_CODE,
        delayInMillis: Long = MockedResponse.DEFAULT_MOCK_DELAY_IN_MILLIS,
    ) {
        mockHelper.mockApiClient.enqueue(
            requestInfo = requestInfo,
            mockedResponse = MockedBufferedResponse(
                buffer = mockHelper.fileReader.readRawFile(fileName),
                httpResponseCode = httpResponseCode,
                delayInMillis = delayInMillis,
            )
        )
    }

}
