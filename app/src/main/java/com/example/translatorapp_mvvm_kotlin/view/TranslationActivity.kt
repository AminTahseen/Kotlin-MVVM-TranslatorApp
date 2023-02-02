package com.example.translatorapp_mvvm_kotlin.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.translatorapp_mvvm_kotlin.R
import com.example.translatorapp_mvvm_kotlin.databinding.ActivityTranslationBinding

class TranslationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTranslationBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController:NavController
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTranslationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.fragmentContainerView)
        drawerLayout=binding.drawerLayout
        binding.navView.setupWithNavController(navController)

        appBarConfiguration= AppBarConfiguration(navController.graph,drawerLayout)

        setupActionBarWithNavController(navController,appBarConfiguration)

    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

}