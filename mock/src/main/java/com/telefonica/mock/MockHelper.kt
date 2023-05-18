package com.telefonica.mock

import android.content.Context
import com.telefonica.mock.di.DaggerMockComponent
import com.telefonica.mock.di.MockApiModule
import java.net.InetAddress
import javax.inject.Inject

class MockHelper(context: Context) {

    @Inject
    lateinit var mockApiClient: MockApiClient

    @Inject
    lateinit var fileReader: FileReader

    init {
        DaggerMockComponent
            .builder()
            .mockApiModule(MockApiModule(context))
            .build()
            .inject(this)
    }

    fun stopServer() {
        mockApiClient.stopServer()
    }

    suspend fun getBaseUrl(): String = mockApiClient.getBaseUrl()

    suspend fun setUp(
        inetAddress: InetAddress = InetAddress.getByName(DEFAULT_HOSTNAME),
        port: Int = 0,
        enableSsl: Boolean = false,
    ) {
        mockApiClient.setUp(
            address = inetAddress,
            enableSsl = enableSsl,
        )
        mockApiClient.startServer(inetAddress, port)
    }

    companion object {
        const val DEFAULT_HOSTNAME = "localhost"
    }
}

fun whenever(mockHelper: MockHelper): MockResponseBuilder = MockResponseBuilder(mockHelper)

class MockResponseBuilder(private val mockHelper: MockHelper) {

    fun on(
        path: Path,
        method: Method = Method.Get,
    ): MockResponseBuilderWithRequestInfo =
        MockResponseBuilderWithRequestInfo(mockHelper, RequestInfo(path, method))

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
            mock = MockedApiResponse(
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
            mock = MockedApiResponse(
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
            mock = MockedBufferedResponse(
                buffer = mockHelper.fileReader.readRawFile(fileName),
                httpResponseCode = httpResponseCode,
                delayInMillis = delayInMillis,
            )
        )
    }

}
