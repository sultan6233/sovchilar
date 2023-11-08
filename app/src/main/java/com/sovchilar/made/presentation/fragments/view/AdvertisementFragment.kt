package com.sovchilar.made.presentation.fragments.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sovchilar.made.data.remote.ApiService
import com.sovchilar.made.databinding.FragmentAdvertisementBinding
import com.sovchilar.made.domain.models.AdvertisementsModel
import com.sovchilar.made.domain.usecases.AdvertisementsFixUseCase
import com.sovchilar.made.presentation.fragments.view.adapter.AdvertisementAdapter
import com.sovchilar.made.presentation.viewmodel.AdvertisementViewModel
import com.sovchilar.made.presentation.viewmodel.MainViewModel
import com.sovchilar.made.uitls.femaleGender
import com.sovchilar.made.uitls.maleGender
import com.sovchilar.made.uitls.utils.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    private fun getList() {
        lifecycleScope.launch {
            viewModel.advertisements.observe(viewLifecycleOwner) {
                viewModel.advertisementsList = it as ArrayList<AdvertisementsModel>?
                if (viewModel.gender.value == null) {
                    viewModel.gender.value = femaleGender
                }
                when {
                    binding.srRefresh.isRefreshing -> binding.srRefresh.isRefreshing = false
                }
            }
        }
    }

    private fun initClicks() {
        binding.btnMale.setOnClickListener {
            viewModel.gender.value = maleGender
        }
        binding.btnFemale.setOnClickListener {
            viewModel.gender.value = femaleGender
        }
        binding.srRefresh.setOnRefreshListener {
            viewModel.getAdvertisements()
        }
    }


    private fun submitAdapterList() {
        lifecycleScope.launch {
            viewModel.gender.observe(viewLifecycleOwner) { gender ->
                viewModel.advertisementsList?.let {
                    advertisementAdapter.differ.submitList(
                        advertisementsFixUseCase.getFixGenderDividedAdvertisements(
                            it, gender
                        )
                    )
                }

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