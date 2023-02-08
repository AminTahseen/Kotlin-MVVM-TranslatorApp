package com.example.translatorapp_mvvm_kotlin.model.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.translatorapp_mvvm_kotlin.model.room.dao.SavedTranslationDao
import com.example.translatorapp_mvvm_kotlin.model.room.model.SavedTranslation

@Database(entities = [SavedTranslation::class], version = 1, exportSchema = false)
abstract class TranslationDatabase : RoomDatabase() {

    abstract fun getSavedTranslationDao(): SavedTranslationDao
    companion object {
        @Volatile
        private var mainInstance: TranslationDatabase? = null

        private val dbName = "translation_database.db"
        fun getDatabase(context: Context): TranslationDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            // here synchronised used for blocking the other thread
            // from accessing another while in a specific code execution.
            return mainInstance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, TranslationDatabase::class.java,
                    dbName
                ).build()
                mainInstance = instance
                // return instance
                instance
            }
        }
    }
}