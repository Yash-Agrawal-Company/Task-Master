package com.yashagrawal.taskmaster.presentations.splashscreen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.yashagrawal.taskmaster.presentations.navigation.Routes
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController : NavHostController
) {
    // Animation states
    var startAnimation by remember { mutableStateOf(false) }
    var logoVisible by remember { mutableStateOf(false) }
    var textVisible by remember { mutableStateOf(false) }
    var particlesVisible by remember { mutableStateOf(false) }

    // Animation values
    val logoScale by animateFloatAsState(
        targetValue = if (logoVisible) 1f else 0f,
        animationSpec = tween(
            durationMillis = 800,
            easing = FastOutSlowInEasing
        ),
        label = "logo_scale"
    )

    val logoRotation by animateFloatAsState(
        targetValue = if (logoVisible) 0f else 180f,
        animationSpec = tween(
            durationMillis = 800,
            easing = FastOutSlowInEasing
        ),
        label = "logo_rotation"
    )

    val textAlpha by animateFloatAsState(
        targetValue = if (textVisible) 1f else 0f,
        animationSpec = tween(
            durationMillis = 600,
            delayMillis = 400,
            easing = LinearEasing
        ),
        label = "text_alpha"
    )

    val particleAlpha by animateFloatAsState(
        targetValue = if (particlesVisible) 0.7f else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            delayMillis = 600,
            easing = LinearEasing
        ),
        label = "particle_alpha"
    )

    // Infinite rotation for background elements
    val infiniteTransition = rememberInfiniteTransition(label = "infinite_rotation")
    val backgroundRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "background_rotation"
    )

    // Start animations and navigate after delay
    LaunchedEffect(Unit) {
        startAnimation = true
        logoVisible = true
        delay(500)
        textVisible = true
        delay(300)
        particlesVisible = true
        delay(1200) // Total delay: ~2 seconds
        onNavigateToLogin(navController)
    }

    // Theme colors
    val primaryBlue = Color(0xFF1E3A8A)
    val lightBlue = Color(0xFF3B82F6)
    val accentBlue = Color(0xFF60A5FA)
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
        // Animated background particles
        AnimatedBackground(
            rotation = backgroundRotation,
            alpha = particleAlpha,
            primaryColor = lightBlue,
            accentColor = accentBlue
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo Container
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .scale(logoScale)
                    .graphicsLayer { rotationY = logoRotation },
                contentAlignment = Alignment.Center
            ) {
                // Outer ring
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    lightBlue,
                                    primaryBlue
                                )
                            )
                        )
                )

                // Inner circle
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.9f)),
                    contentAlignment = Alignment.Center
                ) {
                    // Task icon (checkmark with lines)
                    Canvas(
                        modifier = Modifier.size(50.dp)
                    ) {
                        val strokeWidth = 4.dp.toPx()

                        // Checkmark
                        drawPath(
                            path = Path().apply {
                                moveTo(size.width * 0.25f, size.height * 0.5f)
                                lineTo(size.width * 0.45f, size.height * 0.7f)
                                lineTo(size.width * 0.75f, size.height * 0.3f)
                            },
                            color = primaryBlue,
                            style = Stroke(
                                width = strokeWidth,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )

                        // Task lines
                        repeat(3) { index ->
                            val y = size.height * (0.2f + index * 0.2f)
                            drawLine(
                                color = lightBlue.copy(alpha = 0.6f),
                                start = Offset(size.width * 0.15f, y),
                                end = Offset(size.width * 0.85f, y),
                                strokeWidth = 2.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // App Name
            AnimatedVisibility(
                visible = textVisible,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(600)
                ) + fadeIn(animationSpec = tween(600))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "TaskMaster",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.alpha(textAlpha)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Master Your Productivity",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = accentBlue,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.alpha(textAlpha)
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Loading indicator
            AnimatedVisibility(
                visible = textVisible,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 600,
                        delayMillis = 800
                    )
                )
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(32.dp),
                    color = accentBlue,
                    strokeWidth = 3.dp
                )
            }
        }
    }
}

@Composable
private fun AnimatedBackground(
    rotation: Float,
    alpha: Float,
    primaryColor: Color,
    accentColor: Color
) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .alpha(alpha)
    ) {
        drawAnimatedParticles(
            rotation = rotation,
            primaryColor = primaryColor,
            accentColor = accentColor
        )
    }
}

private fun DrawScope.drawAnimatedParticles(
    rotation: Float,
    primaryColor: Color,
    accentColor: Color
) {
    val centerX = size.width / 2
    val centerY = size.height / 2

    rotate(rotation, pivot = Offset(centerX, centerY)) {
        // Large circles
        repeat(6) { index ->
            val angle = (index * 60f) * (Math.PI / 180f)
            val radius = size.minDimension / 3
            val x = centerX + (radius * kotlin.math.cos(angle)).toFloat()
            val y = centerY + (radius * kotlin.math.sin(angle)).toFloat()

            drawCircle(
                color = primaryColor.copy(alpha = 0.1f),
                radius = 20.dp.toPx(),
                center = Offset(x, y)
            )
        }
    }

    rotate(-rotation * 0.7f, pivot = Offset(centerX, centerY)) {
        // Small particles
        repeat(12) { index ->
            val angle = (index * 30f) * (Math.PI / 180f)
            val radius = size.minDimension / 4
            val x = centerX + (radius * kotlin.math.cos(angle)).toFloat()
            val y = centerY + (radius * kotlin.math.sin(angle)).toFloat()

            drawCircle(
                color = accentColor.copy(alpha = 0.2f),
                radius = 8.dp.toPx(),
                center = Offset(x, y)
            )
        }
    }
}
fun onNavigateToLogin(navController: NavHostController){
    navController.navigate(Routes.LoginScreen){
        popUpTo(Routes.SplashScreen){ inclusive = true }
    }

}

