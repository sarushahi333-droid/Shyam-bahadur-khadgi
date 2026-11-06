package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.service.SermonBuilderService
import com.example.ui.components.ChristianHeaderBanner
import com.example.ui.theme.ChristianGold
import com.example.ui.theme.ChristianGoldContainer
import com.example.ui.theme.YatraOneFontFamily
import com.example.ui.viewmodel.BibleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SermonBuilderScreen(
    viewModel: BibleViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTabIndex by remember { mutableStateOf(0) } // 0: Builder, 1: Saved Sermons

    var selectedTopic by remember { mutableStateOf(SermonBuilderService.availableTopics[0].topicName) }
    var selectedAudience by remember { mutableStateOf(SermonBuilderService.availableAudiences[0]) }
    var selectedDuration by remember { mutableStateOf(30) }

    var topicDropdownExpanded by remember { mutableStateOf(false) }
    var audienceDropdownExpanded by remember { mutableStateOf(false) }

    val currentSermon by viewModel.currentGeneratedSermon.collectAsState()
    val savedSermons by viewModel.allSermons.collectAsState()
    val isEnglish by viewModel.isEnglishMode.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("sermon_builder_screen")
    ) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 },
                text = { Text(if (isEnglish) "Prepare Sermon" else "उपदेश तयार गर्नुहोस्") }
            )
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                text = { Text(if (isEnglish) "Saved (${savedSermons.size})" else "सुरक्षित उपदेशहरू (${savedSermons.size})") }
            )
        }

        if (selectedTabIndex == 0) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    ChristianHeaderBanner(
                        title = if (isEnglish) "Sermon Builder" else "व्यावसायिक उपदेश तयारी",
                        subtitle = if (isEnglish) "Biblically grounded, structured preaching messages" else "विषय, स्रोता र समयअनुसार पूर्ण उपदेश रूपरेखा निर्माण",
                        icon = Icons.Default.RecordVoiceOver
                    )
                }

                // Controls Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "उपदेशका मापदण्ड छान्नुहोस्:",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Topic Dropdown
                            Text(text = "१. उपदेशको विषय (Topic):", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            ExposedDropdownMenuBox(
                                expanded = topicDropdownExpanded,
                                onExpandedChange = { topicDropdownExpanded = !topicDropdownExpanded }
                            ) {
                                OutlinedTextField(
                                    value = selectedTopic,
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = topicDropdownExpanded) },
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                ExposedDropdownMenu(
                                    expanded = topicDropdownExpanded,
                                    onDismissRequest = { topicDropdownExpanded = false }
                                ) {
                                    SermonBuilderService.availableTopics.forEach { topic ->
                                        DropdownMenuItem(
                                            text = { Text(topic.topicName) },
                                            onClick = {
                                                selectedTopic = topic.topicName
                                                topicDropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Audience Dropdown
                            Text(text = "२. लक्षित स्रोता (Target Audience):", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            ExposedDropdownMenuBox(
                                expanded = audienceDropdownExpanded,
                                onExpandedChange = { audienceDropdownExpanded = !audienceDropdownExpanded }
                            ) {
                                OutlinedTextField(
                                    value = selectedAudience,
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = audienceDropdownExpanded) },
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                ExposedDropdownMenu(
                                    expanded = audienceDropdownExpanded,
                                    onDismissRequest = { audienceDropdownExpanded = false }
                                ) {
                                    SermonBuilderService.availableAudiences.forEach { audience ->
                                        DropdownMenuItem(
                                            text = { Text(audience) },
                                            onClick = {
                                                selectedAudience = audience
                                                audienceDropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Duration Selector Chips (15, 30, 45, 60 minutes)
                            Text(text = "३. उपदेशको समय (Duration):", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                SermonBuilderService.availableDurations.forEach { dur ->
                                    val isSelected = selectedDuration == dur
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (isSelected) ChristianGold else MaterialTheme.colorScheme.surfaceVariant,
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable { selectedDuration = dur }
                                    ) {
                                        Box(
                                            modifier = Modifier.padding(vertical = 10.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "$dur मिनेट",
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            Button(
                                onClick = {
                                    viewModel.generateSermon(selectedTopic, selectedAudience, selectedDuration)
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("generate_sermon_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Icon(imageVector = Icons.Default.RecordVoiceOver, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "पूर्ण उपदेश तयार गर्नुहोस् (Generate Sermon)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }

                // Render Generated Sermon
                currentSermon?.let { sermon ->
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("generated_sermon_card"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                // Action Header: Save, Copy, Share
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        color = ChristianGoldContainer,
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = "${sermon.durationMinutes} मिनेट उपदेश",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF6B4E00),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }

                                    Row {
                                        IconButton(
                                            onClick = {
                                                viewModel.saveCurrentSermon()
                                                Toast.makeText(context, "उपदेश सुरक्षित भयो!", Toast.LENGTH_SHORT).show()
                                            },
                                            modifier = Modifier.testTag("save_sermon_button")
                                        ) {
                                            Icon(imageVector = Icons.Default.Bookmark, contentDescription = "Save", tint = ChristianGold)
                                        }

                                        IconButton(
                                            onClick = {
                                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                val clip = ClipData.newPlainText("Sermon", "${sermon.title}\n\nपाठ: ${sermon.mainPassage}\n\n${sermon.introduction}\n\nमुख्य बुँदाहरू:\n${sermon.mainPoints.joinToString("\n")}\n\nव्याख्या:\n${sermon.detailedExplanations}\n\nनिष्कर्ष:\n${sermon.conclusion}\n\nप्रार्थना:\n${sermon.closingPrayer}")
                                                clipboard.setPrimaryClip(clip)
                                                Toast.makeText(context, "उपदेश कपी भयो!", Toast.LENGTH_SHORT).show()
                                            }
                                        ) {
                                            Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy")
                                        }

                                        IconButton(
                                            onClick = {
                                                val sendIntent = Intent().apply {
                                                    action = Intent.ACTION_SEND
                                                    putExtra(Intent.EXTRA_TEXT, "${sermon.title}\n\nमुख्य पाठ: ${sermon.mainPassage}\n\n${sermon.introduction}\n\n— नेपाली बाइबल अध्ययन एप")
                                                    type = "text/plain"
                                                }
                                                context.startActivity(Intent.createChooser(sendIntent, "Share Sermon"))
                                            }
                                        ) {
                                            Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = sermon.title,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontFamily = YatraOneFontFamily,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "मुख्य बाइबल पाठ: ${sermon.mainPassage}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = ChristianGold
                                )

                                Divider(modifier = Modifier.padding(vertical = 12.dp))

                                // Sections
                                SermonTextSection(title = "परिचय (Introduction):", text = sermon.introduction)

                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "मुख्य बुँदाहरू (Main Points):",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                sermon.mainPoints.forEach { pt ->
                                    Text(
                                        text = pt,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(vertical = 3.dp),
                                        lineHeight = 22.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))
                                SermonTextSection(title = "विस्तृत व्याख्या (Explanations):", text = sermon.detailedExplanations)

                                Spacer(modifier = Modifier.height(12.dp))
                                SermonTextSection(title = "बाइबलीय उदाहरणहरू (Biblical Examples):", text = sermon.biblicalExamples)

                                Spacer(modifier = Modifier.height(12.dp))
                                SermonTextSection(title = "आधुनिक जीवनका उदाहरण (Modern Examples):", text = sermon.modernExamples)

                                Spacer(modifier = Modifier.height(12.dp))
                                SermonTextSection(title = "व्यावहारिक प्रयोग (Practical Applications):", text = sermon.practicalApplications)

                                Spacer(modifier = Modifier.height(12.dp))
                                SermonTextSection(title = "निष्कर्ष (Conclusion):", text = sermon.conclusion)

                                Spacer(modifier = Modifier.height(14.dp))
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = ChristianGoldContainer.copy(alpha = 0.5f))
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(
                                            text = "समापन प्रार्थना (Closing Prayer):",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = sermon.closingPrayer,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontStyle = FontStyle.Italic,
                                            lineHeight = 22.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Saved Sermons Tab
            if (savedSermons.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.RecordVoiceOver,
                        contentDescription = null,
                        modifier = Modifier.size(54.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "कुनै पनि सुरक्षित उपदेश छैन।",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "माथिको ट्याबबाट उपदेश तयार गरी सुरक्षित गर्नुहोस्।",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(savedSermons) { s ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = s.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.weight(1f)
                                    )
                                    IconButton(onClick = { viewModel.deleteSermon(s.id) }) {
                                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red.copy(alpha = 0.7f))
                                    }
                                }
                                Text(
                                    text = "पाठ: ${s.mainPassage} · ${s.durationMinutes} मिनेट",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = ChristianGold,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = s.introduction,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 3
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SermonTextSection(title: String, text: String) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 22.sp
        )
    }
}
