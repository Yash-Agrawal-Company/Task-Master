package com.yashagrawal.taskmaster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.yashagrawal.taskmaster.presentations.dashboard.UserDashboard
import com.yashagrawal.taskmaster.presentations.navigation.TaskManagerNavController
import com.yashagrawal.taskmaster.presentations.splashscreen.SplashScreen
import com.yashagrawal.taskmaster.ui.theme.TaskMasterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskMasterTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TaskManagerNavController()
                }
            }
        }
    }
}

