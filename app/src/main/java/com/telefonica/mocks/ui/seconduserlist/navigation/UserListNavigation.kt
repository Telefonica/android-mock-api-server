package com.telefonica.mocks.ui.seconduserlist.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.telefonica.mocks.ui.navigation.NavArg
import com.telefonica.mocks.ui.navigation.NavDestination
import com.telefonica.mocks.ui.seconduserlist.SecondUserListScreen

object SecondUserListNavigation : NavDestination {

    override val baseRoute: String = "second-user-list"

    override val navArgs: List<NavArg> = emptyList()

}

fun NavGraphBuilder.secondUserListComposable(
    navHostController: NavHostController
) {

    composable(
        SecondUserListNavigation.route(),
        SecondUserListNavigation.args()
    ) {

        SecondUserListScreen(
            viewModel = hiltViewModel(),
        ) {
            navHostController.popBackStack()
        }
    }
}
