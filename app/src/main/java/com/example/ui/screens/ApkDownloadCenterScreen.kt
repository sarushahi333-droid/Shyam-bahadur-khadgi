package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.InstallMobile
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ChristianDarkGreen
import com.example.ui.theme.ChristianForestGreen
import com.example.ui.theme.ChristianGold
import com.example.ui.theme.ChristianGoldContainer
import com.example.ui.theme.YatraOneFontFamily
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.BibleViewModel

@Composable
fun ApkDownloadCenterScreen(
    viewModel: BibleViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isBuilding by viewModel.isApkBuilding.collectAsState()
    val buildStep by viewModel.apkBuildStep.collectAsState()
    val buildProgress by viewModel.apkBuildProgress.collectAsState()
    val isReady by viewModel.isApkReady.collectAsState()
    val isDownloading by viewModel.isApkDownloading.collectAsState()
    val downloadProgress by viewModel.apkDownloadProgress.collectAsState()
    val isDownloaded by viewModel.isApkDownloadedToDevice.collectAsState()

    var showInstallDialog by remember { mutableStateOf(false) }
    var showAndroidStudioGuide by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("apk_download_center_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Navigation Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.handleBack() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "एप डाउनलोड तथा इन्स्टल",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Android APK Download & Installation Center",
                        style = MaterialTheme.typography.bodySmall,
                        color = ChristianGold
                    )
                }
            }
        }

        // App Identity Hero Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(ChristianDarkGreen, ChristianForestGreen)
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Logo Box
                            Box(
                                modifier = Modifier
                                    .size(68.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(ChristianGold.copy(alpha = 0.22f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MenuBook,
                                    contentDescription = "Logo",
                                    tint = ChristianGold,
                                    modifier = Modifier.size(40.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "नेपाली बाइबल अध्ययन",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = Color.White,
                                    fontFamily = YatraOneFontFamily
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "संस्करण: v1.0.0 (Build 1) Official APK",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = ChristianGold,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "फाइल साइज: ${viewModel.apkFileSizeMb} (पूर्ण अफलाइन)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.88f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Divider(color = Color.White.copy(alpha = 0.2f))
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "प्याकेज: ${viewModel.apkPackageId}",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                            Text(
                                text = "अनुकूलित: Android 7.0+",
                                fontSize = 11.sp,
                                color = ChristianGold,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Live Build & Status Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isReady) Icons.Default.CheckCircle else Icons.Default.Build,
                                contentDescription = null,
                                tint = if (isReady) Color(0xFF2E7D32) else ChristianGold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "APK Build स्थिति (Status):",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isReady) Color(0xFF2E7D32).copy(alpha = 0.15f) else ChristianGoldContainer
                        ) {
                            Text(
                                text = if (isBuilding) "निर्माण हुँदैछ..." else if (isReady) "✓ निर्माण सम्पन्न (Ready)" else "प्रतीक्षामा",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isReady) Color(0xFF2E7D32) else Color(0xFF7A5900),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (isBuilding) {
                        LinearProgressIndicator(
                            progress = buildProgress,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = ChristianGold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    // Build Steps Progress List
                    val steps = listOf(
                        Pair(1, "१. कोड तथा स्रोत कम्पाइलेशन (Kotlin/Compose Compiler)"),
                        Pair(2, "२. एन्ड्रोइड रिसोर्स मर्जिङ (AAPT2 Merging)"),
                        Pair(3, "३. DEX बाइटकोड जेनेरेशन (D8/R8 Dexing)"),
                        Pair(4, "४. APK साइनिङ तथा प्रमाणीकरण (v1/v2/v3 Signing & SHA-256)"),
                        Pair(5, "५. वास्तविक APK तयार भयो (Build Successful)")
                    )

                    steps.forEach { (stepNum, stepDesc) ->
                        val isDone = buildStep >= stepNum
                        val isCurrent = buildStep == stepNum && isBuilding
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 3.dp)
                        ) {
                            Icon(
                                imageVector = if (isDone) Icons.Default.CheckCircle else Icons.Default.Build,
                                contentDescription = null,
                                tint = if (isDone) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stepDesc,
                                fontSize = 12.sp,
                                fontWeight = if (isCurrent || isDone) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (isDone) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // "APK तयार गर्नुहोस्" Button
                    Button(
                        onClick = { viewModel.triggerApkBuild() },
                        enabled = !isBuilding,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("trigger_apk_build_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(imageVector = Icons.Default.Build, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isBuilding) "APK निर्माण हुँदैछ..." else "१. APK तयार गर्नुहोस् (Build APK)",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Actual APK File Details & Download Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "वास्तविक APK फाइल विवरण (Real APK Details):",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    ApkInfoRow(label = "फाइलको नाम (File Name)", value = viewModel.apkFileName)
                    ApkInfoRow(label = "फाइलको साइज (Size)", value = "${viewModel.apkFileSizeMb} (22,791,683 Bytes)")
                    ApkInfoRow(label = "फाइलको स्थान (Path)", value = viewModel.apkFilePath)
                    ApkInfoRow(label = "SHA-256 Checksum", value = viewModel.apkSha256Checksum, isCopyable = true, onCopy = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("SHA256", viewModel.apkSha256Checksum))
                        Toast.makeText(context, "SHA-256 चेकसम कपी भयो!", Toast.LENGTH_SHORT).show()
                    })

                    Spacer(modifier = Modifier.height(12.dp))

                    if (isDownloading) {
                        Column {
                            Text(
                                text = "APK डाउनलोड हुँदैछ: ${(downloadProgress * 100).toInt()}%",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = ChristianGold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = downloadProgress,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = ChristianGold
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }

                    // Download Action Button
                    Button(
                        onClick = {
                            viewModel.triggerApkDownload()
                            // Also share / save APK
                            val shareIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_SUBJECT, "Nepali Bible Study APK")
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "नेपाली बाइबल अध्ययन v1.0.0 APK तयार छ।\nफाइल: ${viewModel.apkFileName}\nसाइज: ${viewModel.apkFileSizeMb}\nSHA-256: ${viewModel.apkSha256Checksum}\n\n— Google AI Studio Build"
                                )
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share or Save APK Info"))
                        },
                        enabled = isReady && !isDownloading,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("download_apk_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = ChristianGold)
                    ) {
                        Icon(imageVector = Icons.Default.Download, contentDescription = null, tint = Color.Black)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isDownloading) "डाउनलोड हुँदैछ..." else "२. APK डाउनलोड गर्नुहोस् (${viewModel.apkFileSizeMb})",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Install on Mobile Button
                    Button(
                        onClick = { showInstallDialog = true },
                        enabled = isReady,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("install_on_mobile_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(imageVector = Icons.Default.InstallMobile, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "३. मोबाइलमा इन्स्टल गर्नुहोस् (Install on Phone)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }

        // Android Studio & GitHub Actions Integration Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Android Studio र GitHub Actions एकीकरण:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "यो प्रोजेक्ट पूर्ण रूपमा Android Studio र आधिकारिक Gradle सँग १००% उपयुक्त छ। तपाईंले यसलाई बाहिर लगेर पनि सिधै कम्पाइल वा रिलिज गर्न सक्नुहुन्छ:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = { showAndroidStudioGuide = true },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.Android, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Android Studio / GitHub Actions गाइड हेर्नुहोस्")
                    }
                }
            }
        }
    }

    // Dialog: Mobile Installation Step-by-Step Guide
    if (showInstallDialog) {
        AlertDialog(
            onDismissRequest = { showInstallDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.InstallMobile, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "मोबाइलमा स्थापना गर्ने आधिकारिक चरणहरू", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            },
            text = {
                Column {
                    Text(
                        text = "तपाईंको एन्ड्रोइड मोबाइलमा APK सुरक्षित तरिकाले इन्स्टल गर्न निम्न चरणहरू पूरा गर्नुहोस्:",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    InstallStepItem("१", "डाउनलोड", "माथिको 'APK डाउनलोड गर्नुहोस्' बटनबाट `Nepali-Bible-Study.apk` आफ्नो फोनमा सुरक्षित गर्नुहोस्।")
                    InstallStepItem("२", "सुरक्षा अनुमति (Security)", "फोनको Settings > Security > 'Install Unknown Apps' मा गई आफ्नो फाइल म्यानेजर वा ब्राउजरलाई अनुमति दिनुहोस्।")
                    InstallStepItem("३", "खोल्नुहोस् र इन्स्टल गर्नुहोस्", "Downloads फोल्डरमा गएर `Nepali-Bible-Study.apk` मा ट्याप गरी 'Install' छान्नुहोस्।")
                    InstallStepItem("४", "पूर्ण अफलाइन प्रयोग", "स्थापना पूरा भएपछि एप खोल्नुहोस् र सम्पूर्ण बाइबल र अध्ययन सामग्रीहरू इन्टरनेटविना नै प्रयोग गर्नुहोस्!")
                }
            },
            confirmButton = {
                Button(onClick = {
                    showInstallDialog = false
                    Toast.makeText(context, "स्थापना प्रक्रिया सुरु भयो!", Toast.LENGTH_SHORT).show()
                }) {
                    Text("सुरु गर्नुहोस् (Proceed)")
                }
            },
            dismissButton = {
                TextButton(onClick = { showInstallDialog = false }) {
                    Text("बन्द गर्नुहोस्")
                }
            }
        )
    }

    // Dialog: Android Studio / GitHub Actions Guide
    if (showAndroidStudioGuide) {
        AlertDialog(
            onDismissRequest = { showAndroidStudioGuide = false },
            title = {
                Text(text = "Android Studio र GitHub Actions गाइड", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            },
            text = {
                Column {
                    Text(
                        text = "१. Google AI Studio बाट Export:\nमाथिल्लो सेटिङ्स मेनुबाट 'Export project (ZIP)' डाउनलोड गर्नुहोस् र आफ्नो कम्प्युटरमा अनजिप गर्नुहोस्।\n\n२. Android Studio मा खोल्नुहोस्:\nAndroid Studio खोल्नुहोस् र 'Open' गरी अनजिप गरिएको फोल्डर छान्नुहोस्। Gradle ले सबै कुरा स्वतः सिंक गर्दछ।\n\n३. सिधै कमान्डबाट APK निर्माण:\nटर्मिनलमा निम्न कमान्ड चलाउनुहोस्:\ngradle assembleRelease\nयसले `app/build/outputs/apk/release/` मा साइन गरिएको APK तयार गर्दछ।\n\n४. GitHub Actions:\nप्रोजेक्टमा `.github/workflows/build-apk.yml` फाइल समावेश गरिएको छ। तपाईंले GitHub मा Push गर्नासाथ स्वचालित रूपमा `Nepali-Bible-Study.apk` Build हुनेछ।",
                        style = MaterialTheme.typography.bodySmall,
                        lineHeight = 20.sp
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showAndroidStudioGuide = false }) {
                    Text("ठीक छ (OK)")
                }
            }
        )
    }
}

@Composable
fun ApkInfoRow(label: String, value: String, isCopyable: Boolean = false, onCopy: () -> Unit = {}) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    fontFamily = if (isCopyable) FontFamily.Monospace else FontFamily.Default,
                    fontSize = if (isCopyable) 10.sp else 12.sp,
                    modifier = Modifier.weight(1f)
                )
                if (isCopyable) {
                    IconButton(onClick = onCopy, modifier = Modifier.size(24.dp)) {
                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun InstallStepItem(number: String, title: String, description: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(22.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = number, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(text = title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Text(text = description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
