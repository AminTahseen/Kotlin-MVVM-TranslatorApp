package com.example.translatorapp_mvvm_kotlin.view

import android.graphics.Color
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.translatorapp_mvvm_kotlin.R
import com.example.translatorapp_mvvm_kotlin.databinding.FragmentTranslationBinding
import com.example.translatorapp_mvvm_kotlin.viewModel.LanguagesViewModel
import com.example.translatorapp_mvvm_kotlin.viewModel.TranslationLanguageSharedViewModel
import com.example.translatorapp_mvvm_kotlin.viewModel.TranslationViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest

class TranslationFragment : Fragment() {
    private lateinit var binding: FragmentTranslationBinding
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

    private fun initUI() {
        sharedViewModel =
            ViewModelProviders.of(requireActivity())[TranslationLanguageSharedViewModel::class.java]
        viewModel = ViewModelProvider(this)[TranslationViewModel::class.java]
        languageViewModel = ViewModelProvider(this)[LanguagesViewModel::class.java]
        binding.textFrom.addTextChangedListener {
            viewModel.setTextToTranslate(it.toString())
        }
        lifecycleScope.launchWhenStarted {
            viewModel.errorMessage.collectLatest {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
            }
        }
        lifecycleScope.launchWhenStarted {
            languageViewModel.errorMessage.collectLatest {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
            }
        }
        lifecycleScope.launchWhenStarted {
            languageViewModel.loading.collectLatest {
                when (it) {
                    true -> {
                    }
                    else -> {
                        binding.speechTo.imageAlpha =
                            255 // 0 being transparent and 255 being opaque
                        binding.speechTo.isEnabled = true
                        binding.speechFrom.imageAlpha =
                            255 // 0 being transparent and 255 being opaque
                        binding.speechFrom.isEnabled = true
                        setImageButtonColor(binding.speechFrom, R.color.purple_500)
                        setImageButtonColor(binding.speechTo, R.color.purple_500)

                    }
                }
            }
        }
        viewModel.getLoading().observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    binding.progressCircular.visibility = View.VISIBLE
                    binding.translateBTN.visibility = View.GONE
                }
                else -> {
                    binding.progressCircular.visibility = View.GONE
                    binding.translateBTN.visibility = View.VISIBLE
                }
            }
        }
        viewModel.getTranslatedText().observe(viewLifecycleOwner) {
            if (it != "") binding.textTo.text = it
        }
    }

    private fun setChangeListeners() {
        sharedViewModel.getSelectedFromText().observe(viewLifecycleOwner) {
            binding.translateTextFrom.text = it
        }
        sharedViewModel.getSelectedToText().observe(viewLifecycleOwner) {
            binding.translateTextTo.text = it
        }
    }

    private fun setOnClickListeners() {
        binding.translateTextFrom.setOnClickListener {
            val directions =
                TranslationFragmentDirections.actionTranslationFragmentToLanguagesFragment()
            directions.translateFromTo = "From"
            findNavController().navigate(directions)

        }
        binding.translateTextTo.setOnClickListener {
            val directions =
                TranslationFragmentDirections.actionTranslationFragmentToLanguagesFragment()
            directions.translateFromTo = "To"
            findNavController().navigate(directions)
        }
        binding.translateBTN.setOnClickListener {
            viewModel.translateText(
                sharedViewModel.getSelectedFromCode().value.toString(),
                sharedViewModel.getSelectedToCode().value.toString()
            )
        }
        binding.speechFrom.setOnClickListener {
            binding.speechTo.imageAlpha = 75 // 0 being transparent and 255 being opaque
            binding.speechTo.isEnabled = false

            setImageButtonColor(binding.speechFrom, R.color.teal_700)
            val selectedLanguageCode = sharedViewModel.getSelectedFromCode()
            val typedText = viewModel.getTextToTranslate().value
            typedText?.let { it1 ->
                languageViewModel.passDataToSpeech(
                    selectedLanguageCode.value.toString(),
                    it1
                )
            }
        }
        binding.speechTo.setOnClickListener {
            binding.speechFrom.imageAlpha = 75 // 0 being transparent and 255 being opaque
            binding.speechFrom.isEnabled = false
            setImageButtonColor(binding.speechTo, R.color.teal_700)
            val selectedLanguageCode = sharedViewModel.getSelectedToCode()
            val translatedText =viewModel.getTranslatedText().value
            translatedText?.let { it1 ->
                languageViewModel.passDataToSpeech(
                    selectedLanguageCode.value.toString(),
                    it1
                )
            }
        }
    }

    private fun setImageButtonColor(imageButton: ImageButton, color: Int) {
        DrawableCompat.setTint(
            DrawableCompat.wrap(imageButton.drawable),
            ContextCompat.getColor(requireContext(), color)
        )

    }
}