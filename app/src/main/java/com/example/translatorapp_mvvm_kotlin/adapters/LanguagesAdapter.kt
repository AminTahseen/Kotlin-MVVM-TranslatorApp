package com.example.translatorapp_mvvm_kotlin.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.translatorapp_mvvm_kotlin.R
import com.example.translatorapp_mvvm_kotlin.databinding.LanguagesItemBinding
import com.example.translatorapp_mvvm_kotlin.model.LanguageModel
import com.example.translatorapp_mvvm_kotlin.viewModel.TranslationLanguageSharedViewModel

class LanguagesAdapter(
    private val context: Context?,
    private val languageList: MutableList<LanguageModel>,
    private val sharedViewModel: TranslationLanguageSharedViewModel,
    private val selectedPositionFor: String = "From",
    private val findNavController: NavController,
) : RecyclerView.Adapter<LanguagesAdapter.LanguageHolder>() {
    private var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageHolder {
        val view = LanguagesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LanguageHolder(view)
    }

    override fun onBindViewHolder(holder: LanguageHolder, position: Int) {
        holder.viewBinding.textView2.text = languageList[position].languageText
        selectedPosition = when(selectedPositionFor){
            "From"-> sharedViewModel.getSelectedFrom()?.value!!
            else-> sharedViewModel.getSelectedTo()?.value!!
        }
        when (selectedPosition) {
            position -> {
                holder.viewBinding.mainItemHolder.setBackgroundResource(R.color.redLIGHT)
                holder.viewBinding.textView2.setTextColor(
                    ContextCompat.getColor(
                        context!!,
                        R.color.white
                    )
                )
            }
            else -> {
                holder.viewBinding.mainItemHolder.setBackgroundResource(R.color.white)
                holder.viewBinding.textView2.setTextColor(
                    ContextCompat.getColor(
                        context!!,
                        R.color.black
                    )
                )
            }
        }
        holder.viewBinding.mainItemHolder.setOnClickListener {
            Log.d("SELECTEDiS",selectedPosition.toString())
            setSelectedItem(position)
        }
    }

    private fun setSelectedItem(position: Int) {
        selectedPosition = position
        when (selectedPositionFor) {
            "From" ->{
                sharedViewModel.selectFrom(position)
                sharedViewModel.selectFromCodeAndText(
                    languageList[position].languageTextCode,
                    languageList[position].languageText
                )
            }
            else ->{ sharedViewModel.selectTo(position)
                sharedViewModel.selectToCodeAndText(
                    languageList[position].languageTextCode,
                    languageList[position].languageText
                )
            }
        }
        notifyDataSetChanged()
        findNavController.popBackStack()
    }

    override fun getItemCount(): Int {
        return languageList.size
    }

    inner class LanguageHolder(val viewBinding: LanguagesItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root)
}