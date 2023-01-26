package com.example.translatorapp_mvvm_kotlin.view

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.translatorapp_mvvm_kotlin.databinding.FragmentTranslationBinding
import com.example.translatorapp_mvvm_kotlin.viewModel.LanguagesViewModel
import com.example.translatorapp_mvvm_kotlin.viewModel.TranslationLanguageSharedViewModel
import com.example.translatorapp_mvvm_kotlin.viewModel.TranslationViewModel
import com.google.android.material.snackbar.Snackbar

class TranslationFragment : Fragment() {
    private lateinit var binding:FragmentTranslationBinding
    private lateinit var sharedViewModel: TranslationLanguageSharedViewModel
    private lateinit var viewModel: TranslationViewModel
    private lateinit var languageViewModel: LanguagesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTranslationBinding.inflate(layoutInflater)
        initUI()
        setOnClickListeners()
        setChangeListeners()
        return binding.root
    }

    private fun initUI(){
        sharedViewModel = ViewModelProviders.of(requireActivity())[TranslationLanguageSharedViewModel::class.java]
        viewModel = ViewModelProvider(this)[TranslationViewModel::class.java]
        languageViewModel=ViewModelProvider(this)[LanguagesViewModel::class.java]
        binding.textFrom.addTextChangedListener{
          viewModel.setTextToTranslate(it.toString())
        }
        viewModel.getErrorMessage().observe(viewLifecycleOwner){
           if(it!="") Snackbar.make(binding.root, it.toString(), Snackbar.LENGTH_SHORT).show()
        }
        viewModel.getLoading().observe(viewLifecycleOwner){
            when(it){
                true->{
                    binding.progressCircular.visibility=View.VISIBLE
                    binding.translateBTN.visibility=View.GONE
                }
                else -> {
                    binding.progressCircular.visibility=View.GONE
                    binding.translateBTN.visibility=View.VISIBLE
                }
            }
        }
        viewModel.getTranslatedText().observe(viewLifecycleOwner){
            if(it!="") binding.textTo.text=it
        }
    }
    private fun setChangeListeners(){
        sharedViewModel.getSelectedFromText().observe(viewLifecycleOwner) {
            binding.translateTextFrom.text=it
        }
        sharedViewModel.getSelectedToText().observe(viewLifecycleOwner) {
            binding.translateTextTo.text=it
        }
    }
    private fun setOnClickListeners(){
        binding.translateTextFrom.setOnClickListener{
            val directions=TranslationFragmentDirections.actionTranslationFragmentToLanguagesFragment()
            directions.translateFromTo="From"
            findNavController().navigate(directions)

        }
        binding.translateTextTo.setOnClickListener{
            val directions=TranslationFragmentDirections.actionTranslationFragmentToLanguagesFragment()
            directions.translateFromTo="To"
            findNavController().navigate(directions)
        }
        binding.translateBTN.setOnClickListener {
            viewModel.translateText(
                sharedViewModel.getSelectedFromCode().value.toString(),
                sharedViewModel.getSelectedToCode().value.toString()
            )
        }
        binding.speechFrom.setOnClickListener {
            viewModel.getTextToTranslate().observe(viewLifecycleOwner){
                val selectedLanguageCode=sharedViewModel.getSelectedFromCode()
                val typedText=it
                languageViewModel.passDataToSpeech(
                    selectedLanguageCode.value.toString(),
                    typedText
                )
            }
        }
        binding.speechTo.setOnClickListener {

        }
    }
}