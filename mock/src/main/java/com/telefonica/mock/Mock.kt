package com.telefonica.mock

sealed class Method(val value: String) {
    object Post : Method("POST")
    object Get : Method("GET")
    object Delete : Method("DELETE")
    object Put : Method("PUT")
    object Patch : Method("PATCH")
}

data class Mock(
    val path: String,
    val body: String = "{}",
    val method: Method? = null,
    val httpResponseCode: Int = 200,
    val delayInMillis: Long = 1000,
)
