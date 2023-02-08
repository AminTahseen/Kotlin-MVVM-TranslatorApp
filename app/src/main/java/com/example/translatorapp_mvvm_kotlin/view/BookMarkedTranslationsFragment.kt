package com.example.translatorapp_mvvm_kotlin.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.translatorapp_mvvm_kotlin.R
import com.example.translatorapp_mvvm_kotlin.databinding.FragmentBookMarkedTranslationsBinding
import com.example.translatorapp_mvvm_kotlin.databinding.FragmentLanguagesBinding

class BookMarkedTranslationsFragment : Fragment() {
    private lateinit var binding: FragmentBookMarkedTranslationsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentBookMarkedTranslationsBinding.inflate(inflater)
        // Inflate the layout for this fragment

        return binding.root
    }
}