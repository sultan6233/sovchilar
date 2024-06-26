package com.sovchilar.made.presentation.fragments.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sovchilar.made.CustomLogger
import com.sovchilar.made.R
import com.sovchilar.made.databinding.ItemAdvertisementBinding
import sovchilar.uz.domain.models.remote.AdvertisementModelPresentation

class AdvertisementAdapter :
    PagingDataAdapter<AdvertisementModelPresentation, AdvertisementAdapter.MainViewHolder>(diffCallback = differCallback) {
    companion object {
        private const val VIEW_TYPE_DEFAULT = 1
        val differCallback = object : DiffUtil.ItemCallback<AdvertisementModelPresentation>() {

            override fun areItemsTheSame(
                oldItem: AdvertisementModelPresentation,
                newItem: AdvertisementModelPresentation,
            ): Boolean {
                return oldItem.telegram == newItem.telegram
            }

            override fun areContentsTheSame(
                oldItem: AdvertisementModelPresentation,
                newItem: AdvertisementModelPresentation,
            ): Boolean {
                return oldItem.equals(newItem)
            }
        }
    }

    private val openTelegramUseCase by lazy { sovchilar.uz.domain.usecases.OpenTelegramUseCase() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = ItemAdvertisementBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MainViewHolder(binding)
    }

    inner class MainViewHolder(private val binding: ItemAdvertisementBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(advertisementsModel: AdvertisementModelPresentation) {
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
                CustomLogger.log("telegramTextClick", "true")
                openTelegramUseCase.openUserPage(
                    it.context, binding.tvProvidedTelegram.text.toString(), binding.root
                )
            }
            binding.ivTelegram.setOnClickListener {
                CustomLogger.log("telegramImageClick", "true")
                openTelegramUseCase.openUserPage(
                    it.context, binding.tvProvidedTelegram.text.toString(), binding.root
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE_DEFAULT
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}