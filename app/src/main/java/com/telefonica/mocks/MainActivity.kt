package com.telefonica.mocks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.telefonica.mocks.ui.navigation.mainNavGraph
import com.telefonica.mocks.ui.theme.MocksTheme
import com.telefonica.mocks.ui.userlist.navigation.UserListNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MocksTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    InitialScreen()
                }
            }
        }
    }

}

@Composable
fun InitialScreen() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = UserListNavigation.route()
    ) {
        mainNavGraph(navController)
    }

}
