package com.sovchilar.made.presentation.fragments.view

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sovchilar.made.databinding.FragmentAdvertisementBinding
import com.sovchilar.made.domain.models.AdvertisementsModel
import com.sovchilar.made.domain.usecases.AdvertisementsFixUseCase
import com.sovchilar.made.presentation.activity.MainActivity
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
        layoutPadding()
        getList()
        initRecyclerView()
        initClicks()
        submitAdapterList()
    }

    private fun initClicks() {
        binding.btnMale.setOnClickListener {
            viewModel.gender.value = maleGender
        }
        binding.btnFemale.setOnClickListener {
            viewModel.gender.value = femaleGender
        }
    }

    private fun getList() {
        lifecycleScope.launch {
            viewModel.advertisements.observe(viewLifecycleOwner) {
                viewModel.advertisementsList = it as ArrayList<AdvertisementsModel>
                viewModel.gender.value = femaleGender
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

    private fun layoutPadding() {
        lifecycleScope.launch {
            activityViewModel.bottomHeight.observe(viewLifecycleOwner) {
                binding.rvAdvertisement.setPadding(
                    0, 0, 0, it * 4
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