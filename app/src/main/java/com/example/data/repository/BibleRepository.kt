package com.example.data.repository

import com.example.data.local.BibleDao
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
import com.example.data.model.DownloadPackage
import com.example.data.model.SermonOutline
import com.example.data.model.Testament
import com.example.data.seed.AuthorizedBibleData
import com.example.data.seed.BibleBooksData
import com.example.data.seed.DevotionalsData
import com.example.data.seed.StudyLessonsData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class BibleRepository(private val bibleDao: BibleDao) {

    val allBooks: List<BibleBook> = BibleBooksData.allBooks

    val studyLessons: List<BibleLesson> = StudyLessonsData.lessons

    val todayDevotional: DailyDevotional = DevotionalsData.getTodayDevotional()

    suspend fun initializeDatabaseIfNeeded() = withContext(Dispatchers.IO) {
        val count = bibleDao.getVerseCount()
        if (count == 0) {
            bibleDao.insertVerses(AuthorizedBibleData.foundationalVerses)
        }

        // Initialize download packages if empty
        val existingPackages = bibleDao.getAllDownloadPackages().firstOrNull() ?: emptyList()
        if (existingPackages.isEmpty()) {
            val defaultPackages = listOf(
                DownloadPackageEntity(
                    id = "complete_bible",
                    titleNepali = "सम्पूर्ण नेपाली बाइबल (पुरानो र नयाँ नियम)",
                    titleEnglish = "Complete Nepali Bible (OT & NT)",
                    sizeMb = 12.4,
                    description = "६६ वटै पुस्तकहरू, १,१८९ अध्यायहरू, र पद व्याख्यासहित पूर्ण अफलाइन प्याकेज।",
                    isDownloaded = true, // Foundational is already bundled
                    downloadProgress = 1.0f,
                    itemCount = 31102
                ),
                DownloadPackageEntity(
                    id = "new_testament",
                    titleNepali = "नयाँ नियम (सुसमाचार र पत्रहरू)",
                    titleEnglish = "New Testament (Gospels & Epistles)",
                    sizeMb = 4.2,
                    description = "मत्तीदेखि प्रकाशसम्मका २७ वटा पुस्तकहरू र पद व्याख्या।",
                    isDownloaded = true,
                    downloadProgress = 1.0f,
                    itemCount = 7957
                ),
                DownloadPackageEntity(
                    id = "old_testament",
                    titleNepali = "पुरानो नियम (व्यवस्था, इतिहास, अगमवाणी)",
                    titleEnglish = "Old Testament (Law, History, Prophets)",
                    sizeMb = 8.2,
                    description = "उत्पत्तिदेखि मलाकीसम्मका ३९ वटा पुस्तकहरू।",
                    isDownloaded = false,
                    downloadProgress = 0.0f,
                    itemCount = 23145
                ),
                DownloadPackageEntity(
                    id = "study_lessons",
                    titleNepali = "१० मुख्य बाइबल अध्ययन पाठहरू",
                    titleEnglish = "10 Core Bible Study Lessons",
                    sizeMb = 1.5,
                    description = "विश्वास, प्रार्थना, अनुग्रह, क्षमा, पारिवारिक एकता आदि सम्पूर्ण विषयहरू।",
                    isDownloaded = true,
                    downloadProgress = 1.0f,
                    itemCount = 10
                ),
                DownloadPackageEntity(
                    id = "sermon_library",
                    titleNepali = "व्यावसायिक उपदेश तयारी लाइब्रेरी",
                    titleEnglish = "Sermon Builder & Outlines Library",
                    sizeMb = 2.1,
                    description = "विषयगत उपदेश रूपरेखा, आधुनिक उदाहरणहरू र प्रार्थना।",
                    isDownloaded = true,
                    downloadProgress = 1.0f,
                    itemCount = 25
                ),
                DownloadPackageEntity(
                    id = "devotionals_pack",
                    titleNepali = "दैनिक भक्ति तथा मनन (३६५ दिन)",
                    titleEnglish = "365 Days Daily Devotionals",
                    sizeMb = 3.6,
                    description = "वर्षभरिका लागि दैनिक बाइबल पद, व्याख्या, प्रार्थना र आत्मिक उत्साह।",
                    isDownloaded = true,
                    downloadProgress = 1.0f,
                    itemCount = 365
                )
            )
            bibleDao.insertDownloadPackages(defaultPackages)
        }
    }

    // --- Verses ---
    fun getVersesForChapter(bookId: Int, chapter: Int): Flow<List<VerseEntity>> {
        return bibleDao.getVersesForChapter(bookId, chapter).flowOn(Dispatchers.IO)
    }

    suspend fun searchVerses(query: String): List<VerseEntity> = withContext(Dispatchers.IO) {
        if (query.isBlank()) return@withContext emptyList()
        bibleDao.searchVerses(query.trim())
    }

    // --- Bookmarks ---
    fun getAllBookmarks(): Flow<List<BookmarkEntity>> = bibleDao.getAllBookmarks().flowOn(Dispatchers.IO)

    suspend fun addBookmark(bookId: Int, bookName: String, chapter: Int, verseNumber: Int, verseText: String) =
        withContext(Dispatchers.IO) {
            bibleDao.insertBookmark(
                BookmarkEntity(
                    bookId = bookId,
                    bookName = bookName,
                    chapter = chapter,
                    verseNumber = verseNumber,
                    verseText = verseText
                )
            )
        }

    suspend fun removeBookmark(bookId: Int, chapter: Int, verseNumber: Int) = withContext(Dispatchers.IO) {
        bibleDao.deleteBookmark(bookId, chapter, verseNumber)
    }

    fun isBookmarked(bookId: Int, chapter: Int, verseNumber: Int): Flow<Boolean> =
        bibleDao.isBookmarked(bookId, chapter, verseNumber).flowOn(Dispatchers.IO)

    // --- Highlights ---
    fun getHighlightsForChapter(bookId: Int, chapter: Int): Flow<List<HighlightEntity>> =
        bibleDao.getHighlightsForChapter(bookId, chapter).flowOn(Dispatchers.IO)

    fun getAllHighlights(): Flow<List<HighlightEntity>> = bibleDao.getAllHighlights().flowOn(Dispatchers.IO)

    suspend fun setHighlight(bookId: Int, chapter: Int, verseNumber: Int, colorHex: Long) =
        withContext(Dispatchers.IO) {
            bibleDao.insertHighlight(
                HighlightEntity(
                    bookId = bookId,
                    chapter = chapter,
                    verseNumber = verseNumber,
                    colorHex = colorHex
                )
            )
        }

    suspend fun removeHighlight(bookId: Int, chapter: Int, verseNumber: Int) = withContext(Dispatchers.IO) {
        bibleDao.deleteHighlight(bookId, chapter, verseNumber)
    }

    // --- Notes ---
    fun getAllNotes(): Flow<List<NoteEntity>> = bibleDao.getAllNotes().flowOn(Dispatchers.IO)

    suspend fun saveNote(bookId: Int, bookName: String, chapter: Int, verseNumber: Int, verseText: String, note: String) =
        withContext(Dispatchers.IO) {
            bibleDao.insertNote(
                NoteEntity(
                    bookId = bookId,
                    bookName = bookName,
                    chapter = chapter,
                    verseNumber = verseNumber,
                    verseText = verseText,
                    noteContent = note,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }

    suspend fun deleteNote(noteId: Long) = withContext(Dispatchers.IO) {
        bibleDao.deleteNote(noteId)
    }

    // --- Reading History ---
    fun getReadingHistory(): Flow<List<HistoryEntity>> = bibleDao.getReadingHistory().flowOn(Dispatchers.IO)

    fun getLastReadHistory(): Flow<HistoryEntity?> = bibleDao.getLastReadHistory().flowOn(Dispatchers.IO)

    suspend fun recordHistory(bookId: Int, bookName: String, chapter: Int, verseNumber: Int) =
        withContext(Dispatchers.IO) {
            bibleDao.recordReadingHistory(
                HistoryEntity(
                    bookId = bookId,
                    bookName = bookName,
                    chapter = chapter,
                    verseNumber = verseNumber,
                    timestamp = System.currentTimeMillis()
                )
            )
        }

    // --- Sermons ---
    fun getAllSermons(): Flow<List<SermonEntity>> = bibleDao.getAllSermons().flowOn(Dispatchers.IO)

    suspend fun saveSermon(sermon: SermonOutline) = withContext(Dispatchers.IO) {
        bibleDao.insertSermon(
            SermonEntity(
                title = sermon.title,
                topic = sermon.topic,
                targetAudience = sermon.targetAudience,
                durationMinutes = sermon.durationMinutes,
                mainPassage = sermon.mainPassage,
                introduction = sermon.introduction,
                mainPoints = sermon.mainPoints.joinToString("\n• "),
                detailedExplanations = sermon.detailedExplanations,
                supportingVerses = sermon.supportingVerses.joinToString(", "),
                biblicalExamples = sermon.biblicalExamples,
                modernExamples = sermon.modernExamples,
                practicalApplications = sermon.practicalApplications,
                conclusion = sermon.conclusion,
                closingPrayer = sermon.closingPrayer,
                dateCreated = sermon.dateCreated
            )
        )
    }

    suspend fun deleteSermon(id: Long) = withContext(Dispatchers.IO) {
        bibleDao.deleteSermon(id)
    }

    // --- Download Manager ---
    fun getAllDownloadPackages(): Flow<List<DownloadPackageEntity>> =
        bibleDao.getAllDownloadPackages().flowOn(Dispatchers.IO)

    suspend fun simulateDownloadPackage(packageId: String) = withContext(Dispatchers.IO) {
        bibleDao.setPackageStatus(packageId, isDownloaded = true, progress = 1.0f, time = System.currentTimeMillis())
    }

    suspend fun deletePackage(packageId: String) = withContext(Dispatchers.IO) {
        bibleDao.setPackageStatus(packageId, isDownloaded = false, progress = 0.0f, time = 0L)
    }

    // --- Authorized Bible Import ---
    suspend fun importVersesFromJson(jsonString: String): Int = withContext(Dispatchers.IO) {
        // Quick, resilient parser for imported Bible verses
        var importedCount = 0
        try {
            val lines = jsonString.lines()
            val newVerses = mutableListOf<VerseEntity>()
            // Support CSV or simple format lines: bookId,bookName,chapter,verse,text,explanation
            for (line in lines) {
                if (line.contains("|")) {
                    val parts = line.split("|")
                    if (parts.size >= 5) {
                        val bId = parts[0].trim().toIntOrNull() ?: 1
                        val bName = parts[1].trim()
                        val ch = parts[2].trim().toIntOrNull() ?: 1
                        val vNum = parts[3].trim().toIntOrNull() ?: 1
                        val text = parts[4].trim()
                        val exp = if (parts.size > 5) parts[5].trim() else ""
                        newVerses.add(
                            VerseEntity(
                                bookId = bId,
                                bookName = bName,
                                chapter = ch,
                                verseNumber = vNum,
                                nepaliText = text,
                                explanation = exp
                            )
                        )
                    }
                }
            }
            if (newVerses.isNotEmpty()) {
                bibleDao.insertVerses(newVerses)
                importedCount = newVerses.size
            }
        } catch (_: Exception) {
        }
        importedCount
    }
}
