package com.telefonica.mocks.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.telefonica.mocks.ui.seconduserlist.navigation.secondUserListComposable
import com.telefonica.mocks.ui.userlist.navigation.userListComposable

fun NavGraphBuilder.mainNavGraph(
    navHostController: NavHostController
) {

    userListComposable(navHostController)

    secondUserListComposable(navHostController)

}
