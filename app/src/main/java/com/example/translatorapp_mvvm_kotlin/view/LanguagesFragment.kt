package com.example.translatorapp_mvvm_kotlin.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.translatorapp_mvvm_kotlin.adapters.LanguagesAdapter
import com.example.translatorapp_mvvm_kotlin.databinding.FragmentLanguagesBinding
import com.example.translatorapp_mvvm_kotlin.model.LanguageModel
import com.example.translatorapp_mvvm_kotlin.viewModel.LanguagesViewModel
import com.example.translatorapp_mvvm_kotlin.viewModel.TranslationLanguageSharedViewModel


class LanguagesFragment : Fragment() {
    private lateinit var binding: FragmentLanguagesBinding
    private lateinit var viewModel: LanguagesViewModel
    private lateinit var sharedViewModel: TranslationLanguageSharedViewModel
    private lateinit var translateFromToArgs:String
    private lateinit var adapter:LanguagesAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLanguagesBinding.inflate(layoutInflater)
        getArgs()
        initUI()
        viewModel.languageListLive.observe(viewLifecycleOwner) {
            setAdapter(it)
        }
        return binding.root
    }
    private fun setAdapter(list: MutableList<LanguageModel>){
        binding.languageList.layoutManager= LinearLayoutManager(activity)
        adapter=LanguagesAdapter(context,list,sharedViewModel,
            translateFromToArgs,findNavController())
        binding.languageList.adapter=adapter

    }
    private fun initUI(){
        viewModel = ViewModelProvider(this)[LanguagesViewModel::class.java]
        sharedViewModel = ViewModelProviders.of(requireActivity())[TranslationLanguageSharedViewModel::class.java]
    }
    private fun getArgs(){
        val bundle = arguments
        if (bundle == null) {
            Log.e("Confirmation", "Fragment did not receive traveler information")
            return
        }
        val args = LanguagesFragmentArgs.fromBundle(bundle)
        translateFromToArgs=args.translateFromTo
    }
}