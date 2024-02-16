package com.sovchilar.made.presentation.fragments.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sovchilar.made.databinding.ItemLanguageBinding
import com.sovchilar.made.presentation.fragments.language.Language

class ChangeLanguageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val differCallback = object : DiffUtil.ItemCallback<Language.Supported>() {

        override fun areItemsTheSame(
            oldItem: Language.Supported,
            newItem: Language.Supported,
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: Language.Supported,
            newItem: Language.Supported,
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        val binding =
            ItemLanguageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    inner class MainViewHolder(private val binding: ItemLanguageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(language: Language.Supported) {
            binding.rbLaunguage.isSelected = language.selected
            binding.rbLaunguage.text = language.name
            binding.rbLaunguage.setOnCheckedChangeListener { compoundButton, isChecked ->
                language.selected = isChecked
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MainViewHolder) {
            val foodItem = differ.currentList[position]
            holder.bind(foodItem)
        }

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}