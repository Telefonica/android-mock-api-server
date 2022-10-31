package com.telefonica.mocks.ui.userlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.telefonica.mocks.common.ApiResult
import com.telefonica.mocks.common.getMessage
import com.telefonica.mocks.domain.user.GetUserListUseCase
import com.telefonica.mocks.model.user.UserBo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UserListState {
    object Idle : UserListState()
    object Loading : UserListState()
    data class Error(val message: String) : UserListState()
    data class Success(val data: List<UserBo>) : UserListState()
}

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val getUserListUseCase: GetUserListUseCase,
) : ViewModel() {

    private val _userListMutableStateFlow: MutableStateFlow<UserListState> =
        MutableStateFlow(UserListState.Idle)
    val userListStateFlow: StateFlow<UserListState> = _userListMutableStateFlow

    fun obtainFullUserList() {
        _userListMutableStateFlow.value = UserListState.Loading
        viewModelScope.launch {
            _userListMutableStateFlow.value = when (val result = getUserListUseCase(true)) {
                is ApiResult.Error -> UserListState.Error(result.getMessage())
                is ApiResult.Success -> UserListState.Success(result.data)
            }
        }
    }

    fun obtainTinyUserList() {
        _userListMutableStateFlow.value = UserListState.Loading
        viewModelScope.launch {
            _userListMutableStateFlow.value = when (val result = getUserListUseCase(false)) {
                is ApiResult.Error -> UserListState.Error(result.getMessage())
                is ApiResult.Success -> UserListState.Success(result.data)
            }
        }
    }

}
