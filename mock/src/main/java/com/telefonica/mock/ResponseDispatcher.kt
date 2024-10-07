package com.telefonica.mock

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.SocketPolicy
import java.util.*
import java.util.concurrent.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResponseDispatcher @Inject constructor() {

    private val responses: MutableMap<RequestInfo, LinkedList<MockedResponse>> = mutableMapOf()

    fun dispatch(recordedMethod: String?, recordedPath: String?): MockResponse {
        val responseList = responses
            .filterKeys { requestInfo ->
                requestInfo.method.value == recordedMethod && !recordedPath.isNullOrEmpty() && Regex(requestInfo.path).matches(recordedPath)
            }
            .entries
            .firstOrNull()
            ?.value

        return when {
            responseList.isNullOrEmpty() -> MockResponse().setResponseCode(MockedServer.RESPONSE_NOT_FOUND_CODE)
            else -> {
                val response = responseList.poll() ?: return MockResponse().setResponseCode(MockedServer.RESPONSE_NOT_FOUND_CODE)
                responseList.add(response)
                when (response) {
                    is MockedApiResponse -> MockResponse()
                        .setResponseCode(response.httpResponseCode)
                        .setBodyDelay(response.delayInMillis, TimeUnit.MILLISECONDS)
                        .setBody(response.body)
                    is MockedBufferedResponse -> MockResponse()
                        .setResponseCode(response.httpResponseCode)
                        .setBodyDelay(response.delayInMillis, TimeUnit.MILLISECONDS)
                        .setBody(response.buffer)
                        .setSocketPolicy(SocketPolicy.DISCONNECT_AT_END)
                }
            }
        }
    }

    internal fun enqueue(requestInfo: RequestInfo, mockedResponse: MockedResponse) {
        val mockListUpdated = (responses[requestInfo] ?: LinkedList()).apply { add(mockedResponse) }
        responses[requestInfo] = mockListUpdated
    }
}
