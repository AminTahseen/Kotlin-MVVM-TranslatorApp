package com.example.translatorapp_mvvm_kotlin.model.server.dto

data class ResponseData(
    val match: Double,
    val translatedText: String
)