package com.telefonica.mock

import org.junit.Test

class MockHelperTest {

    lateinit var mockHelper: MockHelper
    @Test
    fun test() {
        mockHelper.enqueue {
            whenever("patata").thenReturn("{ketchup}")
            whenever("potato").thenReturn("{ketchup}")
        }
    }
}