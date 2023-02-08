package com.example.translatorapp_mvvm_kotlin.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
import com.example.translatorapp_mvvm_kotlin.model.room.model.SavedTranslation
import com.example.translatorapp_mvvm_kotlin.viewModel.LanguagesViewModel
import com.example.translatorapp_mvvm_kotlin.viewModel.TranslationLanguageSharedViewModel
import com.example.translatorapp_mvvm_kotlin.viewModel.TranslationViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import java.util.*

class TranslationFragment : Fragment() {
    private lateinit var binding: FragmentTranslationBinding
    private lateinit var sharedViewModel: TranslationLanguageSharedViewModel
    private lateinit var viewModel: TranslationViewModel
    private lateinit var languageViewModel: LanguagesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
        activity?.let { viewModel.setRoomRepository(it) }
        languageViewModel = ViewModelProvider(this)[LanguagesViewModel::class.java]
        binding.textFrom.addTextChangedListener {
            viewModel.setTextToTranslate(it.toString())
        }
        lifecycleScope.launchWhenStarted {
            viewModel.message.collectLatest {
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
                        setSelectedSpeechColor(fromSpeech = "Default".lowercase())
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
            binding.languageFromHeader.text = it
        }
        sharedViewModel.getSelectedToText().observe(viewLifecycleOwner) {
            binding.translateTextTo.text = it
            binding.languageToHeader.text = it
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
            setSelectedSpeechColor(fromSpeech = "From".lowercase())
            val selectedLanguageCode = sharedViewModel.getSelectedFromCode()
            val typedText = viewModel.getTextToTranslate().value
            typedText?.let { it1 ->
                languageViewModel.passDataToSpeech(
                    selectedLanguageCode.value.toString(), it1
                )
            }
        }
        binding.speechTo.setOnClickListener {
            setSelectedSpeechColor(fromSpeech = "To".lowercase())
            val selectedLanguageCode = sharedViewModel.getSelectedToCode()
            val translatedText = viewModel.getTranslatedText().value
            translatedText?.let { it1 ->
                languageViewModel.passDataToSpeech(
                    selectedLanguageCode.value.toString(), it1
                )
            }
        }
        binding.fromMainLinearCopy.setOnClickListener {
            val textToCopy = binding.textFrom.text.toString()
            copyText(textToCopy)
        }
        binding.toMainLinearCopy.setOnClickListener {
            val textToCopy = binding.textTo.text.toString()
            copyText(textToCopy)
        }
        binding.micSpeak.setOnClickListener {
            speechToText()
        }
        binding.exchangeTranslations.setOnClickListener {
            sharedViewModel.exchangeLanguage()
        }
        binding.saveTranslation.setOnClickListener {
            val savedTranslation = SavedTranslation(
                translationTextFrom = binding.languageFromHeader.text.toString(),
                translationTextTo = binding.languageToHeader.text.toString(),
                translationTextFrom_code = sharedViewModel.getSelectedFromCode().value,
                translationTextTo_Code =sharedViewModel.getSelectedToCode().value,
                translationTextFromText = viewModel.getTextToTranslate().value,
                translationTextToText = viewModel.getTranslatedText().value
            )
            viewModel.bookmarkTranslation(savedTranslation)
        }
    }

    private fun speechToText() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        // on below line we are passing language model
        // and model free form in our intent
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )

        // on below line we are passing our
        // language as a default language.
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            Locale.getDefault()
        )

        // on below line we are specifying a prompt
        // message as speak to text on below line.
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")

        // on below line we are specifying a try catch block.
        // in this block we are calling a start activity
        // for result method and passing our result code.
        try {
            resultLauncher.launch(intent)
        } catch (e: Exception) {
            // on below line we are displaying error message in toast
            Toast
                .makeText(
                    activity, " " + e.message,
                    Toast.LENGTH_SHORT
                )
                .show()
        }
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                // There are no request codes
                val data: Intent? = result.data
                val res: ArrayList<String> =
                    data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>

                // on below line we are setting data
                // to our output text view.
                binding.textFrom.setText(
                    Objects.requireNonNull(res)[0]
                )
            }
        }

    private fun copyText(textToCopy: String) {
        activity?.applicationContext?.let { it1 -> viewModel.copyText(textToCopy, it1) }
    }

    private fun setSelectedSpeechColor(fromSpeech: String) {
        when (fromSpeech) {
            "From".lowercase() -> {
                // from section Colors
                binding.fromMainLinear.setBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.purple_500)
                )
                binding.languageFromHeader.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.white)
                )
                DrawableCompat.setTint(
                    DrawableCompat.wrap(binding.fromMainLinearCopy.drawable),
                    ContextCompat.getColor(requireContext(), R.color.white)
                )
                binding.textFrom.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.white)
                )

                // to section Colors
                binding.toMainLinear.setBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.white)
                )
                binding.languageToHeader.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.black)
                )
                DrawableCompat.setTint(
                    DrawableCompat.wrap(binding.toMainLinearCopy.drawable),
                    ContextCompat.getColor(requireContext(), R.color.greyColor)
                )
                binding.textTo.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.black)
                )

            }
            "To".lowercase() -> {
                // to section Colors
                binding.toMainLinear.setBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.purple_500)
                )
                binding.languageToHeader.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.white)
                )
                DrawableCompat.setTint(
                    DrawableCompat.wrap(binding.toMainLinearCopy.drawable),
                    ContextCompat.getColor(requireContext(), R.color.white)
                )
                binding.textTo.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.white)
                )

                // from section Colors
                binding.fromMainLinear.setBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.white)
                )
                binding.languageFromHeader.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.black)
                )
                DrawableCompat.setTint(
                    DrawableCompat.wrap(binding.fromMainLinearCopy.drawable),
                    ContextCompat.getColor(requireContext(), R.color.greyColor)
                )
                binding.textFrom.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.black)
                )
            }
            else -> {
                // from section Colors
                binding.fromMainLinear.setBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.white)
                )
                binding.languageFromHeader.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.black)
                )
                DrawableCompat.setTint(
                    DrawableCompat.wrap(binding.fromMainLinearCopy.drawable),
                    ContextCompat.getColor(requireContext(), R.color.greyColor)
                )
                binding.textFrom.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.black)
                )

                // to section Colors
                binding.toMainLinear.setBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.white)
                )
                binding.languageToHeader.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.black)
                )
                DrawableCompat.setTint(
                    DrawableCompat.wrap(binding.toMainLinearCopy.drawable),
                    ContextCompat.getColor(requireContext(), R.color.greyColor)
                )
                binding.textTo.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.black)
                )

            }
        }
    }
}