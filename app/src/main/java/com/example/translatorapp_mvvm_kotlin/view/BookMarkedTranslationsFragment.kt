package com.example.translatorapp_mvvm_kotlin.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.translatorapp_mvvm_kotlin.adapters.BookmarksAdapter
import com.example.translatorapp_mvvm_kotlin.databinding.FragmentBookMarkedTranslationsBinding
import com.example.translatorapp_mvvm_kotlin.model.room.model.SavedTranslation
import com.example.translatorapp_mvvm_kotlin.viewModel.BookmarksViewModel

class BookMarkedTranslationsFragment : Fragment() {
    private lateinit var binding: FragmentBookMarkedTranslationsBinding
    private lateinit var bookmarksViewModel: BookmarksViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookMarkedTranslationsBinding.inflate(inflater)
        initUI()
        setBookmarkList()
        return binding.root
    }

    private fun initUI() {
        bookmarksViewModel = ViewModelProvider(this)[BookmarksViewModel::class.java]
        activity?.let { bookmarksViewModel.setRoomRepository(it) }
    }

    private fun setBookmarkList() {
        binding.progressCircular.visibility=View.VISIBLE
        bookmarksViewModel.allBookmarks.observe(viewLifecycleOwner) { listTranslation ->
            setAdapter(listTranslation)
        }
    }

    private fun setAdapter(savedTranslations: List<SavedTranslation>) {
        binding.progressCircular.visibility=View.GONE
        binding.bookmarkedTranslations.layoutManager = LinearLayoutManager(activity)
        val adapter = BookmarksAdapter(savedTranslations)
        binding.bookmarkedTranslations.adapter = adapter
    }
}