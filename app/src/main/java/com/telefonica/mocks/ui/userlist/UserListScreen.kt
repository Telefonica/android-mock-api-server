package com.telefonica.mocks.ui.userlist

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.telefonica.mocks.ui.common.Error
import com.telefonica.mocks.ui.common.Loading
import com.telefonica.mocks.ui.common.UserList

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun UserListScreen(
    viewModel: UserListViewModel,
    onNextPageClicked: () -> Unit
) {

    val userListState by viewModel.userListStateFlow.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.obtainTinyUserList()
    }
    Scaffold(
        modifier = Modifier
            .background(Color.LightGray)
            .fillMaxSize(),
        floatingActionButton = {

            Row {

                FloatingActionButton(
                    modifier = Modifier.padding(start = 8.dp),
                    onClick = { viewModel.obtainTinyUserList() },
                    contentColor = Color.White,
                    backgroundColor = MaterialTheme.colors.primary
                ) {
                    Icon(imageVector = Icons.Filled.Refresh, contentDescription = null)
                }

                FloatingActionButton(
                    modifier = Modifier.padding(start = 8.dp),
                    onClick = { onNextPageClicked() },
                    contentColor = Color.White,
                    backgroundColor = MaterialTheme.colors.primaryVariant
                ) {
                    Icon(imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = null)
                }
            }
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
