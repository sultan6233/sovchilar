package com.sovchilar.made.presentation.fragments.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sovchilar.made.databinding.ItemAdvertisementBinding
import com.sovchilar.made.domain.models.AdvertisementsModel

class AdvertisementAdapter:
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val differCallback = object : DiffUtil.ItemCallback<AdvertisementsModel>() {
        override fun areItemsTheSame(oldItem: AdvertisementsModel, newItem: AdvertisementsModel): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: AdvertisementsModel, newItem: AdvertisementsModel): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val binding = ItemAdvertisementBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MainViewHolder(binding)
    }
    inner class MainViewHolder(private val binding: ItemAdvertisementBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(advertisementsModel: AdvertisementsModel) {
            binding.tvProvidedName.text = advertisementsModel.name
            binding.tvProvidedAge.text = advertisementsModel.age.toString()
            binding.tvProvidedNationality.text = advertisementsModel.nationality
            binding.tvProvidedMarriageStatus.text = advertisementsModel.marriagestatus
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is MainViewHolder){
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