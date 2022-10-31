package com.telefonica.mocks.domain.mock

import com.telefonica.mock.Method
import com.telefonica.mock.Mock
import com.telefonica.mock.MockHelper
import com.telefonica.mocks.model.user.NameDto
import com.telefonica.mocks.model.user.UserDto
import com.telefonica.mocks.model.user.UserWrapperDto
import javax.inject.Inject

open class GetUserMocksUseCase @Inject constructor(
    mockHelper: MockHelper
) {

    operator fun invoke(): List<Mock> = listOf(
        fiveUsersMock,
        userMockError,
        tenUserMock
    )

    private val userMockError = mockHelper.getMockFromFile(
        path = "/?results=5",
        localJsonFile = "user_list_success_1.json",
        method = Method.Get,
        httpResponseCode = 500
    )
    private val fiveUsersMock = mockHelper.getMockFromFile(
        path = "/?results=5",
        localJsonFile = "user_list_success_1.json",
        method = Method.Get,
        delayInMillis = 3000
    )

    private val tenUserMock = mockHelper.getMockFromObject(
        path = "/?results=10",
        method = Method.Get,
        dataObject = UserWrapperDto(
            results = listOf(
                UserDto(
                    name = NameDto(
                        title = "Sr", first = "Pablo", last = "Garcia"
                    ),
                    email = "Pablogarcia@telefonica.com",
                    phone = "611 11 11 11"
                ),
                UserDto(
                    name = NameDto(
                        title = "Sr", first = "David", last = "Santiago"
                    ),
                    email = "Davidsantiago@telefonica.com",
                    phone = "611 11 11 12"
                ),
                UserDto(
                    name = NameDto(
                        title = "Sr", first = "David", last = "Pastor"
                    ),
                    email = "Davidpastor@telefonica.com",
                    phone = "611 11 11 13"
                ),
                UserDto(
                    name = NameDto(
                        title = "Sr", first = "Pablo", last = "Martin"
                    ),
                    email = "Pablomartin@telefonica.com",
                    phone = "611 11 11 14"
                ),
                UserDto(
                    name = NameDto(
                        title = "Sr", first = "Yamal", last = "Al-Mahamid"
                    ),
                    email = "Yamalalmahamid@telefonica.com",
                    phone = "611 11 11 15"
                ),
                UserDto(
                    name = NameDto(
                        title = "Sr", first = "David", last = "Gonzalez"
                    ),
                    email = "Davidgonzalez@telefonica.com",
                    phone = "611 11 11 16"
                ),
                UserDto(
                    name = NameDto(
                        title = "Sr", first = "Jesus", last = "Latorre"
                    ),
                    email = "Jesuslatorre@telefonica.com",
                    phone = "611 11 11 17"
                ),
                UserDto(
                    name = NameDto(
                        title = "Sr", first = "Guillermo", last = "Merino"
                    ),
                    email = "Guillermomerino@telefonica.com",
                    phone = "611 11 11 18"
                ),
                UserDto(
                    name = NameDto(
                        title = "Sr", first = "Manolo", last = "Vera"
                    ),
                    email = "Manolovera@gmail.com",
                    phone = "611 11 11 19"
                ),
                UserDto(
                    name = NameDto(
                        title = "Sr", first = "Javier", last = "Delgado"
                    ),
                    email = "Javierdelgado@gmail.com",
                    phone = "611 11 11 10"
                ),
            ),
        )
    )
}
