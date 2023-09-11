package com.telefonica.mock

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import java.net.InetAddress
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.HeldCertificate
import javax.inject.Inject

open class MockedServer @Inject constructor(
    private val coroutineDispatcher: CoroutineDispatcher,
    private val mockWebServer: MockWebServer,
    private val responseDispatcher: ResponseDispatcher,
) {

    private val dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse = responseDispatcher.dispatch(
            recordedMethod = request.method,
            recordedPath = request.path
        )
    }

    internal fun startServer(port: Int = 0) {
        mockWebServer.start(port = port)
    }

    fun stopServer() {
        mockWebServer.shutdown()
    }

    suspend fun getBaseUrl(): String = withContext(coroutineDispatcher) {
        mockWebServer.url("/").toString()
    }

    internal fun enqueue(requestInfo: RequestInfo, mockedResponse: MockedResponse) {
        responseDispatcher.enqueue(requestInfo, mockedResponse)
    }

    internal fun setUp(enableSsl: Boolean) {
        mockWebServer.dispatcher = dispatcher
        if (enableSsl) {
            enableHttpsFor(mockWebServer)
        }
    }

    private fun enableHttpsFor(mockWebServer: MockWebServer) {
        val serverCertificates = HandshakeCertificates.Builder()
            .heldCertificate(buildCertificate())
            .build()
        mockWebServer.useHttps(serverCertificates.sslSocketFactory(), false)
    }

    private fun buildCertificate(): HeldCertificate = HeldCertificate.Builder()
        .addSubjectAlternativeName("localhost")
        .build()

    companion object {
        const val RESPONSE_NOT_FOUND_CODE = 404
    }
}
