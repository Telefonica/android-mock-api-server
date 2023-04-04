package com.telefonica.mock

import android.os.PatternMatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import java.net.InetAddress
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.HeldCertificate
import java.net.InetAddress
import java.util.concurrent.TimeUnit
import javax.inject.Inject

open class MockApiClient @Inject constructor(
    private val coroutineDispatcher: CoroutineDispatcher,
    private val mockWebServer: MockWebServer,
) {

    private val mockMap: MutableMap<String, List<Mock>> = mutableMapOf()

    private val enqueuedAnswers: MutableList<Mock> = mutableListOf()

    private val dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            val mockFiles: List<Mock> = enqueuedAnswers
                .filter { mock -> request.method == mock.method.value }
                .filter { mock ->
                    PatternMatcher(
                        mock.path,
                        PatternMatcher.PATTERN_SIMPLE_GLOB
                    ).match(request.path)
                }

            return getMockResponse(mockFiles)
        }
    }

    internal suspend fun startServer(inetAddress: InetAddress, port: Int = 0) {
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

    fun enqueue(mock: Mock) {
        enqueuedAnswers.add(mock)
    }

    fun enqueue(mocks: List<Mock>) {
        enqueuedAnswers.addAll(mocks)
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


    private fun getMockResponse(mockFiles: List<Mock>): MockResponse = when (mockFiles.isEmpty()) {
        true -> MockResponse().setResponseCode(NOT_FOUND_ERROR)
        false -> {
            val path = mockFiles.first().path
            val mockMapList = mockMap[path]
            val mock: Mock = when (mockMapList != null && mockMapList.isNotEmpty()) {
                true -> {
                    val mock = mockMapList.first()
                    mockMap[path] = mockMapList.filter { it != mock }
                    mock
                }
                false -> {
                    val mock = mockFiles.first()
                    mockMap[path] = mockFiles.filter { it != mock }
                    mock
                }
            }
            MockResponse()
                .setResponseCode(mock.httpResponseCode)
                .setBodyDelay(mock.delayInMillis, TimeUnit.MILLISECONDS)
                .setBody(mock.body)
        }
    }

    private companion object {
        const val NOT_FOUND_ERROR = 404
    }
}
