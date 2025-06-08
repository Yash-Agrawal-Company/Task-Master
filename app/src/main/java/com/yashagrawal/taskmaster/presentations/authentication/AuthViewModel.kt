package com.yashagrawal.taskmaster.presentations.authentication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yashagrawal.taskmaster.DataModels.tasksdatabase.TaskDatabase
import com.yashagrawal.taskmaster.DataModels.userdatabase.UserDataModel
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao = TaskDatabase.getDatabase(application).userDao()

    fun insertUser(user: UserDataModel, onResult: () -> Unit) {
        viewModelScope.launch {
            userDao.insertUser(user)
            onResult()
        }
    }
}
