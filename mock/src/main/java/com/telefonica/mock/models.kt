package com.telefonica.mock

import android.os.PatternMatcher
import okio.Buffer

sealed class Method(val value: String) {
    object Post : Method("POST")
    object Get : Method("GET")
    object Delete : Method("DELETE")
    object Put : Method("PUT")
    object Patch : Method("PATCH")
}

sealed class MockedResponse(
    val httpResponseCode: Int = DEFAULT_MOCK_HTTP_RESPONSE_CODE,
    val delayInMillis: Long = DEFAULT_MOCK_DELAY_IN_MILLIS,
) {
    companion object {
        const val DEFAULT_MOCK_DELAY_IN_MILLIS = 0L
        const val DEFAULT_MOCK_HTTP_RESPONSE_CODE = 200
    }
}

class MockedApiResponse(
    val body: String = DEFAULT_BODY,
    httpResponseCode: Int = DEFAULT_MOCK_HTTP_RESPONSE_CODE,
    delayInMillis: Long = DEFAULT_MOCK_DELAY_IN_MILLIS,
) : MockedResponse(
    httpResponseCode,
    delayInMillis,
) {
    companion object {
        const val DEFAULT_BODY = "{}"
    }
}

class MockedBufferedResponse(
    val buffer: Buffer,
    httpResponseCode: Int = DEFAULT_MOCK_HTTP_RESPONSE_CODE,
    delayInMillis: Long = DEFAULT_MOCK_DELAY_IN_MILLIS,
) : MockedResponse(
    httpResponseCode,
    delayInMillis,
)

internal class RequestAndResponse(val requestInfo: RequestInfo, val mockedResponse: MockedResponse)

data class RequestInfo(val path: Path, val method: Method, val matchingPattern: Int)
typealias Path=String

internal class RequestInfoWithPattern(val pathPattern: PatternMatcher, val method: Method)