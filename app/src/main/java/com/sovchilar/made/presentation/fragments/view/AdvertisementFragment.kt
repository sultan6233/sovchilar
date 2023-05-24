package com.sovchilar.made.presentation.fragments.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sovchilar.made.databinding.FragmentAdvertisementBinding
import com.sovchilar.made.domain.models.AdvertisementsModel
import com.sovchilar.made.domain.usecases.AdvertisementsFixUseCase
import com.sovchilar.made.presentation.fragments.view.adapter.AdvertisementAdapter
import com.sovchilar.made.presentation.fragments.viewmodel.AdvertisementViewModel
import com.sovchilar.made.presentation.fragments.viewmodel.MainViewModel
import com.sovchilar.made.uitls.femaleGender
import com.sovchilar.made.uitls.maleGender
import com.sovchilar.made.uitls.utils.BaseFragment
import kotlinx.coroutines.launch

class AdvertisementFragment :
    BaseFragment<FragmentAdvertisementBinding>(FragmentAdvertisementBinding::inflate) {

    private val viewModel: AdvertisementViewModel by viewModels()
    private val activityViewModel by activityViewModels<MainViewModel>()
    private val advertisementAdapter = AdvertisementAdapter()
    private val advertisementsFixUseCase: AdvertisementsFixUseCase by lazy {
        AdvertisementsFixUseCase(
            requireContext()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getList()
        initRecyclerView()
        initClicks()
        submitAdapterList()
    }

    private fun initClicks() {
        if (viewModel.gender.value == maleGender) {
            binding.rbFemale.isChecked = false
            binding.rbMale.isChecked = true
        } else {
            binding.rbFemale.isChecked = true
            binding.rbMale.isChecked = false
        }

        binding.rgGender.setOnCheckedChangeListener { _, checkedId ->
            if (binding.rbMale.id == checkedId) {
                viewModel.gender.value = maleGender
            } else {
                viewModel.gender.value = femaleGender
            }
        }
    }

    private fun getList() {
        lifecycleScope.launch {
            viewModel.advertisements.observe(viewLifecycleOwner) {
                viewModel.advertisementsList = it as ArrayList<AdvertisementsModel>
                if (viewModel.gender.value == null) {
                    viewModel.gender.value = femaleGender
                }
            }
        }
    }

    private fun submitAdapterList() {
        lifecycleScope.launch {
            viewModel.gender.observe(viewLifecycleOwner) {
                advertisementAdapter.differ.submitList(
                    advertisementsFixUseCase.getFixGenderDividedAdvertisements(
                        viewModel.advertisementsList, it
                    )
                )
            }
        }
    }

    private fun initRecyclerView() {
        lifecycleScope.launch {
            binding.rvAdvertisement.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            binding.rvAdvertisement.adapter = advertisementAdapter
        }
    }

}