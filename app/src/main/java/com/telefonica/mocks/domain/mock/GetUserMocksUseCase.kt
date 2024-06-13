package com.telefonica.mocks.domain.mock

import com.telefonica.mock.MockHelper
import javax.inject.Inject

open class GetUserMocksUseCase @Inject constructor(
    private val mockHelper: MockHelper
) {

    operator fun invoke() {
        mockHelper.enqueue {
            whenever(".*/?results=5").thenReturnFromFile("user_list_success_1.json")
            whenever(".*/?results=10").thenReturnFromFile("user_list_success_2.json")
        }
    }

}
