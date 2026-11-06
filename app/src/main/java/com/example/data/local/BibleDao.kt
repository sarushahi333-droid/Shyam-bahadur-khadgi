package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BibleDao {

    // --- Verses ---
    @Query("SELECT * FROM verses WHERE bookId = :bookId AND chapter = :chapter ORDER BY verseNumber ASC")
    fun getVersesForChapter(bookId: Int, chapter: Int): Flow<List<VerseEntity>>

    @Query("SELECT * FROM verses WHERE bookId = :bookId AND chapter = :chapter ORDER BY verseNumber ASC")
    suspend fun getVersesForChapterSync(bookId: Int, chapter: Int): List<VerseEntity>

    @Query("SELECT * FROM verses WHERE nepaliText LIKE '%' || :query || '%' OR englishText LIKE '%' || :query || '%' OR bookName LIKE '%' || :query || '%' ORDER BY bookId, chapter, verseNumber ASC LIMIT 80")
    suspend fun searchVerses(query: String): List<VerseEntity>

    @Query("SELECT * FROM verses WHERE bookId = :bookId AND chapter = :chapter AND verseNumber = :verseNumber LIMIT 1")
    suspend fun getVerse(bookId: Int, chapter: Int, verseNumber: Int): VerseEntity?

    @Query("SELECT COUNT(*) FROM verses")
    suspend fun getVerseCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVerses(verses: List<VerseEntity>)

    // --- Bookmarks ---
    @Query("SELECT * FROM bookmarks ORDER BY createdAt DESC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity): Long

    @Query("DELETE FROM bookmarks WHERE bookId = :bookId AND chapter = :chapter AND verseNumber = :verseNumber")
    suspend fun deleteBookmark(bookId: Int, chapter: Int, verseNumber: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE bookId = :bookId AND chapter = :chapter AND verseNumber = :verseNumber)")
    fun isBookmarked(bookId: Int, chapter: Int, verseNumber: Int): Flow<Boolean>

    // --- Highlights ---
    @Query("SELECT * FROM highlights WHERE bookId = :bookId AND chapter = :chapter")
    fun getHighlightsForChapter(bookId: Int, chapter: Int): Flow<List<HighlightEntity>>

    @Query("SELECT * FROM highlights ORDER BY createdAt DESC")
    fun getAllHighlights(): Flow<List<HighlightEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHighlight(highlight: HighlightEntity)

    @Query("DELETE FROM highlights WHERE bookId = :bookId AND chapter = :chapter AND verseNumber = :verseNumber")
    suspend fun deleteHighlight(bookId: Int, chapter: Int, verseNumber: Int)

    // --- Notes ---
    @Query("SELECT * FROM notes ORDER BY updatedAt DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE bookId = :bookId AND chapter = :chapter AND verseNumber = :verseNumber LIMIT 1")
    suspend fun getNoteForVerse(bookId: Int, chapter: Int, verseNumber: Int): NoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

    @Query("DELETE FROM notes WHERE id = :noteId")
    suspend fun deleteNote(noteId: Long)

    // --- Reading History ---
    @Query("SELECT * FROM reading_history ORDER BY timestamp DESC LIMIT 30")
    fun getReadingHistory(): Flow<List<HistoryEntity>>

    @Query("SELECT * FROM reading_history ORDER BY timestamp DESC LIMIT 1")
    fun getLastReadHistory(): Flow<HistoryEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun recordReadingHistory(history: HistoryEntity)

    @Query("DELETE FROM reading_history")
    suspend fun clearHistory()

    // --- Sermons ---
    @Query("SELECT * FROM sermons ORDER BY dateCreated DESC")
    fun getAllSermons(): Flow<List<SermonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSermon(sermon: SermonEntity): Long

    @Query("DELETE FROM sermons WHERE id = :sermonId")
    suspend fun deleteSermon(sermonId: Long)

    // --- Download Packages ---
    @Query("SELECT * FROM download_packages")
    fun getAllDownloadPackages(): Flow<List<DownloadPackageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDownloadPackages(packages: List<DownloadPackageEntity>)

    @Update
    suspend fun updateDownloadPackage(pkg: DownloadPackageEntity)

    @Query("UPDATE download_packages SET isDownloaded = :isDownloaded, downloadProgress = :progress, lastDownloadedAt = :time WHERE id = :id")
    suspend fun setPackageStatus(id: String, isDownloaded: Boolean, progress: Float, time: Long)

    @Query("DELETE FROM verses WHERE bookId = :bookId")
    suspend fun deleteVersesByBook(bookId: Int)
}
