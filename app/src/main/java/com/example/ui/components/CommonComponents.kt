package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ChristianGold
import com.example.ui.theme.YatraOneFontFamily
import com.example.ui.viewmodel.AppScreen

@Composable
fun AppBottomNavigation(
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.testTag("app_bottom_nav"),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        val items = listOf(
            Triple(AppScreen.HOME, "गृहपृष्ठ", Icons.Default.Home),
            Triple(AppScreen.BIBLE_READER, "बाइबल", Icons.Default.MenuBook),
            Triple(AppScreen.STUDY_LESSONS, "पाठहरू", Icons.Default.Book),
            Triple(AppScreen.SERMON_BUILDER, "उपदेश", Icons.Default.RecordVoiceOver),
            Triple(AppScreen.APK_DOWNLOAD_CENTER, "APK इन्स्टल", Icons.Default.Download),
            Triple(AppScreen.SETTINGS, "सेटिङ्स", Icons.Default.Settings)
        )

        items.forEach { (screen, label, icon) ->
            val isSelected = when (screen) {
                AppScreen.HOME -> currentScreen == AppScreen.HOME
                AppScreen.BIBLE_READER -> currentScreen == AppScreen.BIBLE_READER || currentScreen == AppScreen.SEARCH
                AppScreen.STUDY_LESSONS -> currentScreen == AppScreen.STUDY_LESSONS || currentScreen == AppScreen.LESSON_DETAIL
                AppScreen.SERMON_BUILDER -> currentScreen == AppScreen.SERMON_BUILDER
                AppScreen.APK_DOWNLOAD_CENTER -> currentScreen == AppScreen.APK_DOWNLOAD_CENTER
                AppScreen.SETTINGS -> currentScreen == AppScreen.SETTINGS || currentScreen == AppScreen.DOWNLOAD_MANAGER || currentScreen == AppScreen.SAVED_ITEMS || currentScreen == AppScreen.AI_TEACHER || currentScreen == AppScreen.DEVOTIONAL
                else -> false
            }

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(screen) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@Composable
fun ChristianHeaderBanner(
    title: String,
    subtitle: String,
    icon: ImageVector = Icons.Default.MenuBook,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        color = MaterialTheme.colorScheme.primaryContainer,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(ChristianGold.copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = ChristianGold,
                    modifier = Modifier.size(30.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontFamily = YatraOneFontFamily
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.88f)
                )
            }
        }
    }
}
