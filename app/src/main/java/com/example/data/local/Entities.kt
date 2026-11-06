package com.example.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "verses",
    indices = [
        Index(value = ["bookId", "chapter", "verseNumber"], unique = true),
        Index(value = ["bookId", "chapter"]),
        Index(value = ["nepaliText"])
    ]
)
data class VerseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val bookId: Int,
    val bookName: String,
    val chapter: Int,
    val verseNumber: Int,
    val nepaliText: String,
    val englishText: String = "",
    val explanation: String = "",
    val crossReferences: String = "" // Comma-separated
)

@Entity(tableName = "bookmarks", indices = [Index(value = ["bookId", "chapter", "verseNumber"], unique = true)])
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val bookId: Int,
    val bookName: String,
    val chapter: Int,
    val verseNumber: Int,
    val verseText: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "highlights", indices = [Index(value = ["bookId", "chapter", "verseNumber"], unique = true)])
data class HighlightEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val bookId: Int,
    val chapter: Int,
    val verseNumber: Int,
    val colorHex: Long,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val bookId: Int,
    val bookName: String,
    val chapter: Int,
    val verseNumber: Int,
    val verseText: String,
    val noteContent: String,
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "reading_history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val bookId: Int,
    val bookName: String,
    val chapter: Int,
    val verseNumber: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "sermons")
data class SermonEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val topic: String,
    val targetAudience: String,
    val durationMinutes: Int,
    val mainPassage: String,
    val introduction: String,
    val mainPoints: String, // Delimited or JSON string
    val detailedExplanations: String,
    val supportingVerses: String,
    val biblicalExamples: String,
    val modernExamples: String,
    val practicalApplications: String,
    val conclusion: String,
    val closingPrayer: String,
    val dateCreated: Long = System.currentTimeMillis()
)

@Entity(tableName = "download_packages")
data class DownloadPackageEntity(
    @PrimaryKey
    val id: String,
    val titleNepali: String,
    val titleEnglish: String,
    val sizeMb: Double,
    val description: String,
    val isDownloaded: Boolean = false,
    val downloadProgress: Float = 0f,
    val itemCount: Int = 0,
    val lastDownloadedAt: Long = 0L
)
