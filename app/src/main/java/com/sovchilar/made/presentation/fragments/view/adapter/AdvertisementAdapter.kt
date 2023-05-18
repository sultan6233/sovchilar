package com.sovchilar.made.presentation.fragments.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sovchilar.made.R
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
            binding.tvProvidedAge.text = binding.tvProvidedAge.context.getString(
                R.string.years, advertisementsModel.age.toString()
            )
            binding.tvProvidedNationality.text = advertisementsModel.nationality
            binding.tvProvidedMarriageStatusAndChildren.text = String.format(
                binding.tvProvidedMarriageStatusAndChildren.context.getString(R.string.provided_marriage_status_and_children),
                advertisementsModel.marriageStatus,
                advertisementsModel.children.lowercase()
            )
            binding.tvProvidedAgeRestriction.text = String.format(
                binding.tvProvidedAgeRestriction.context.getString(R.string.provided_age_restriction),
                advertisementsModel.fromAge.toString(),
                advertisementsModel.tillAge.toString()
            )
            binding.tvProvidedTelegram.text = advertisementsModel.telegram?.lowercase()
            binding.tvProvidedCountryAndCity.text = String.format(
                binding.tvProvidedCountryAndCity.context.getString(R.string.provided_country_and_city),
                advertisementsModel.city,
                advertisementsModel.country
            )
            binding.tvProvidedMoreInfo.text = advertisementsModel.moreInfo
            binding.tvProvidedTelegram.setOnClickListener {
                openTelegramUseCase.openUserPage(
                    it.context, binding.tvProvidedTelegram.text.toString()
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