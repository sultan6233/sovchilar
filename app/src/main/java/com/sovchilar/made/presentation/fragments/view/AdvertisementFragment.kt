package com.sovchilar.made.presentation.fragments.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.google.android.material.snackbar.Snackbar
import com.sovchilar.made.databinding.FragmentAdvertisementBinding
import com.sovchilar.made.presentation.fragments.view.adapter.AdvertisementAdapter
import com.sovchilar.made.presentation.mappers.AdvertisementsModelMapper
import com.sovchilar.made.presentation.usecases.BaseFragment
import com.sovchilar.made.presentation.viewmodel.AdvertisementViewModel
import com.sovchilar.made.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import sovchilar.uz.comm.femaleGender
import sovchilar.uz.comm.maleGender
import sovchilar.uz.domain.utils.DataState

@AndroidEntryPoint
class AdvertisementFragment :
    BaseFragment<FragmentAdvertisementBinding>(FragmentAdvertisementBinding::inflate) {

    private val viewModel: AdvertisementViewModel by viewModels()
    private val activityViewModel by activityViewModels<MainViewModel>()
    private val advertisementAdapter = AdvertisementAdapter()
    private val advertisementsModelMapper by lazy { AdvertisementsModelMapper(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getList()
        initRecyclerView()
        initClicks()
        submitAdapterList()
        observerAdapterInsertion()
    }

    private fun getList() {
        lifecycleScope.launch {
            viewModel.advertisements.observe(viewLifecycleOwner) { dataState ->
                when (dataState) {
                    is DataState.Loading -> binding.srRefresh.isRefreshing = true
                    is DataState.Success -> {
                        if (viewModel.gender.value == null) {
                            viewModel.gender.value = femaleGender
                        } else {
                            viewModel.gender.value = viewModel.gender.value
                        }
                        viewModel.advertisementsList = dataState.data.personals as ArrayList
                    }

                    is DataState.Error -> Snackbar.make(
                        requireView(),
                        dataState.exception.message.toString(),
                        Snackbar.LENGTH_LONG
                    ).show()
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
            binding.rvAdvertisement.smoothScrollToPosition(0)
        }
        binding.btnFemale.setOnClickListener {
            viewModel.gender.value = femaleGender
            binding.rvAdvertisement.smoothScrollToPosition(0)
        }
        binding.srRefresh.setOnRefreshListener {
            viewModel.getAdvertisements()
        }
    }

    private fun submitAdapterList() {
        lifecycleScope.launch {
            viewModel.gender.observe(viewLifecycleOwner) {
                viewModel.advertisementsList?.let {
                    advertisementAdapter.differ.submitList(
                        advertisementsModelMapper.mapToAdvertisementModelPresentation(
                            it
                        )
                    )
                }
            }
        }
    }

    private fun scrollToFirst() {
        val position =
            (binding.rvAdvertisement.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
        if (position < 1 && position != RecyclerView.NO_POSITION) {
            binding.rvAdvertisement.scrollToPosition(0)
        }
    }

    private fun observerAdapterInsertion() {
        binding.rvAdvertisement.itemAnimator = object : DefaultItemAnimator() {
            override fun canReuseUpdatedViewHolder(
                viewHolder: RecyclerView.ViewHolder,
                payloads: List<Any>
            ): Boolean {
                return true
            }
        }
        advertisementAdapter.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0) {
                    scrollToFirst()
                    // advertisementAdapter.unregisterAdapterDataObserver(this)
                }
            }
        })
    }

    private fun initRecyclerView() {
        lifecycleScope.launch {
            binding.rvAdvertisement.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            binding.rvAdvertisement.adapter = advertisementAdapter
        }
    }

}