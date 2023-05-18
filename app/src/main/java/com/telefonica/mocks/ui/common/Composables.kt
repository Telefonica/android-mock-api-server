package com.telefonica.mocks.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.telefonica.mocks.model.user.UserBo

@Composable
fun Loading() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LinearProgressIndicator()
            Text(text = "Cargando...")
        }
    }
}

@Composable
fun Error(message: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Rounded.Warning,
                contentDescription = null,
                tint = Color.Yellow
            )
            Text(
                text = message.takeIf { it.isNotEmpty() }
                    ?: "Ha ocurrido un error, prueba mas tarde"
            )
        }
    }
}

@Composable
fun UserList(list: List<UserBo>) {
    LazyColumn {
        items(
            items = list
        ) { user ->
            UserRow(user = user)
        }
    }
}

@Composable
fun UserRow(user: UserBo) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {

            AsyncImage(
                model = "https://localhost:8080/image.png",
                contentDescription = null,
            )
            Text(text = "${user.name.first} ${user.name.last}")
            Text(text = user.email)
            Text(text = user.phone)
        }
    }
}
