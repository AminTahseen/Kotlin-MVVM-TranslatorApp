package com.example.translatorapp_mvvm_kotlin.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.translatorapp_mvvm_kotlin.model.Constants
import com.example.translatorapp_mvvm_kotlin.model.LanguageModel
import com.example.translatorapp_mvvm_kotlin.model.apiService.TextToSpeechAPIInterface
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.launch


class LanguagesViewModel : ViewModel() {
    var languageListLive: MutableLiveData<MutableList<LanguageModel>> = MutableLiveData()
    private var apiInterface: TextToSpeechAPIInterface = TextToSpeechAPIInterface.getInstance()

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

    fun passDataToSpeech(languageCode:String,textToPass:String){
        Log.d("DataSendAre","$languageCode and $textToPass")
//        viewModelScope.launch {
//            getTextToSpeech(languageCode,textToPass)
//        }
    }
    private suspend fun getTextToSpeech(languageCode:String,textToPass:String) {
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
                Log.d("textToSpeechResponse", "success")
                Log.d("textToSpeechResponse", response.toString())
                val audioBytes = response.body()?.bytes()
                if (audioBytes != null) {
                    // write the bytes to a file or stream
                    Log.d("textToSpeechResponse", audioBytes.toString())

                }
            } else {
                Log.d("textToSpeechResponse", "failed")
                Log.d("textToSpeechResponse", response.message())
            }
        } catch (e: JsonSyntaxException) {
            Log.d("textToSpeechResponse", "catch")
            Log.d("textToSpeechResponse", e.toString())

        }

    }
}

