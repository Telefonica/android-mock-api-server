package com.telefonica.mocks.ui.seconduserlist

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.telefonica.mocks.ui.common.Error
import com.telefonica.mocks.ui.common.Loading
import com.telefonica.mocks.ui.common.UserList
import com.telefonica.mocks.ui.userlist.UserListState
import com.telefonica.mocks.ui.userlist.UserListViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SecondUserListScreen(
    viewModel: UserListViewModel,
    onBackButtonClicked: () -> Unit
) {

    val userListState by viewModel.userListStateFlow.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.obtainFullUserList()
    }

    Scaffold(
        modifier = Modifier
            .background(Color.LightGray)
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(text = "Segunda Lista")
                },
                navigationIcon = {
                    IconButton(onClick = { onBackButtonClicked() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                backgroundColor = Color.White,
            )
        }
    ) {

        when (userListState) {
            is UserListState.Error -> Error((userListState as UserListState.Error).message)
            UserListState.Idle -> {}
            UserListState.Loading -> Loading()
            is UserListState.Success ->
                UserList(list = (userListState as UserListState.Success).data)
        }
    }

}
