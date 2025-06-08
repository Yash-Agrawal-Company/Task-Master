package com.yashagrawal.taskmaster.presentations.authentication


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.yashagrawal.taskmaster.DataModels.userdatabase.UserDataModel
import com.yashagrawal.taskmaster.presentations.navigation.Routes

// SignUp State data class
data class SignUpState(
    val name: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isAuthenticated: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController : NavHostController
) {
    val viewModel: AuthViewModel = viewModel()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val loginState = remember { mutableStateOf(SignUpState()) }
    fun OnSignUpSuccess(){
        Toast.makeText(context, "SignUp Successful", Toast.LENGTH_SHORT).show()
    }
    val primaryBlue = Color(0xFF1E3A8A)
    val lightBlue = Color(0xFF3B82F6)
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0F172A),
            Color(0xFF1E293B),
            Color(0xFF334155)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.95f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(lightBlue, primaryBlue)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "SignUp",
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    Text(
                        text = "Welcome",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryBlue,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Sign up to your TaskMaster account",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = loginState.value.name,
                        onValueChange = { loginState.value = loginState.value.copy(name = it) },
                        label = { Text("Name") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Name",
                                tint = lightBlue
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = lightBlue,
                            focusedLabelColor = lightBlue,
                            cursorColor = lightBlue
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = loginState.value.password,
                        onValueChange = { loginState.value = loginState.value.copy(password = it) },
                        label = { Text("Password") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Password",
                                tint = lightBlue
                            )
                        },
                        visualTransformation = if (loginState.value.isPasswordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = lightBlue,
                            focusedLabelColor = lightBlue,
                            cursorColor = lightBlue
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = loginState.value.confirmPassword,
                        onValueChange = { loginState.value = loginState.value.copy(confirmPassword = it) },
                        label = { Text("Confirm Password") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Confirm Password",
                                tint = lightBlue
                            )
                        },
                        visualTransformation = if (loginState.value.isPasswordVisible)
                            VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = lightBlue,
                            focusedLabelColor = lightBlue,
                            cursorColor = lightBlue
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                        singleLine = true
                    )

                }

                Button(
                    onClick = {
                        val name = loginState.value.name.trim()
                        val password = loginState.value.password
                        val confirmPassword = loginState.value.confirmPassword

                        if (name.isBlank() || password.isBlank()) {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        if (password != confirmPassword) {
                            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        val newUser = UserDataModel(name = name, password = password)

                        viewModel.insertUser(newUser) {
                            Toast.makeText(context, "SignUp Successful", Toast.LENGTH_SHORT).show()
                            navController.navigate(Routes.UserDashBoard) {
                                popUpTo(Routes.SignUpScreen){inclusive = true}
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = lightBlue,
                        disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
                    )
                ) {
                    Text(
                        text = "SignUp",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Text(
                    text = "Secure login using local input only",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Already have an account ? Login",
                    fontSize = 12.sp,
                    color = primaryBlue,
                    textDecoration = TextDecoration.Underline,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 10.dp).
                    clickable{
                        navController.navigate(Routes.LoginScreen)
                    }
                )
            }
        }
    }

}
