package com.example.translatorapp_mvvm_kotlin.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class TranslationLanguageSharedViewModel : ViewModel() {
    private val selectedTo = MutableLiveData(0)
    private val selectedFrom = MutableLiveData(0)

    private val selectedToCode = MutableLiveData("en-GB")
    private val selectedToText = MutableLiveData("English")

    private val selectedFromCode = MutableLiveData("en-GB")
    private val selectedFromText = MutableLiveData("English")

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

    fun getSelectedToCode():LiveData<String>{
        return selectedToCode
    }

    fun getSelectedFromCode():LiveData<String>{
        return selectedFromCode
    }

    fun getSelectedToText():LiveData<String> {
        return selectedToText
    }

    fun getSelectedFromText():LiveData<String> {
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
}