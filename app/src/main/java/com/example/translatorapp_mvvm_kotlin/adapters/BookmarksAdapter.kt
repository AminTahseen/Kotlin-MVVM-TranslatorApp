package com.example.translatorapp_mvvm_kotlin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.translatorapp_mvvm_kotlin.databinding.BookmarkTranslationItemBinding
import com.example.translatorapp_mvvm_kotlin.model.room.model.SavedTranslation

class BookmarksAdapter(
    private val bookmarkList:List<SavedTranslation>,
) : RecyclerView.Adapter<BookmarksAdapter.BookmarksHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarksHolder {
        val view = BookmarkTranslationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookmarksHolder(view)
    }

    override fun onBindViewHolder(holder: BookmarksHolder, position: Int) {
        val bookmarkItem=bookmarkList[position]
        holder.viewBinding.translateTextFrom.text=bookmarkItem.translationTextFrom
        holder.viewBinding.translateTextTo.text=bookmarkItem.translationTextTo
        holder.viewBinding.textToTranslate.text=bookmarkItem.translationTextFromText
        holder.viewBinding.translatedText.text=bookmarkItem.translationTextToText
    }

    override fun getItemCount(): Int {
        return bookmarkList.size
    }

    inner class BookmarksHolder(val viewBinding: BookmarkTranslationItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root)
}