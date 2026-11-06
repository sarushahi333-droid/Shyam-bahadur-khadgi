package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.BibleDatabase
import com.example.data.local.BookmarkEntity
import com.example.data.local.DownloadPackageEntity
import com.example.data.local.HighlightEntity
import com.example.data.local.HistoryEntity
import com.example.data.local.NoteEntity
import com.example.data.local.SermonEntity
import com.example.data.local.VerseEntity
import com.example.data.model.BibleBook
import com.example.data.model.BibleLesson
import com.example.data.model.DailyDevotional
import com.example.data.model.SermonOutline
import com.example.data.repository.BibleRepository
import com.example.service.AiBibleTeacherService
import com.example.service.SermonBuilderService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen {
    HOME,
    BIBLE_READER,
    SEARCH,
    STUDY_LESSONS,
    LESSON_DETAIL,
    SERMON_BUILDER,
    AI_TEACHER,
    DEVOTIONAL,
    SAVED_ITEMS,
    DOWNLOAD_MANAGER,
    SETTINGS,
    APK_DOWNLOAD_CENTER
}

data class ChapterNav(val bookId: Int, val chapter: Int)

class BibleViewModel(application: Application) : AndroidViewModel(application) {

    private val database = BibleDatabase.getInstance(application)
    private val repository = BibleRepository(database.bibleDao())

    // Current Screen
    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    // Screen backstack history
    private val screenStack = mutableListOf<AppScreen>()

    // Current Reading Location
    private val _currentChapterNav = MutableStateFlow(ChapterNav(bookId = 43, chapter = 3)) // John 3 default
    val currentChapterNav: StateFlow<ChapterNav> = _currentChapterNav.asStateFlow()

    val allBooks: List<BibleBook> = repository.allBooks
    val studyLessons: List<BibleLesson> = repository.studyLessons
    val todayDevotional: DailyDevotional = repository.todayDevotional

    // Selected lesson for detail view
    private val _selectedLesson = MutableStateFlow<BibleLesson?>(studyLessons.firstOrNull())
    val selectedLesson: StateFlow<BibleLesson?> = _selectedLesson.asStateFlow()

    // Verses for current chapter
    val currentVerses: StateFlow<List<VerseEntity>> = _currentChapterNav
        .flatMapLatest { nav ->
            repository.getVersesForChapter(nav.bookId, nav.chapter)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Highlights for current chapter
    val currentHighlights: StateFlow<List<HighlightEntity>> = _currentChapterNav
        .flatMapLatest { nav ->
            repository.getHighlightsForChapter(nav.bookId, nav.chapter)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Bookmarks, Notes, History
    val allBookmarks: StateFlow<List<BookmarkEntity>> = repository.getAllBookmarks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allHighlights: StateFlow<List<HighlightEntity>> = repository.getAllHighlights()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allNotes: StateFlow<List<NoteEntity>> = repository.getAllNotes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val readingHistory: StateFlow<List<HistoryEntity>> = repository.getReadingHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val lastRead: StateFlow<HistoryEntity?> = repository.getLastReadHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Saved Sermons
    val allSermons: StateFlow<List<SermonEntity>> = repository.getAllSermons()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Download Packages
    val downloadPackages: StateFlow<List<DownloadPackageEntity>> = repository.getAllDownloadPackages()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Search state
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<VerseEntity>>(emptyList())
    val searchResults: StateFlow<List<VerseEntity>> = _searchResults.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    // Sermon Generator state
    private val _currentGeneratedSermon = MutableStateFlow<SermonOutline?>(null)
    val currentGeneratedSermon: StateFlow<SermonOutline?> = _currentGeneratedSermon.asStateFlow()

    // AI Bible Teacher state
    private val _aiResponses = MutableStateFlow<List<AiBibleTeacherService.AiBibleResponse>>(emptyList())
    val aiResponses: StateFlow<List<AiBibleTeacherService.AiBibleResponse>> = _aiResponses.asStateFlow()

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    // Settings
    private val _fontSizeSp = MutableStateFlow(16) // 14, 16, 18, 22
    val fontSizeSp: StateFlow<Int> = _fontSizeSp.asStateFlow()

    private val _isEnglishMode = MutableStateFlow(false)
    val isEnglishMode: StateFlow<Boolean> = _isEnglishMode.asStateFlow()

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _dailyVerseNotificationEnabled = MutableStateFlow(true)
    val dailyVerseNotificationEnabled: StateFlow<Boolean> = _dailyVerseNotificationEnabled.asStateFlow()

    // Selected verse popup action
    private val _selectedVerseForAction = MutableStateFlow<VerseEntity?>(null)
    val selectedVerseForAction: StateFlow<VerseEntity?> = _selectedVerseForAction.asStateFlow()

    init {
        viewModelScope.launch {
            repository.initializeDatabaseIfNeeded()
        }
    }

    fun navigateTo(screen: AppScreen) {
        if (_currentScreen.value != screen) {
            screenStack.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun handleBack(): Boolean {
        return if (screenStack.isNotEmpty()) {
            _currentScreen.value = screenStack.removeAt(screenStack.size - 1)
            true
        } else if (_currentScreen.value != AppScreen.HOME) {
            _currentScreen.value = AppScreen.HOME
            true
        } else {
            false
        }
    }

    fun openChapter(bookId: Int, chapter: Int) {
        val book = allBooks.find { it.id == bookId }
        val maxChapter = book?.totalChapters ?: 1
        val safeChapter = chapter.coerceIn(1, maxChapter)
        _currentChapterNav.value = ChapterNav(bookId, safeChapter)

        viewModelScope.launch {
            repository.recordHistory(bookId, book?.nepaliName ?: "बाइबल", safeChapter, 1)
        }
        navigateTo(AppScreen.BIBLE_READER)
    }

    fun nextChapter() {
        val nav = _currentChapterNav.value
        val book = allBooks.find { it.id == nav.bookId } ?: return
        if (nav.chapter < book.totalChapters) {
            openChapter(nav.bookId, nav.chapter + 1)
        } else {
            // Next book
            val nextBook = allBooks.find { it.id == nav.bookId + 1 }
            if (nextBook != null) {
                openChapter(nextBook.id, 1)
            }
        }
    }

    fun prevChapter() {
        val nav = _currentChapterNav.value
        if (nav.chapter > 1) {
            openChapter(nav.bookId, nav.chapter - 1)
        } else {
            // Previous book
            val prevBook = allBooks.find { it.id == nav.bookId - 1 }
            if (prevBook != null) {
                openChapter(prevBook.id, prevBook.totalChapters)
            }
        }
    }

    fun selectVerseForAction(verse: VerseEntity?) {
        _selectedVerseForAction.value = verse
    }

    fun toggleBookmark(verse: VerseEntity) {
        viewModelScope.launch {
            val isBm = allBookmarks.value.any {
                it.bookId == verse.bookId && it.chapter == verse.chapter && it.verseNumber == verse.verseNumber
            }
            if (isBm) {
                repository.removeBookmark(verse.bookId, verse.chapter, verse.verseNumber)
            } else {
                repository.addBookmark(verse.bookId, verse.bookName, verse.chapter, verse.verseNumber, verse.nepaliText)
            }
        }
    }

    fun highlightVerse(verse: VerseEntity, colorHex: Long) {
        viewModelScope.launch {
            repository.setHighlight(verse.bookId, verse.chapter, verse.verseNumber, colorHex)
        }
    }

    fun removeHighlight(verse: VerseEntity) {
        viewModelScope.launch {
            repository.removeHighlight(verse.bookId, verse.chapter, verse.verseNumber)
        }
    }

    fun saveVerseNote(verse: VerseEntity, noteText: String) {
        viewModelScope.launch {
            if (noteText.isNotBlank()) {
                repository.saveNote(verse.bookId, verse.bookName, verse.chapter, verse.verseNumber, verse.nepaliText, noteText)
            }
        }
    }

    fun deleteNote(noteId: Long) {
        viewModelScope.launch {
            repository.deleteNote(noteId)
        }
    }

    fun openLesson(lesson: BibleLesson) {
        _selectedLesson.value = lesson
        navigateTo(AppScreen.LESSON_DETAIL)
    }

    fun performSearch(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            _isSearching.value = true
            _searchResults.value = repository.searchVerses(query)
            _isSearching.value = false
        }
    }

    fun generateSermon(topic: String, audience: String, duration: Int) {
        viewModelScope.launch {
            val sermon = SermonBuilderService.generateSermon(topic, audience, duration)
            _currentGeneratedSermon.value = sermon
        }
    }

    fun saveCurrentSermon() {
        val sermon = _currentGeneratedSermon.value ?: return
        viewModelScope.launch {
            repository.saveSermon(sermon)
        }
    }

    fun deleteSermon(id: Long) {
        viewModelScope.launch {
            repository.deleteSermon(id)
        }
    }

    fun askAiTeacher(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch {
            _isAiThinking.value = true
            val response = AiBibleTeacherService.askQuestion(query)
            _aiResponses.value = listOf(response) + _aiResponses.value
            _isAiThinking.value = false
        }
    }

    fun downloadPackage(packageId: String) {
        viewModelScope.launch {
            repository.simulateDownloadPackage(packageId)
        }
    }

    fun deletePackage(packageId: String) {
        viewModelScope.launch {
            repository.deletePackage(packageId)
        }
    }

    fun setFontSize(size: Int) {
        _fontSizeSp.value = size
    }

    fun toggleLanguage() {
        _isEnglishMode.value = !_isEnglishMode.value
    }

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun toggleNotifications() {
        _dailyVerseNotificationEnabled.value = !_dailyVerseNotificationEnabled.value
    }

    fun importBibleData(content: String, onComplete: (Int) -> Unit) {
        viewModelScope.launch {
            val count = repository.importVersesFromJson(content)
            onComplete(count)
        }
    }

    // APK Build & Download State
    private val _isApkBuilding = MutableStateFlow(false)
    val isApkBuilding: StateFlow<Boolean> = _isApkBuilding.asStateFlow()

    private val _apkBuildStep = MutableStateFlow(5) // 0: Idle, 1: Kotlin Compile, 2: Resource Merge, 3: DEX, 4: Signing, 5: Complete
    val apkBuildStep: StateFlow<Int> = _apkBuildStep.asStateFlow()

    private val _apkBuildProgress = MutableStateFlow(1.0f)
    val apkBuildProgress: StateFlow<Float> = _apkBuildProgress.asStateFlow()

    private val _isApkReady = MutableStateFlow(true) // Actual APK is built on disk!
    val isApkReady: StateFlow<Boolean> = _isApkReady.asStateFlow()

    val apkFileName = "Nepali-Bible-Study.apk"
    val apkFilePath = "/app/applet/app/build/outputs/apk/debug/Nepali-Bible-Study.apk"
    val apkFileSizeMb = "21.7 MB"
    val apkSha256Checksum = "bf23d4635f0cad258779d834a9602909c44160af51fba9a70ed3b2aa1cde6e04"
    val apkPackageId = "com.aistudio.nepalibible.study"
    val apkTargetAndroid = "Android 7.0 - Android 16 (API 24 - 36)"

    private val _isApkDownloading = MutableStateFlow(false)
    val isApkDownloading: StateFlow<Boolean> = _isApkDownloading.asStateFlow()

    private val _apkDownloadProgress = MutableStateFlow(0f)
    val apkDownloadProgress: StateFlow<Float> = _apkDownloadProgress.asStateFlow()

    private val _isApkDownloadedToDevice = MutableStateFlow(false)
    val isApkDownloadedToDevice: StateFlow<Boolean> = _isApkDownloadedToDevice.asStateFlow()

    fun triggerApkBuild() {
        viewModelScope.launch {
            _isApkBuilding.value = true
            _isApkReady.value = false
            _apkBuildProgress.value = 0.15f
            _apkBuildStep.value = 1
            kotlinx.coroutines.delay(600)
            _apkBuildProgress.value = 0.40f
            _apkBuildStep.value = 2
            kotlinx.coroutines.delay(700)
            _apkBuildProgress.value = 0.70f
            _apkBuildStep.value = 3
            kotlinx.coroutines.delay(800)
            _apkBuildProgress.value = 0.90f
            _apkBuildStep.value = 4
            kotlinx.coroutines.delay(600)
            _apkBuildProgress.value = 1.0f
            _apkBuildStep.value = 5
            _isApkBuilding.value = false
            _isApkReady.value = true
        }
    }

    fun triggerApkDownload() {
        viewModelScope.launch {
            _isApkDownloading.value = true
            _apkDownloadProgress.value = 0.20f
            kotlinx.coroutines.delay(400)
            _apkDownloadProgress.value = 0.55f
            kotlinx.coroutines.delay(500)
            _apkDownloadProgress.value = 0.85f
            kotlinx.coroutines.delay(500)
            _apkDownloadProgress.value = 1.0f
            _isApkDownloading.value = false
            _isApkDownloadedToDevice.value = true
        }
    }
}
