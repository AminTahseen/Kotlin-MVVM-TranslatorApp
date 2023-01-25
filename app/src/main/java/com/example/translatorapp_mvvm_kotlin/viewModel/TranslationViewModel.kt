package com.example.translatorapp_mvvm_kotlin.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.translatorapp_mvvm_kotlin.model.apiService.APIInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TranslationViewModel(
    private var apiInterface: APIInterface=APIInterface.getInstance()
):ViewModel() {
    private val textToTranslate: MutableLiveData<String> = MutableLiveData("")
    private val loading: MutableLiveData<Boolean> = MutableLiveData(false)
    private val errorMessage: MutableLiveData<String> = MutableLiveData("")

    private val translatedText: MutableLiveData<String> = MutableLiveData("")


    fun setTextToTranslate(value:String){
        textToTranslate.value=value
    }
    fun getTextToTranslate():LiveData<String>{
        return textToTranslate
    }
    fun getTranslatedText():LiveData<String>{
        return translatedText
    }
    fun getLoading():LiveData<Boolean>{
        return loading
    }
    fun getErrorMessage():LiveData<String>{
        return errorMessage
    }
    fun translateText(langFrom:String,langTo:String){
        when(textToTranslate.value){
            ""-> errorMessage.value="Text cannot be empty !"
            else->{
                viewModelScope.launch(Dispatchers.IO) {
                    val langPair="$langFrom|$langTo"
                    getTranslateText(langPair)
                }
            }
        }
    }
    private suspend fun getTranslateText(langPair:String){
        loading.postValue(true)
        val response=apiInterface.getTranslatedText(textToTranslate.value.toString(),langPair)
        if(response.isSuccessful){
            Log.d("Response","success")
            Log.d("Response", response.body().toString())
            translatedText.postValue(response.body()?.responseData?.translatedText)
            loading.postValue(false)

        }else{
            Log.d("Response","failed")
            Log.d("Response",response.message())
            loading.postValue(false)

        }
    }
}