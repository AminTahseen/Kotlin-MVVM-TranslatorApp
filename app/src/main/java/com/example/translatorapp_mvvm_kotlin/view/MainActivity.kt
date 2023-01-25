package com.example.translatorapp_mvvm_kotlin.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.translatorapp_mvvm_kotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setOnClickListeners()

    }
    private fun setOnClickListeners(){
        binding.continueBTN.setOnClickListener {
            finish()
            val translationActivity = Intent(MainActivity@this,TranslationActivity::class.java)
            startActivity(translationActivity)
        }
    }

}