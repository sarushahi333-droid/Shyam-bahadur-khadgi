package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.service.AiBibleTeacherService
import com.example.ui.components.ChristianHeaderBanner
import com.example.ui.theme.ChristianGold
import com.example.ui.theme.ChristianGoldContainer
import com.example.ui.theme.YatraOneFontFamily
import com.example.ui.viewmodel.BibleViewModel

@Composable
fun AiTeacherScreen(
    viewModel: BibleViewModel,
    modifier: Modifier = Modifier
) {
    var queryText by remember { mutableStateOf("") }
    val aiResponses by viewModel.aiResponses.collectAsState()
    val isThinking by viewModel.isAiThinking.collectAsState()
    val isEnglish by viewModel.isEnglishMode.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("ai_teacher_screen")
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ChristianHeaderBanner(
                    title = if (isEnglish) "AI Bible Teacher" else "AI बाइबल शिक्षक",
                    subtitle = if (isEnglish) "Biblical, contextual answers grounded strictly in Scripture" else "बाइबलमा आधारित स्पष्ट जवाफ, सन्दर्भ, पद तुलना र ऐतिहासिक पृष्ठभूमि",
                    icon = Icons.Default.AutoAwesome
                )
            }

            // Quick Questions Chips
            item {
                Column {
                    Text(
                        text = "लोकप्रिय बाइबलीय प्रश्नहरू (छुनुहोस्):",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(AiBibleTeacherService.popularQuestions) { q ->
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
                                modifier = Modifier.clickable {
                                    queryText = q
                                    viewModel.askAiTeacher(q)
                                }
                            ) {
                                Text(
                                    text = q,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
            }

            if (isThinking) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(
                            modifier = Modifier.padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                color = ChristianGold,
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.5.dp
                            )
                            Spacer(modifier = Modifier.width(14.dp))
                            Text(
                                text = "बाइबल पदहरू र ऐतिहासिक पृष्ठभूमि जाँचिँदैछ...",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            if (aiResponses.isEmpty() && !isThinking) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(ChristianGold.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.HelpOutline,
                                        contentDescription = null,
                                        tint = ChristianGold
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "तपाईंको प्रश्न सोध्नुहोस्",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "AI बाइबल शिक्षकले पवित्रशास्त्रका आधारमा हुबहु बाइबल पदहरू, ऐतिहासिक पृष्ठभूमि, र व्यावहारिक आत्मिक अर्थसहित विस्तृत व्याख्या प्रदान गर्दछ। माथिका सुझावहरूबाट रोज्न सक्नुहुन्छ वा तल आफ्नै प्रश्न लेख्न सक्नुहुन्छ।",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 22.sp
                            )
                        }
                    }
                }
            } else {
                items(aiResponses) { resp ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            // Question header
                            Row(verticalAlignment = Alignment.Top) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.HelpOutline,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "प्रश्न:",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = resp.question,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Divider(modifier = Modifier.padding(vertical = 12.dp))

                            // Exact Bible References
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.MenuBook, contentDescription = null, tint = ChristianGold, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "मुख्य बाइबल सन्दर्भहरू: ${resp.exactBibleReferences.joinToString(", ")}",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = ChristianGold
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Direct Answer
                            Text(
                                text = resp.directAnswer,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                lineHeight = 22.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Scripture Quotation Box
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FormatQuote,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = resp.scriptureTextQuotation,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontStyle = FontStyle.Italic,
                                        lineHeight = 22.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Context
                            AiTeacherSection(
                                title = "ऐतिहासिक तथा आत्मिक पृष्ठभूमि (Context):",
                                icon = Icons.Default.HistoryEdu,
                                content = resp.historicalAndSpiritualContext
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Scripture Comparisons
                            AiTeacherSection(
                                title = "अन्य पदहरूसँग तुलना (Scripture Comparison):",
                                icon = Icons.Default.MenuBook,
                                content = resp.scriptureComparisons
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Practical Application
                            AiTeacherSection(
                                title = "व्यावहारिक आत्मिक शिक्षा (Application):",
                                icon = Icons.Default.Lightbulb,
                                content = resp.practicalApplication
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Disclaimer Note
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = ChristianGoldContainer.copy(alpha = 0.35f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = resp.interpretationNote,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF6B4E00),
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bottom Input Bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = queryText,
                    onValueChange = { queryText = it },
                    placeholder = { Text("बाइबलसम्बन्धी प्रश्न यहाँ सोध्नुहोस्...") },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("ai_query_input"),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 3
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (queryText.isNotBlank()) {
                            viewModel.askAiTeacher(queryText)
                            queryText = ""
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.testTag("send_ai_query_button")
                ) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = "Send")
                }
            }
        }
    }
}

@Composable
fun AiTeacherSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: String
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 22.sp
        )
    }
}
