package com.example.translatorapp_mvvm_kotlin.viewModel

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.translatorapp_mvvm_kotlin.model.Constants
import com.example.translatorapp_mvvm_kotlin.model.LanguageModel
import com.example.translatorapp_mvvm_kotlin.model.server.apiService.TextToSpeechAPIInterface
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch


class LanguagesViewModel : ViewModel() {
    var languageListLive: MutableLiveData<MutableList<LanguageModel>> = MutableLiveData()
    private var apiInterface: TextToSpeechAPIInterface = TextToSpeechAPIInterface.getInstance()
    private val _loading=MutableSharedFlow<Boolean>()
    val loading=_loading.asSharedFlow()

    private val _errorMessage= MutableSharedFlow<String>()
    val errorMessage=_errorMessage.asSharedFlow()

    init {
        setLanguages()
    }

    private fun setLanguages() {
        val list = ArrayList<LanguageModel>()

        for (item in Constants.countryNameList.indices) {
            val languageModel =
                LanguageModel(Constants.countryNameList[item], Constants.countryCodeList[item])
            list.add(languageModel)
        }
        languageListLive.value = list
    }

    fun passDataToSpeech(languageCode: String, textToPass: String) {
        Log.d("DataSendAre", "$languageCode and $textToPass")
        viewModelScope.launch {
            getTextToSpeech(languageCode, textToPass)
        }
    }

    private suspend fun getTextToSpeech(languageCode: String, textToPass: String) {
        _loading.emit(true)
        val response = apiInterface.getTextToSpeech(
            text = textToPass,
            language_code = languageCode,
            fileType = "mp3",
            speed = -4,
            key = Constants.TEXT_TO_SPEECH_API_KEY,
            rapidApiKey = Constants.TEXT_TO_SPEECH_RAPID_KEY,
            b64 = false,
        )
        try {
            if (response.isSuccessful && response != null) {
                _loading.emit(false)
                Log.d("textToSpeechResponse", "success")
                Log.d("textToSpeechResponse", response.toString())
                response.body()?.string()?.let {
                    if (it.contains("ERROR:")) {
                        Log.d("textToSpeechResponse", it)
                        _errorMessage.emit(it)
                    } else {
                        val url =
                            "https://voicerss-text-to-speech.p.rapidapi.com/?key=${Constants.TEXT_TO_SPEECH_API_KEY}&rapidapi-key=${Constants.TEXT_TO_SPEECH_RAPID_KEY}&b64=false&src=$textToPass&hl=$languageCode&c=mp3&r=-4"
                        playAudio(url)
                    }
                }

            } else {
                _loading.emit(false)
                Log.d("textToSpeechResponse", "failed")
                Log.d("textToSpeechResponse", response.message())
                _errorMessage.emit("An Error Occurred")
            }
        } catch (e: JsonSyntaxException) {
            _loading.emit(false)
            Log.d("textToSpeechResponse", "catch")
            Log.d("textToSpeechResponse", e.toString())

        }

    }

    private fun playAudio(url: String) {
        val mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(url)
            prepare() // might take long! (for buffering, etc)
            start()
        }
    }
}

