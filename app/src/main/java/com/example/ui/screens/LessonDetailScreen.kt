package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ChristianGold
import com.example.ui.theme.ChristianGoldContainer
import com.example.ui.theme.YatraOneFontFamily
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.BibleViewModel

@Composable
fun LessonDetailScreen(
    viewModel: BibleViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lesson by viewModel.selectedLesson.collectAsState()
    val isEnglish by viewModel.isEnglishMode.collectAsState()

    if (lesson == null) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "पाठ छान्नुहोस्")
            Button(onClick = { viewModel.navigateTo(AppScreen.STUDY_LESSONS) }) {
                Text(text = "पाठहरूको सूचीमा फर्कनुहोस्")
            }
        }
        return
    }

    val currentLesson = lesson!!

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("lesson_detail_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Navigation Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.navigateTo(AppScreen.STUDY_LESSONS) },
                    modifier = Modifier.testTag("lesson_back_button")
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isEnglish) currentLesson.categoryNameEnglish else currentLesson.categoryNameNepali,
                        style = MaterialTheme.typography.labelMedium,
                        color = ChristianGold,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (isEnglish) currentLesson.titleEnglish else currentLesson.titleNepali,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }
                IconButton(
                    onClick = {
                        val shareText = "${currentLesson.titleNepali}\n\nविषय: ${currentLesson.categoryNameNepali}\n\nमुख्य पदहरू:\n${currentLesson.mainVerses.joinToString("\n")}\n\n— नेपाली बाइबल अध्ययन एप"
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, shareText)
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(sendIntent, "Share Lesson"))
                    }
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
                }
            }
        }

        // Lesson Title Header Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = if (isEnglish) currentLesson.titleEnglish else currentLesson.titleNepali,
                        style = MaterialTheme.typography.headlineLarge,
                        fontFamily = YatraOneFontFamily,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "विषय: ${currentLesson.categoryNameNepali} (${currentLesson.categoryNameEnglish})",
                        style = MaterialTheme.typography.labelLarge,
                        color = ChristianGold,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // 1. Topic Introduction (विषय परिचय)
        item {
            LessonSectionCard(
                title = "१. विषय परिचय (Topic Introduction)",
                icon = Icons.Default.MenuBook,
                content = currentLesson.topicIntro
            )
        }

        // 2. Main Bible Verses (मुख्य बाइबल पदहरू)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(ChristianGold.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.FormatQuote, contentDescription = null, tint = ChristianGold, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "२. मुख्य बाइबल पदहरू (Main Verses)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    currentLesson.mainVerses.forEach { verse ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                        ) {
                            Text(
                                text = verse,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(12.dp),
                                lineHeight = 22.sp
                            )
                        }
                    }
                }
            }
        }

        // 3. Detailed Explanation (विस्तृत व्याख्या)
        item {
            LessonSectionCard(
                title = "३. विस्तृत व्याख्या (Detailed Explanation)",
                icon = Icons.Default.AutoAwesome,
                content = currentLesson.detailedExplanation
            )
        }

        // 4. Real-life Examples (वास्तविक जीवनका उदाहरणहरू)
        item {
            LessonSectionCard(
                title = "४. वास्तविक जीवनका उदाहरणहरू (Real-life Examples)",
                icon = Icons.Default.MenuBook,
                content = currentLesson.realLifeExamples
            )
        }

        // 5. Practical Applications (व्यावहारिक प्रयोगहरू)
        item {
            LessonSectionCard(
                title = "५. व्यावहारिक प्रयोगहरू (Practical Applications)",
                icon = Icons.Default.VolunteerActivism,
                content = currentLesson.practicalApplications
            )
        }

        // 6. Questions for Reflection (मनन तथा आत्म-मूल्याङ्कनका प्रश्नहरू)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(ChristianGold.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.HelpOutline, contentDescription = null, tint = ChristianGold, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "६. मनन तथा प्रश्नहरू (Questions for Reflection)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    currentLesson.reflectionQuestions.forEachIndexed { index, question ->
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Text(
                                text = "${index + 1}. ",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = question,
                                style = MaterialTheme.typography.bodyMedium,
                                lineHeight = 22.sp
                            )
                        }
                    }
                }
            }
        }

        // 7. Closing Prayer (समापन प्रार्थना)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = ChristianGoldContainer.copy(alpha = 0.6f)
                )
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(ChristianGold),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.VolunteerActivism, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "७. समापन प्रार्थना (Closing Prayer)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = currentLesson.closingPrayer,
                        style = MaterialTheme.typography.bodyLarge,
                        fontStyle = FontStyle.Italic,
                        lineHeight = 24.sp
                    )
                }
            }
        }
    }
}

@Composable
fun LessonSectionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 24.sp
            )
        }
    }
}
