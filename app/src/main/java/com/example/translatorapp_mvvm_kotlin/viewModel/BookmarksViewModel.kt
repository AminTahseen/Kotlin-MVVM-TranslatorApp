package com.example.translatorapp_mvvm_kotlin.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.translatorapp_mvvm_kotlin.model.room.TranslationDatabase
import com.example.translatorapp_mvvm_kotlin.model.room.model.SavedTranslation
import com.example.translatorapp_mvvm_kotlin.model.room.repository.SavedTranslationRepository

class BookmarksViewModel : ViewModel() {
    private lateinit var repository: SavedTranslationRepository
    lateinit var allBookmarks: LiveData<List<SavedTranslation>>

    fun setRoomRepository(context: Context) {
        val dao = TranslationDatabase.getDatabase(context).getSavedTranslationDao()
        repository = SavedTranslationRepository(dao)
        allBookmarks = repository.getAllSavedTranslation()
    }


}