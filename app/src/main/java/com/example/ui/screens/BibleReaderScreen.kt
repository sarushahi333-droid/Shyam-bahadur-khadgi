package com.example.ui.screens

import android.content.Intent
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.VerseEntity
import com.example.data.model.BibleBook
import com.example.data.model.HighlightColor
import com.example.data.model.Testament
import com.example.ui.theme.ChristianGold
import com.example.ui.theme.YatraOneFontFamily
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.BibleViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BibleReaderScreen(
    viewModel: BibleViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val nav by viewModel.currentChapterNav.collectAsState()
    val verses by viewModel.currentVerses.collectAsState()
    val highlights by viewModel.currentHighlights.collectAsState()
    val bookmarks by viewModel.allBookmarks.collectAsState()
    val fontSizeSp by viewModel.fontSizeSp.collectAsState()
    val isEnglish by viewModel.isEnglishMode.collectAsState()
    val currentBook = viewModel.allBooks.find { it.id == nav.bookId }

    var showBookPicker by remember { mutableStateOf(false) }
    var showChapterPicker by remember { mutableStateOf(false) }
    var selectedVerse by remember { mutableStateOf<VerseEntity?>(null) }
    var showExplanationDialog by remember { mutableStateOf<VerseEntity?>(null) }
    var showNoteDialog by remember { mutableStateOf<VerseEntity?>(null) }
    var noteInputText by remember { mutableStateOf("") }

    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("bible_reader_screen")
    ) {
        // Top Navigation Header: Book & Chapter Picker
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Book selector button
                Button(
                    onClick = { showBookPicker = true },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("select_book_button")
                ) {
                    Text(
                        text = (if (isEnglish) currentBook?.englishName else currentBook?.nepaliName) ?: "बाइबल",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                // Chapter selector button
                Button(
                    onClick = { showChapterPicker = true },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("select_chapter_button")
                ) {
                    Text(
                        text = if (isEnglish) "Chapter ${nav.chapter}" else "अध्याय ${nav.chapter}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                // Font size quick cycle
                IconButton(
                    onClick = {
                        val nextSize = when (fontSizeSp) {
                            14 -> 16
                            16 -> 18
                            18 -> 22
                            else -> 14
                        }
                        viewModel.setFontSize(nextSize)
                    },
                    modifier = Modifier.testTag("font_size_toggle_button")
                ) {
                    Icon(imageVector = Icons.Default.FormatSize, contentDescription = "Font Size")
                }
            }
        }

        // Verses List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${currentBook?.nepaliName ?: ""} — अध्याय ${nav.chapter}",
                        style = MaterialTheme.typography.headlineLarge,
                        fontFamily = YatraOneFontFamily,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "${currentBook?.englishName ?: ""} Chapter ${nav.chapter}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Divider(modifier = Modifier.padding(vertical = 6.dp))
            }

            if (verses.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "यस अध्यायका पदहरू अफलाइन प्याकेजमा उपलब्ध छन्।",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "पूर्ण बाइबल वा सम्बन्धित पुस्तक डाउनलोड गर्न अफलाइन प्रबन्धकमा जानुहोस् वा आधारभूत पदहरू पढ्नुहोस्।",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Button(
                                onClick = { viewModel.navigateTo(AppScreen.DOWNLOAD_MANAGER) }
                            ) {
                                Text(text = "अफलाइन डाउनलोड प्रबन्धक खोल्नुहोस्")
                            }
                        }
                    }
                }
            } else {
                items(verses) { verse ->
                    val highlight = highlights.find { it.verseNumber == verse.verseNumber }
                    val isBookmarked = bookmarks.any {
                        it.bookId == verse.bookId && it.chapter == verse.chapter && it.verseNumber == verse.verseNumber
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedVerse = verse }
                            .testTag("verse_item_${verse.verseNumber}"),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = highlight?.let { Color(it.colorHex).copy(alpha = 0.35f) }
                                ?: MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            // Verse Number Badge
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.size(26.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "${verse.verseNumber}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = verse.nepaliText,
                                    fontSize = fontSizeSp.sp,
                                    lineHeight = (fontSizeSp + 8).sp,
                                    fontWeight = FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                if (isEnglish && verse.englishText.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = verse.englishText,
                                        fontSize = (fontSizeSp - 2).sp,
                                        lineHeight = (fontSizeSp + 4).sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                if (verse.explanation.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Info,
                                            contentDescription = null,
                                            tint = ChristianGold,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "व्याख्या उपलब्ध (छुनुहोस्)",
                                            fontSize = 11.sp,
                                            color = ChristianGold,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            if (isBookmarked) {
                                Icon(
                                    imageVector = Icons.Default.Bookmark,
                                    contentDescription = "Bookmarked",
                                    tint = ChristianGold,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bottom Navigation: Previous / Next Chapter
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = { viewModel.prevChapter() },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("prev_chapter_button")
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Prev")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = if (isEnglish) "Prev" else "अघिल्लो")
                }

                Text(
                    text = "${currentBook?.nepaliName ?: ""} ${nav.chapter}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Button(
                    onClick = { viewModel.nextChapter() },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("next_chapter_button")
                ) {
                    Text(text = if (isEnglish) "Next" else "पछिल्लो")
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Next")
                }
            }
        }
    }

    // Modal: Book Picker
    if (showBookPicker) {
        ModalBottomSheet(
            onDismissRequest = { showBookPicker = false },
            sheetState = bottomSheetState
        ) {
            var selectedTestament by remember { mutableStateOf(Testament.NEW) }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = if (isEnglish) "Select Bible Book" else "बाइबलको पुस्तक छान्नुहोस्",
                    style = MaterialTheme.typography.headlineMedium,
                    fontFamily = YatraOneFontFamily,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                TabRow(
                    selectedTabIndex = if (selectedTestament == Testament.NEW) 0 else 1
                ) {
                    Tab(
                        selected = selectedTestament == Testament.NEW,
                        onClick = { selectedTestament = Testament.NEW },
                        text = { Text("नयाँ नियम (२७)") }
                    )
                    Tab(
                        selected = selectedTestament == Testament.OLD,
                        onClick = { selectedTestament = Testament.OLD },
                        text = { Text("पुरानो नियम (३९)") }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                val filteredBooks = viewModel.allBooks.filter { it.testament == selectedTestament }
                LazyColumn(
                    modifier = Modifier.height(380.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(filteredBooks) { book ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.openChapter(book.id, 1)
                                    showBookPicker = false
                                },
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (book.id == nav.bookId)
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                                else MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = book.nepaliName,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = book.englishName,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Surface(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "${book.totalChapters} अध्याय",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal: Chapter Picker
    if (showChapterPicker && currentBook != null) {
        ModalBottomSheet(
            onDismissRequest = { showChapterPicker = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "${currentBook.nepaliName} — अध्याय छान्नुहोस् (कुल: ${currentBook.totalChapters})",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                val chapters = (1..currentBook.totalChapters).toList()
                LazyColumn(modifier = Modifier.height(300.dp)) {
                    items(chapters.chunked(5)) { row ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            row.forEach { ch ->
                                Button(
                                    onClick = {
                                        viewModel.openChapter(currentBook.id, ch)
                                        showChapterPicker = false
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (ch == nav.chapter)
                                            ChristianGold
                                        else MaterialTheme.colorScheme.primaryContainer
                                    )
                                ) {
                                    Text(
                                        text = "$ch",
                                        fontWeight = FontWeight.Bold,
                                        color = if (ch == nav.chapter) Color.Black else Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal: Verse Click Actions BottomSheet
    selectedVerse?.let { verse ->
        ModalBottomSheet(
            onDismissRequest = { selectedVerse = null }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${verse.bookName} ${verse.chapter}:${verse.verseNumber}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(onClick = { selectedVerse = null }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = verse.nepaliText,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Highlight Color Picker
                Text(
                    text = "हाइलाइट रङ छान्नुहोस् (Highlight):",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    HighlightColor.values().forEach { hc ->
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(hc.hex))
                                .clickable {
                                    viewModel.highlightVerse(verse, hc.hex)
                                    selectedVerse = null
                                }
                        )
                    }
                    // Remove highlight
                    Surface(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .clickable {
                                viewModel.removeHighlight(verse)
                                selectedVerse = null
                            },
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Clear", modifier = Modifier.size(16.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(12.dp))

                // Actions List
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    // Bookmark
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            viewModel.toggleBookmark(verse)
                            selectedVerse = null
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Bookmark, contentDescription = "Bookmark", tint = ChristianGold)
                        Text(text = "बुकमार्क", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    // Explanation
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            showExplanationDialog = verse
                            selectedVerse = null
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = "Explanation", tint = MaterialTheme.colorScheme.primary)
                        Text(text = "पद व्याख्या", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    // Personal Note
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            noteInputText = ""
                            showNoteDialog = verse
                            selectedVerse = null
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Note", tint = MaterialTheme.colorScheme.primary)
                        Text(text = "नोट लेख्ने", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    // Ask AI
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            viewModel.askAiTeacher("${verse.bookName} ${verse.chapter}:${verse.verseNumber} को विस्तृत बाइबलीय व्याख्या र आत्मिक अर्थ के हो?")
                            viewModel.navigateTo(AppScreen.AI_TEACHER)
                            selectedVerse = null
                        }
                    ) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = "AI", tint = ChristianGold)
                        Text(text = "AI सँग सोध्ने", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    // Share
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "${verse.bookName} ${verse.chapter}:${verse.verseNumber}\n\n\"${verse.nepaliText}\"\n\n— नेपाली बाइबल अध्ययन एप")
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Share Verse"))
                            selectedVerse = null
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Share", tint = MaterialTheme.colorScheme.primary)
                        Text(text = "साझेदारी", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // Dialog: Verse Explanation & Cross References
    showExplanationDialog?.let { verse ->
        AlertDialog(
            onDismissRequest = { showExplanationDialog = null },
            title = {
                Text(
                    text = "${verse.bookName} ${verse.chapter}:${verse.verseNumber} — व्याख्या",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            text = {
                Column {
                    Text(
                        text = "\"${verse.nepaliText}\"",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "सरल नेपाली व्याख्या:",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = ChristianGold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (verse.explanation.isNotBlank()) verse.explanation else "यस पदको आधारभूत व्याख्या छिट्टै अपडेट हुनेछ।",
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 22.sp
                    )
                    if (verse.crossReferences.isNotBlank()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "सम्बन्धित बाइबल पदहरू (Cross References):",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = verse.crossReferences,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showExplanationDialog = null }) {
                    Text("बन्द गर्नुहोस्")
                }
            }
        )
    }

    // Dialog: Add/Edit Personal Note
    showNoteDialog?.let { verse ->
        AlertDialog(
            onDismissRequest = { showNoteDialog = null },
            title = {
                Text(
                    text = "${verse.bookName} ${verse.chapter}:${verse.verseNumber} — व्यक्तिगत नोट",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "\"${verse.nepaliText}\"",
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = noteInputText,
                        onValueChange = { noteInputText = it },
                        label = { Text("तपाईंको आत्मिक मनन / टिप्पणी") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        maxLines = 5
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.saveVerseNote(verse, noteInputText)
                        showNoteDialog = null
                    }
                ) {
                    Text("सुरक्षित गर्नुहोस्")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNoteDialog = null }) {
                    Text("रद्द")
                }
            }
        )
    }
}
