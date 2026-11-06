package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AppBottomNavigation
import com.example.ui.screens.AiTeacherScreen
import com.example.ui.screens.ApkDownloadCenterScreen
import com.example.ui.screens.BibleReaderScreen
import com.example.ui.screens.BibleSearchScreen
import com.example.ui.screens.DevotionalScreen
import com.example.ui.screens.DownloadManagerScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LessonDetailScreen
import com.example.ui.screens.SavedItemsScreen
import com.example.ui.screens.SermonBuilderScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.StudyLessonsScreen
import com.example.ui.theme.ChristianGold
import com.example.ui.theme.NepaliBibleTheme
import com.example.ui.theme.YatraOneFontFamily
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.BibleViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val bibleViewModel: BibleViewModel = viewModel()
            val isDarkMode by bibleViewModel.isDarkMode.collectAsState()

            NepaliBibleTheme(darkTheme = isDarkMode) {
                MainAppContent(bibleViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContent(viewModel: BibleViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val isEnglish by viewModel.isEnglishMode.collectAsState()

    // Handle back button on sub-screens
    BackHandler(enabled = currentScreen != AppScreen.HOME) {
        viewModel.handleBack()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (currentScreen != AppScreen.SEARCH) {
                TopAppBar(
                    title = {
                        Text(
                            text = if (isEnglish) "Nepali Bible Study" else "नेपाली बाइबल अध्ययन",
                            fontFamily = YatraOneFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 20.sp,
                            color = Color.White
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = { viewModel.navigateTo(AppScreen.SEARCH) },
                            modifier = Modifier.testTag("topbar_search_action")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.White
                            )
                        }

                        IconButton(
                            onClick = { viewModel.navigateTo(AppScreen.SAVED_ITEMS) },
                            modifier = Modifier.testTag("topbar_saved_action")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bookmark,
                                contentDescription = "Saved Items",
                                tint = ChristianGold
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = Color.White
                    ),
                    modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
                )
            }
        },
        bottomBar = {
            AppBottomNavigation(
                currentScreen = currentScreen,
                onNavigate = { screen -> viewModel.navigateTo(screen) },
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                AppScreen.HOME -> HomeScreen(viewModel)
                AppScreen.BIBLE_READER -> BibleReaderScreen(viewModel)
                AppScreen.SEARCH -> BibleSearchScreen(viewModel)
                AppScreen.STUDY_LESSONS -> StudyLessonsScreen(viewModel)
                AppScreen.LESSON_DETAIL -> LessonDetailScreen(viewModel)
                AppScreen.SERMON_BUILDER -> SermonBuilderScreen(viewModel)
                AppScreen.AI_TEACHER -> AiTeacherScreen(viewModel)
                AppScreen.DEVOTIONAL -> DevotionalScreen(viewModel)
                AppScreen.SAVED_ITEMS -> SavedItemsScreen(viewModel)
                AppScreen.DOWNLOAD_MANAGER -> DownloadManagerScreen(viewModel)
                AppScreen.SETTINGS -> SettingsScreen(viewModel)
                AppScreen.APK_DOWNLOAD_CENTER -> ApkDownloadCenterScreen(viewModel)
            }
        }
    }
}
