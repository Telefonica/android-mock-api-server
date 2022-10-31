package com.telefonica.mocks.domain.user

import com.telefonica.mocks.common.ApiResult
import com.telefonica.mocks.data.user.UserRepository
import com.telefonica.mocks.model.user.UserBo
import javax.inject.Inject

open class GetUserListUseCase @Inject constructor(
    private val repository: UserRepository
) {

    open suspend operator fun invoke(fullList: Boolean): ApiResult<List<UserBo>> = when (fullList) {
        true -> repository.getFullUserList()
        false -> repository.getTinyUserList()
    }
}
