package com.yashagrawal.taskmaster.presentations.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yashagrawal.taskmaster.presentations.TasksUi.TaskUi
import com.yashagrawal.taskmaster.presentations.authentication.LoginScreen
import com.yashagrawal.taskmaster.presentations.authentication.SignUpScreen
import com.yashagrawal.taskmaster.presentations.dashboard.UserDashboard
import com.yashagrawal.taskmaster.presentations.splashscreen.SplashScreen

@Composable
fun TaskManagerNavController() {
    val navController = rememberNavController()
    NavHost(startDestination = Routes.SplashScreen,navController = navController){
       composable <Routes.SplashScreen>{ SplashScreen(navController) }
       composable <Routes.LoginScreen>{ LoginScreen(navController) }
       composable <Routes.SignUpScreen>{ UserDashboard(navController) }
       composable <Routes.TaskUi>{ TaskUi(navController) }
    }
}