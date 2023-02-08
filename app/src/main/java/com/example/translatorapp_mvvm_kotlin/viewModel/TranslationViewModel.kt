package com.example.translatorapp_mvvm_kotlin.viewModel

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.translatorapp_mvvm_kotlin.model.room.TranslationDatabase
import com.example.translatorapp_mvvm_kotlin.model.room.model.SavedTranslation
import com.example.translatorapp_mvvm_kotlin.model.room.repository.SavedTranslationRepository
import com.example.translatorapp_mvvm_kotlin.model.server.apiService.APIInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch


class TranslationViewModel(
    private var apiInterface: APIInterface = APIInterface.getInstance(),
) : ViewModel() {
    private val textToTranslate: MutableLiveData<String> = MutableLiveData("")
    private val loading: MutableLiveData<Boolean> = MutableLiveData(false)
    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()
    private lateinit var repository: SavedTranslationRepository
    private val translatedText: MutableLiveData<String> = MutableLiveData("")

    fun setRoomRepository(context: Context) {
        val dao = TranslationDatabase.getDatabase(context).getSavedTranslationDao()
        repository = SavedTranslationRepository(dao)
    }

    fun setTextToTranslate(value: String) {
        textToTranslate.value = value
    }

    fun getTextToTranslate(): LiveData<String> {
        return textToTranslate
    }

    fun getTranslatedText(): LiveData<String> {
        return translatedText
    }

    fun getLoading(): LiveData<Boolean> {
        return loading
    }

    fun translateText(langFrom: String, langTo: String) {
        when (textToTranslate.value) {
            "" -> {
                viewModelScope.launch {
                    sendMessage("Text cannot be empty !")
                }
            }
            else -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val langPair = "$langFrom|$langTo"
                    getTranslateText(langPair)
                }
            }
        }
    }

    fun copyText(textToCopy: String, context: Context) {
        Log.d("CopiedText", textToCopy)
        var myClipboard =
            getSystemService(context, ClipboardManager::class.java) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText("copied text", textToCopy)
        myClipboard.setPrimaryClip(clip)
        viewModelScope.launch {
            sendMessage("Text copied to clipboard")
        }
    }

    private suspend fun sendMessage(message: String) {
        _message.emit(message)
    }

    private suspend fun getTranslateText(langPair: String) {
        loading.postValue(true)
        val response = apiInterface.getTranslatedText(textToTranslate.value.toString(), langPair)
        if (response.isSuccessful) {
            Log.d("Response", "success")
            Log.d("Response", response.body().toString())
            translatedText.postValue(response.body()?.responseData?.translatedText)
            loading.postValue(false)

        } else {
            Log.d("Response", "failed")
            Log.d("Response", response.message())
            loading.postValue(false)

        }
    }

    fun bookmarkTranslation(savedTranslation: SavedTranslation) {
        Log.d("savedTranslation", savedTranslation.toString())
        bookmarkTranslationSuspend(savedTranslation)

    }

    private fun bookmarkTranslationSuspend(savedTranslation: SavedTranslation) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertSavedTranslation(savedTranslation)
            sendMessage("Translation Bookmarked")
        }
    }
}