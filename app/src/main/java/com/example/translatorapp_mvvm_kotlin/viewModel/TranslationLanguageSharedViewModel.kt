package com.example.translatorapp_mvvm_kotlin.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.translatorapp_mvvm_kotlin.model.Constants


class TranslationLanguageSharedViewModel : ViewModel() {
    private val selectedTo = MutableLiveData(0)
    private val selectedFrom = MutableLiveData(0)

    private val selectedToCode = MutableLiveData(Constants.countryCodeList[0])
    private val selectedToText = MutableLiveData(Constants.countryNameList[0])

    private val selectedFromCode = MutableLiveData(Constants.countryCodeList[0])
    private val selectedFromText = MutableLiveData(Constants.countryNameList[0])

    fun selectTo(item: Int) {
        selectedTo.value = item
    }

    fun selectToCodeAndText(code: String, text: String) {
        selectedToCode.value = code
        selectedToText.value = text
    }

    fun selectFromCodeAndText(code: String, text: String) {
        selectedFromCode.value = code
        selectedFromText.value = text
    }

    fun getSelectedToCode(): LiveData<String> {
        return selectedToCode
    }

    fun getSelectedFromCode(): LiveData<String> {
        return selectedFromCode
    }

    fun getSelectedToText(): LiveData<String> {
        return selectedToText
    }

    fun getSelectedFromText(): LiveData<String> {
        return selectedFromText
    }

    fun getSelectedTo(): LiveData<Int>? {
        return selectedTo
    }

    fun selectFrom(item: Int) {
        selectedFrom.value = item
    }

    fun getSelectedFrom(): LiveData<Int>? {
        return selectedFrom
    }
    fun exchangeLanguage(){
        /*
        * selectedTo, selectedFrom
        * selectedToCode selectedFromCode
        * selectedToText selectedFromText
        * */
        val selectedToVal=selectedTo.value
        val selectedToCodeVal=selectedToCode.value
        val selectedToTextVal=selectedToText.value

        selectedTo.value=selectedFrom.value
        selectedToCode.value=selectedFromCode.value
        selectedToText.value=selectedFromText.value

        selectedFrom.value=selectedToVal
        selectedFromCode.value=selectedToCodeVal
        selectedFromText.value=selectedToTextVal


    }
}