package com.example.translatorapp_mvvm_kotlin.model.dto


data class TextToSpeechResponseDTO(
    val protocol:String?="",
    val code:Int?=0,
    val message:String?="",
    val url:String?=""
)