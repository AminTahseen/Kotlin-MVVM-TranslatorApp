package com.example.translatorapp_mvvm_kotlin.model.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.translatorapp_mvvm_kotlin.model.room.model.SavedTranslation

@Dao
interface SavedTranslationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun saveTranslation(translation: SavedTranslation)

    @Query("Select * from savedTranslation order by savedTranslation_id ASC")
    fun getSavedTranslations(): LiveData<List<SavedTranslation>>
}