package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        VerseEntity::class,
        BookmarkEntity::class,
        HighlightEntity::class,
        NoteEntity::class,
        HistoryEntity::class,
        SermonEntity::class,
        DownloadPackageEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class BibleDatabase : RoomDatabase() {
    abstract fun bibleDao(): BibleDao

    companion object {
        @Volatile
        private var INSTANCE: BibleDatabase? = null

        fun getInstance(context: Context): BibleDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BibleDatabase::class.java,
                    "nepali_bible_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
