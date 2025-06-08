package com.yashagrawal.taskmaster.presentations.dashboard



import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import java.text.SimpleDateFormat
import java.util.*

// Color Theme
val primaryBlue = Color(0xFF1E3A8A)
val lightBlue = Color(0xFF3B82F6)
val backgroundGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF0F172A),
        Color(0xFF1E293B),
        Color(0xFF334155)
    )
)

data class NavigationItem(
    val title: String,
    val emoji: String,
    val icon: ImageVector,
    val description: String,
    val stats: String,
    val color: Brush
)

@Composable
fun UserDashboard(

    navController : NavHostController
) {
    val userName: String = "Yash"
    val currentTime = remember { mutableStateOf(Date()) }

    // Update time every second
    LaunchedEffect(Unit) {
        while (true) {
            currentTime.value = Date()
            kotlinx.coroutines.delay(1000)
        }
    }

    val navigationItems = listOf(
        NavigationItem(
            title = "To-Do Tasks",
            emoji = "✅",
            icon = Icons.Default.CheckCircle,
            description = "Manage your daily tasks",
            stats = "5 pending",
            color = Brush.horizontalGradient(
                colors = listOf(lightBlue, primaryBlue)
            ),

        ),
        NavigationItem(
            title = "Notes",
            emoji = "📝",
            icon = Icons.Default.Add,
            description = "Capture your thoughts",
            stats = "12 notes",
            color = Brush.horizontalGradient(
                colors = listOf(Color(0xFF8B5CF6), Color(0xFF6366F1))
            )
        ),
        NavigationItem(
            title = "Habits",
            emoji = "🔁",
            icon = Icons.Default.Refresh,
            description = "Track your daily habits",
            stats = "3 active",
            color = Brush.horizontalGradient(
                colors = listOf(Color(0xFF10B981), Color(0xFF059669))
            )
        ),
        NavigationItem(
            title = "Pomodoro",
            emoji = "⏱️",
            icon = Icons.Default.DateRange,
            description = "Focus with time blocking",
            stats = "2 sessions today",
            color = Brush.horizontalGradient(
                colors = listOf(Color(0xFFF59E0B), Color(0xFFD97706))
            )
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Section
            item {
                HeaderSection(
                    userName = userName,
                    currentTime = currentTime.value
                )
            }

            // Welcome Message
            item {
                WelcomeCard()
            }

            // Navigation Cards
            items(navigationItems) { item ->
                NavigationCard(
                    item = item,
                    onClick = {
                        when(item){
                            navigationItems[0] -> navController.navigate("TaskUi")
                        }
                    }
                )
            }

            // Quick Stats
            item {
                QuickStatsSection()
            }

            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun HeaderSection(userName: String, currentTime: Date) {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val dateFormat = SimpleDateFormat("EEEE, MMMM dd", Locale.getDefault())

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Welcome back,",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 16.sp
                )
                Text(
                    text = "$userName! 👋",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = timeFormat.format(currentTime),
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = dateFormat.format(currentTime),
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun WelcomeCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🚀",
                fontSize = 32.sp,
                modifier = Modifier.padding(end = 16.dp)
            )
            Column {
                Text(
                    text = "Ready to be productive?",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Choose what you'd like to work on today",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun NavigationCard(
    item: NavigationItem,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(20.dp))
            .clickable {
                isPressed = true
                onClick()
            }
            .animateContentSize()
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.1f)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = primaryBlue.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Emoji and Icon
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            brush = item.color,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.emoji,
                        fontSize = 24.sp
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                // Content
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = item.title,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = item.description,
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        text = item.stats,
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                // Arrow
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Navigate",
                    tint = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(100)
            isPressed = false
        }
    }
}

@Composable
fun QuickStatsSection() {
    Column {
        Text(
            text = "Today's Overview",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickStatCard(
                title = "Completed",
                value = "8",
                subtitle = "tasks",
                color = Color(0xFF10B981),
                modifier = Modifier.weight(1f)
            )
            QuickStatCard(
                title = "Focus Time",
                value = "2.5h",
                subtitle = "today",
                color = Color(0xFFF59E0B),
                modifier = Modifier.weight(1f)
            )
            QuickStatCard(
                title = "Streak",
                value = "12",
                subtitle = "days",
                color = Color(0xFF8B5CF6),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun QuickStatCard(
    title: String,
    value: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = value,
                color = color,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Text(
                text = subtitle,
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 10.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}