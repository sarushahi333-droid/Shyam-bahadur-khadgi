package com.example.data.model

enum class Testament {
    OLD, NEW
}

enum class BibleCategoryGroup {
    LAW, HISTORY, POETRY, PROPHETS, GOSPEL, CHURCH_HISTORY, PAULINE_EPISTLES, GENERAL_EPISTLES, REVELATION
}

data class BibleBook(
    val id: Int,
    val nepaliName: String,
    val englishName: String,
    val testament: Testament,
    val categoryGroup: BibleCategoryGroup,
    val totalChapters: Int,
    val abbreviation: String
)

data class BibleVerse(
    val id: Long = 0,
    val bookId: Int,
    val bookName: String,
    val chapter: Int,
    val verseNumber: Int,
    val nepaliText: String,
    val englishText: String = "",
    val explanation: String = "",
    val crossReferences: List<String> = emptyList()
)

data class BibleLesson(
    val id: String,
    val categoryNameNepali: String,
    val categoryNameEnglish: String,
    val titleNepali: String,
    val titleEnglish: String,
    val topicIntro: String,
    val mainVerses: List<String>,
    val detailedExplanation: String,
    val realLifeExamples: String,
    val practicalApplications: String,
    val reflectionQuestions: List<String>,
    val closingPrayer: String
)

data class SermonOutline(
    val id: Long = 0,
    val title: String,
    val topic: String,
    val targetAudience: String,
    val durationMinutes: Int,
    val mainPassage: String,
    val introduction: String,
    val mainPoints: List<String>,
    val detailedExplanations: String,
    val supportingVerses: List<String>,
    val biblicalExamples: String,
    val modernExamples: String,
    val practicalApplications: String,
    val conclusion: String,
    val closingPrayer: String,
    val dateCreated: Long = System.currentTimeMillis()
)

data class DailyDevotional(
    val id: Int,
    val dayOfYear: Int,
    val title: String,
    val reference: String,
    val verseText: String,
    val explanation: String,
    val prayer: String,
    val encouragement: String
)

data class DownloadPackage(
    val id: String,
    val titleNepali: String,
    val titleEnglish: String,
    val sizeMb: Double,
    val description: String,
    val isDownloaded: Boolean = false,
    val downloadProgress: Float = 0f,
    val itemCount: Int = 0
)

enum class HighlightColor(val hex: Long, val nepaliName: String) {
    YELLOW(0xFFFFF176, "पहेंलो"),
    GREEN(0xFFA5D6A7, "हरियो"),
    BLUE(0xFF90CAF9, "नीलो"),
    ORANGE(0xFFFFCC80, "सुन्तला"),
    PINK(0xFFF48FB1, "गुलाबी")
}

data class UserBookmark(
    val id: Long = 0,
    val bookId: Int,
    val bookName: String,
    val chapter: Int,
    val verseNumber: Int,
    val verseText: String,
    val createdAt: Long = System.currentTimeMillis()
)

data class UserHighlight(
    val id: Long = 0,
    val bookId: Int,
    val chapter: Int,
    val verseNumber: Int,
    val colorHex: Long,
    val createdAt: Long = System.currentTimeMillis()
)

data class UserNote(
    val id: Long = 0,
    val bookId: Int,
    val bookName: String,
    val chapter: Int,
    val verseNumber: Int,
    val verseText: String,
    val noteContent: String,
    val updatedAt: Long = System.currentTimeMillis()
)

data class ReadingHistoryItem(
    val id: Long = 0,
    val bookId: Int,
    val bookName: String,
    val chapter: Int,
    val verseNumber: Int,
    val timestamp: Long = System.currentTimeMillis()
)
