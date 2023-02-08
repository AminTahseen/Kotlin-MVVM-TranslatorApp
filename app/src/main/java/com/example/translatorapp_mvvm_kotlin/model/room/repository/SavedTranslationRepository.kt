package com.example.translatorapp_mvvm_kotlin.model.room.repository

import androidx.lifecycle.LiveData
import com.example.translatorapp_mvvm_kotlin.model.room.dao.SavedTranslationDao
import com.example.translatorapp_mvvm_kotlin.model.room.model.SavedTranslation

class SavedTranslationRepository(private val savedTranslationDao: SavedTranslationDao) {
    // get all the SavedTranslation
    fun getAllSavedTranslation(): LiveData<List<SavedTranslation>> =
        savedTranslationDao.getSavedTranslations()

    // adds an SavedTranslation to our database.
    fun insertSavedTranslation(savedTranslation: SavedTranslation) {
        savedTranslationDao.saveTranslation(savedTranslation)
    }
}