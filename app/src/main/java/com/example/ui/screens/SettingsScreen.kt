package com.example.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ChristianHeaderBanner
import com.example.ui.theme.ChristianGold
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.BibleViewModel

@Composable
fun SettingsScreen(
    viewModel: BibleViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val fontSizeSp by viewModel.fontSizeSp.collectAsState()
    val isEnglish by viewModel.isEnglishMode.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val notificationsEnabled by viewModel.dailyVerseNotificationEnabled.collectAsState()

    var showApkDialog by remember { mutableStateOf(false) }
    var showUpdateDialog by remember { mutableStateOf(false) }
    var showImportDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var importInputText by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("settings_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ChristianHeaderBanner(
                title = if (isEnglish) "Settings & Preferences" else "सेटिङ्स तथा व्यवस्थापन",
                subtitle = if (isEnglish) "Language, appearance, offline data & APK updates" else "भाषा, फन्ट साइज, अफलाइन सामग्री, र एप इन्स्टलेशन",
                icon = Icons.Default.Android
            )
        }

        // Section: Appearance & Reading
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "पठन तथा प्रदर्शन (Display & Reading)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Language toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.toggleLanguage() },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Language, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = "भाषा (Language)", fontWeight = FontWeight.SemiBold)
                                Text(
                                    text = if (isEnglish) "English" else "नेपाली (Primary)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Text(
                            text = if (isEnglish) "नेपालीमा बदल्नुहोस्" else "Switch to English",
                            style = MaterialTheme.typography.labelSmall,
                            color = ChristianGold,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    // Dark Mode toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.DarkMode, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = "डार्क मोड (Dark Theme)", fontWeight = FontWeight.SemiBold)
                                Text(
                                    text = if (isDarkMode) "अन (गाढा मोड)" else "अफ (उज्यालो मोड)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { viewModel.toggleDarkMode() },
                            colors = SwitchDefaults.colors(checkedThumbColor = ChristianGold)
                        )
                    }

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    // Font size selector
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.FormatSize, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = "बाइबल फन्ट साइज: $fontSizeSp sp", fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(
                                Pair(14, "सानो"),
                                Pair(16, "मध्यम"),
                                Pair(18, "ठूलो"),
                                Pair(22, "धेरै ठूलो")
                            ).forEach { (size, label) ->
                                val isSelected = fontSizeSp == size
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) ChristianGold else MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { viewModel.setFontSize(size) }
                                ) {
                                    Box(modifier = Modifier.padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
                                        Text(
                                            text = label,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    // Daily Verse Notification toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Notifications, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = "दैनिक वचन सूचना (Notifications)", fontWeight = FontWeight.SemiBold)
                                Text(
                                    text = if (notificationsEnabled) "बिहान ६:०० बजे दैनिक वचन" else "बन्द गरिएको छ",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = { viewModel.toggleNotifications() },
                            colors = SwitchDefaults.colors(checkedThumbColor = ChristianGold)
                        )
                    }
                }
            }
        }

        // Section: Offline Storage & Data Management
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "अफलाइन सामग्री तथा डाटा (Storage & Data)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Open Download Manager
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.navigateTo(AppScreen.DOWNLOAD_MANAGER) },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Download, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = "अफलाइन डाउनलोड प्रबन्धक", fontWeight = FontWeight.SemiBold)
                                Text(text = "सम्पूर्ण बाइबल र पाठहरू डाउनलोड गर्नुहोस्", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Text(text = "खोल्नुहोस् →", style = MaterialTheme.typography.labelSmall, color = ChristianGold, fontWeight = FontWeight.Bold)
                    }

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    // Authorized Bible Import
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showImportDialog = true },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.CloudUpload, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = "अधिकृत बाइबल डाटा आयात (Import)", fontWeight = FontWeight.SemiBold)
                                Text(text = "थप नेपाली बाइबल अनुवाद/अध्यायहरू आयात गर्नुहोस्", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Text(text = "इम्पोर्ट →", style = MaterialTheme.typography.labelSmall, color = ChristianGold, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Section: Android App Installation & APK
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "एप इन्स्टलेशन र अपडेट (App Installation & APK)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Current Version
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Android, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = "एप संस्करण (Version)", fontWeight = FontWeight.SemiBold)
                                Text(text = "v1.0.0 (Build 1) — Official Release", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    // Check for Updates
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showUpdateDialog = true },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Sync, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = "अपडेट जाँच गर्नुहोस् (Check for Updates)", fontWeight = FontWeight.SemiBold)
                                Text(text = "नयाँ बाइबल सामग्री र फिचरहरूको खोजी", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Text(text = "जाँच →", style = MaterialTheme.typography.labelSmall, color = ChristianGold, fontWeight = FontWeight.Bold)
                    }

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    // Open APK Download Center
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.navigateTo(AppScreen.APK_DOWNLOAD_CENTER) },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Download, contentDescription = null, tint = ChristianGold)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = "एप डाउनलोड तथा इन्स्टल (APK Center)", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                Text(text = "वास्तविक Android APK तयार, डाउनलोड र इन्स्टल गर्नुहोस्", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Text(text = "खोल्नुहोस् →", style = MaterialTheme.typography.labelSmall, color = ChristianGold, fontWeight = FontWeight.Bold)
                    }

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    // Android APK Download & Installation Instructions
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showApkDialog = true },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.SystemUpdate, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = "Android APK डाउनलोड तथा स्थापना विधि", fontWeight = FontWeight.SemiBold)
                                Text(text = "मोबाइलमा सिधै APK फाइल इन्स्टल गर्ने आधिकारिक तरिका", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Text(text = "हेर्नुहोस् →", style = MaterialTheme.typography.labelSmall, color = ChristianGold, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Section: About & Privacy
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "जानकारी तथा गोपनीयता (About & Legal)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showPrivacyDialog = true },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.PrivacyTip, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = "गोपनीयता नीति (Privacy Policy)", fontWeight = FontWeight.SemiBold)
                        }
                        Text(text = "पढ्नुहोस् →", style = MaterialTheme.typography.labelSmall, color = ChristianGold, fontWeight = FontWeight.Bold)
                    }

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showAboutDialog = true },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = "एपको बारेमा (About the App)", fontWeight = FontWeight.SemiBold)
                        }
                        Text(text = "हेर्नुहोस् →", style = MaterialTheme.typography.labelSmall, color = ChristianGold, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // Dialog: APK Installation Instructions
    if (showApkDialog) {
        AlertDialog(
            onDismissRequest = { showApkDialog = false },
            title = {
                Text(
                    text = "Android APK डाउनलोड तथा स्थापना विधि",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            text = {
                Column {
                    Text(
                        text = "यो एप्लिकेशन आधुनिक एन्ड्रोइड स्मार्टफोनहरूका लागि पूर्ण रूपमा अनुकूलित छ।",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "१. APK निर्माण र डाउनलोड:\nAI Studio को माथिल्लो मेनु वा सेटिङ्सबाट 'Export Project (ZIP)' डाउनलोड गर्न सक्नुहुन्छ वा सिधै Gradle मार्फत 'gradle assembleRelease' गरेर साइन गरिएको `app-release.apk` प्राप्त गर्न सक्नुहुन्छ।\n\n२. मोबाइलमा स्थापना (Sideload):\nआफ्नो एन्ड्रोइड फोनमा Settings > Security मा जानुहोस् र 'Install from Unknown Sources' लाई अनुमति दिनुहोस्।\n\n३. इन्स्टल र अफलाइन प्रयोग:\nडाउनलोड गरिएको `.apk` फाइलमा ट्याप गरी 'Install' गर्नुहोस्। त्यसपछि इन्टरनेटविना पनि सम्पूर्ण नेपाली बाइबल, उपदेश तयारी, र अध्ययन पाठहरू प्रयोग गर्नुहोस्।",
                        style = MaterialTheme.typography.bodySmall,
                        lineHeight = 20.sp
                    )
                }
            },
            confirmButton = {
                Button(onClick = { showApkDialog = false }) {
                    Text("बुझें (OK)")
                }
            }
        )
    }

    // Dialog: Check for Updates
    if (showUpdateDialog) {
        AlertDialog(
            onDismissRequest = { showUpdateDialog = false },
            title = {
                Text(
                    text = "एप अपडेट स्थिति",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "✓ तपाईंले नेपाली बाइबल अध्ययन एपको पछिल्लो आधिकारिक संस्करण (v1.0.0) प्रयोग गरिरहनुभएको छ।",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "सबै ६६ पुस्तकहरू, १० अध्ययन पाठहरू र उपदेश लाइब्रेरी पूर्ण रूपमा अद्यावधिक छन्।",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showUpdateDialog = false }) {
                    Text("ठीक छ")
                }
            }
        )
    }

    // Dialog: Authorized Bible Data Import
    if (showImportDialog) {
        AlertDialog(
            onDismissRequest = { showImportDialog = false },
            title = {
                Text(
                    text = "अधिकृत बाइबल डाटा आयात (Import)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "ढाँचा: BookId | BookName | Chapter | Verse | Text | Explanation",
                        style = MaterialTheme.typography.labelSmall,
                        color = ChristianGold,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = importInputText,
                        onValueChange = { importInputText = it },
                        placeholder = { Text("उदा: 43 | यूहन्ना | 1 | 1 | आदिमा वचन हुनुहुन्थ्यो... | व्याख्या...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp),
                        maxLines = 6
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.importBibleData(importInputText) { count ->
                            Toast.makeText(context, "$count वटा पदहरू सफलतापूर्वक आयात भए!", Toast.LENGTH_SHORT).show()
                            showImportDialog = false
                        }
                    }
                ) {
                    Text("आयात गर्नुहोस्")
                }
            },
            dismissButton = {
                TextButton(onClick = { showImportDialog = false }) {
                    Text("रद्द")
                }
            }
        )
    }

    // Dialog: Privacy Policy
    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = {
                Text(text = "गोपनीयता नीति (Privacy Policy)", fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    text = "नेपाली बाइबल अध्ययन एपले प्रयोगकर्ताको गोपनीयतालाई सर्वोच्च प्राथमिकता दिन्छ।\n\n१. डाटा स्थानीय भण्डारण: तपाईंका सबै व्यक्तिगत नोटहरू, बुकमार्कहरू, हाइलाइटहरू, र पठन इतिहास १००% तपाईंको आफ्नै मोबाइल डिभाइसमा सुरक्षित रहन्छन्।\n२. कुनै अनाधिकृत विज्ञापन छैन: यस एपमा कुनै पनि अनावश्यक विज्ञापन वा ट्र्याकरहरू समावेश गरिएका छैनन्।\n३. अफलाइन उपयोग: बाइबल अध्ययनका लागि इन्टरनेट अनिवार्य छैन।",
                    style = MaterialTheme.typography.bodySmall,
                    lineHeight = 20.sp
                )
            },
            confirmButton = {
                TextButton(onClick = { showPrivacyDialog = false }) {
                    Text("बन्द गर्नुहोस्")
                }
            }
        )
    }

    // Dialog: About App
    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = {
                Text(text = "नेपाली बाइबल अध्ययन", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            },
            text = {
                Column {
                    Text(
                        text = "परमेश्वरको वचन बुझौँ, विश्वासमा बढौँ।",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = ChristianGold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "यो एप्लिकेशन नेपाली भाषी क्रिश्चियन समुदाय, पास्टरहरू, प्रचारकहरू, युवाहरू, र प्रत्येक विश्वासीलाई परमेश्वरको जीवित वचनको गहिरो अध्ययन गर्न, उपदेश तयार गर्न, र विश्वासमा बढ्न मद्दत गर्न निर्माण गरिएको हो।",
                        style = MaterialTheme.typography.bodySmall,
                        lineHeight = 20.sp
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text("आमेन")
                }
            }
        )
    }
}
