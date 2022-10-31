package com.telefonica.mocks.ui.userlist.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.telefonica.mocks.ui.navigation.NavArg
import com.telefonica.mocks.ui.navigation.NavDestination
import com.telefonica.mocks.ui.seconduserlist.navigation.SecondUserListNavigation
import com.telefonica.mocks.ui.userlist.UserListScreen

object UserListNavigation : NavDestination {

    override val baseRoute: String = "user-list"

    override val navArgs: List<NavArg> = emptyList()

}

fun NavGraphBuilder.userListComposable(
    navHostController: NavHostController
) {

    composable(
        UserListNavigation.route(),
        UserListNavigation.args()
    ) {

        UserListScreen(
            viewModel = hiltViewModel()
        ) { navHostController.navigate(SecondUserListNavigation.route()) }
    }
}
