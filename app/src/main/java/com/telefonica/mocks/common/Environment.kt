package com.telefonica.mocks.common

enum class Environment(
    val baseUrl: String
) {
    DEMO(""),
    RELEASE("https://api.randomuser.me")
}
