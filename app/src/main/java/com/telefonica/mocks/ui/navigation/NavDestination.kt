package com.telefonica.mocks.ui.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

const val SEPARATOR = "/"

interface NavDestination {

    val baseRoute: String
    val navArgs: List<NavArg>

    fun route(): String {
        val argsKeys = navArgs.map {
            "{${it.key}}"
        }
        return listOf(baseRoute)
            .plus(argsKeys)
            .joinToString(SEPARATOR)
    }

    fun route(vararg params: Any = arrayOf()): String {
        val paramsValue = params.map { "$it" }
        return listOf(baseRoute)
            .plus(paramsValue)
            .joinToString(SEPARATOR)
    }

    fun args(): List<NamedNavArgument> = navArgs.map {
        navArgument(it.key) {
            type = it.navType
        }
    }
}

class NavArg(val key: String, val navType: NavType<*>)

