package com.sovchilar.made.presentation.fragments.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sovchilar.made.databinding.ItemAdvertisementBinding
import com.sovchilar.made.domain.models.AdvertisementsFixedModel
import com.sovchilar.made.domain.usecases.OpenTelegramUseCase

class AdvertisementAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val openTelegramUseCase = OpenTelegramUseCase()

    private val differCallback = object : DiffUtil.ItemCallback<AdvertisementsFixedModel>() {

        override fun areItemsTheSame(
            oldItem: AdvertisementsFixedModel,
            newItem: AdvertisementsFixedModel,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: AdvertisementsFixedModel,
            newItem: AdvertisementsFixedModel,
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
            ItemAdvertisementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    inner class MainViewHolder(private val binding: ItemAdvertisementBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(advertisementsModel: AdvertisementsFixedModel) {
            binding.tvProvidedName.text = advertisementsModel.name
            binding.tvProvidedAge.text = advertisementsModel.age.toString()
            binding.tvProvidedNationality.text = advertisementsModel.nationality
            binding.tvProvidedMarriageStatus.text = advertisementsModel.marriageStatus
            binding.tvProvidedChildren.text = advertisementsModel.children
            binding.tvProvidedAgeRestrictionFrom.text = advertisementsModel.fromAge.toString()
            binding.tvProvidedAgeRestrictionTo.text = advertisementsModel.tillAge.toString()
            binding.tvProvidedTelegram.text = advertisementsModel.telegram
            binding.tvProvidedCity.text = advertisementsModel.city
            binding.tvProvidedCountry.text = advertisementsModel.country
            binding.tvProvidedMoreInfo.text = advertisementsModel.moreInfo
            binding.tvProvidedGender.text = advertisementsModel.gender

            binding.tvProvidedTelegram.setOnClickListener {
                openTelegramUseCase.openUserPage(
                    it.context,
                    binding.tvProvidedTelegram.text.toString()
                )
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