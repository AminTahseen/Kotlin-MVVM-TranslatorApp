package com.example.translatorapp_mvvm_kotlin.model.dto


data class TextToSpeechBodyObject(
    val src: String,
    val hl: String,
    val c: String,
    val r: Int,
)