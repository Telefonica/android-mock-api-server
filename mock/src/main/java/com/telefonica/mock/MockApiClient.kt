package com.telefonica.mock

import android.os.PatternMatcher
import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import okhttp3.mockwebserver.SocketPolicy
import java.net.InetAddress
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.HeldCertificate
import java.util.concurrent.TimeUnit
import javax.inject.Inject

open class MockApiClient @Inject constructor(
    private val coroutineDispatcher: CoroutineDispatcher,
    private val mockWebServer: MockWebServer,
) {

    private val mockMap: MutableMap<String, List<MockedResponse>> = mutableMapOf()

    private val enqueuedAnswers: MutableList<RequestAndResponse> = mutableListOf()

    private val dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            val mockFiles: List<RequestAndResponse> = enqueuedAnswers
                .filter { requestAndResponseInfo -> request.method == requestAndResponseInfo.requestInfo.method.value }
                .filter { requestAndResponseInfo ->
                    PatternMatcher(
                        requestAndResponseInfo.requestInfo.path,
                        PatternMatcher.PATTERN_SIMPLE_GLOB
                    ).match(request.path)
                }

            return getMockResponse(mockFiles)
        }
    }

    internal suspend fun startServer(inetAddress: InetAddress, port: Int = 8080) {
        withContext(coroutineDispatcher) {
            runCatching {
                mockWebServer.start(inetAddress = inetAddress, port = port)
            }
        }
    }

    fun stopServer() {
        mockWebServer.shutdown()
    }

    suspend fun getBaseUrl(): String = withContext(coroutineDispatcher) {
        mockWebServer.url("/").toString()
    }

    internal fun enqueue(requestInfo: RequestInfo, mock: MockedResponse) {
        enqueuedAnswers.add(RequestAndResponse(requestInfo, mock))
    }

    internal fun setUp(address: InetAddress, enableSsl: Boolean) {
        mockWebServer.dispatcher = dispatcher
        if (enableSsl) {
            enableHttpsFor(mockWebServer, address)
        }
    }

    private fun enableHttpsFor(mockWebServer: MockWebServer, address: InetAddress) {
        val serverCertificates = HandshakeCertificates.Builder()
            .heldCertificate(buildCertificate(address.canonicalHostName))
            .build()
        mockWebServer.useHttps(serverCertificates.sslSocketFactory(), false)
    }

    private fun buildCertificate(altName: String): HeldCertificate = HeldCertificate.Builder()
        .addSubjectAlternativeName(altName)
        .build()


    private fun getMockResponse(mockFiles: List<RequestAndResponse>): MockResponse = when (mockFiles.isEmpty()) {
        true -> MockResponse().setResponseCode(NOT_FOUND_ERROR)
        false -> {
            val path = mockFiles.first().requestInfo.path
            val mockMapList = mockMap[path]
            val mock: MockedResponse = when (mockMapList != null && mockMapList.isNotEmpty()) {
                true -> {
                    val mockedResponse: MockedResponse = mockMapList.first()
                    mockMap[path] = mockMapList.filter { it != mockedResponse }
                    mockedResponse
                }
                false -> {
                    val requestAndResponse = mockFiles.first()
                    mockMap[path] = mockFiles.filter { it != requestAndResponse }.map { it.mockedResponse }
                    requestAndResponse.mockedResponse
                }
            }
            when (mock) {
                is MockedApiResponse -> MockResponse()
                    .setResponseCode(mock.httpResponseCode)
                    .setBodyDelay(mock.delayInMillis, TimeUnit.MILLISECONDS)
                    .setBody(mock.body)
                is MockedBufferedResponse -> MockResponse()
                    .setResponseCode(mock.httpResponseCode)
                    .setBodyDelay(mock.delayInMillis, TimeUnit.MILLISECONDS)
                    .setBody(mock.buffer)
                    .setSocketPolicy(SocketPolicy.DISCONNECT_AT_END)
            }

        }
    }

    private companion object {
        const val NOT_FOUND_ERROR = 404
    }
}