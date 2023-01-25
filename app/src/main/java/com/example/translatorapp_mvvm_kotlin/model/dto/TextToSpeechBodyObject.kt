package com.example.translatorapp_mvvm_kotlin.model.dto

import retrofit2.http.Field

data class TextToSpeechBodyObject(
    val src: String,
    val hl: String,
    val c: String,
    val r: Int,
)