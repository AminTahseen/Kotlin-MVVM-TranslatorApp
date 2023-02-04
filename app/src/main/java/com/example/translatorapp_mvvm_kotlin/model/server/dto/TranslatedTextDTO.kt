package com.example.translatorapp_mvvm_kotlin.model.server.dto

data class TranslatedTextDTO(
    val exception_code: Any,
    val mtLangSupported: Any,
    val quotaFinished: Boolean,
    val responderId: Any,
    val responseData: ResponseData,
    val responseDetails: String,
    val responseStatus: Int
)