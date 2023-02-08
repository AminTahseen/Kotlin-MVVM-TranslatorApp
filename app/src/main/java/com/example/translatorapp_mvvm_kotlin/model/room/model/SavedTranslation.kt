package com.example.translatorapp_mvvm_kotlin.model.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "savedTranslation")
data class SavedTranslation(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "savedTranslation_id")
    val id: Int?=null,

    @ColumnInfo(name = "savedTranslation_from")
    val translationTextFrom: String?,

    @ColumnInfo(name = "savedTranslation_to")
    val translationTextTo: String?,

    @ColumnInfo(name = "savedTranslation_from_text")
    val translationTextFromText: String?,

    @ColumnInfo(name = "savedTranslation_to_text")
    val translationTextToText: String?,

    @ColumnInfo(name = "savedTranslation_from_code")
    val translationTextFrom_code: String?,

    @ColumnInfo(name = "savedTranslation_to_code")
    val translationTextTo_Code: String?,

    )
