package com.yashagrawal.taskmaster.presentations.navigation

import kotlinx.serialization.Serializable

sealed class Routes{
    @Serializable
    data object SplashScreen : Routes()
    @Serializable
    data object LoginScreen : Routes()
    @Serializable
    data object SignUpScreen : Routes()
    @Serializable
    data object UserDashBoard : Routes()
    @Serializable
    data object TaskUi : Routes()
}