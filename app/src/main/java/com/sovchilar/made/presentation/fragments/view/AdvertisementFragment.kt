package com.sovchilar.made.presentation.fragments.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.map
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.google.android.material.snackbar.Snackbar
import com.sovchilar.made.R
import com.sovchilar.made.databinding.FragmentAdvertisementBinding
import com.sovchilar.made.presentation.activity.MainActivity
import com.sovchilar.made.presentation.fragments.view.adapter.AdvertisementAdapter
import com.sovchilar.made.presentation.mappers.AdvertisementsModelMapper
import com.sovchilar.made.presentation.usecases.BaseFragment
import com.sovchilar.made.presentation.viewmodel.AdvertisementViewModel
import com.sovchilar.made.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sovchilar.uz.comm.femaleGender
import sovchilar.uz.comm.maleGender
import sovchilar.uz.domain.models.remote.AdvertisementModelPresentation
import sovchilar.uz.domain.models.remote.AdvertisementsModel
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
        advertisementAdapter.addLoadStateListener { loadState ->
            binding.srRefresh.isRefreshing = loadState.source.refresh is LoadState.Loading

            // Handle errors
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Snackbar.make(binding.root, it.error.localizedMessage, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (!hidden) {
            (requireActivity() as MainActivity).binding.bnvSovchilar.apply {
                if (selectedItemId != R.id.advertisementFragment) {
                    selectedItemId = R.id.advertisementFragment
                }
            }
        }
        super.onHiddenChanged(hidden)
    }

    private fun getList() {
        viewModel.advertisements.observe(viewLifecycleOwner) {
            initList(it)
        }
    }

    private fun initList(pagingData: PagingData<AdvertisementsModel>) = lifecycleScope.launch {
        val advertisements = pagingData.map { ad ->
            advertisementsModelMapper.mapToAdvertisementModelPresentation(ad)
        }
        advertisementAdapter.submitData(lifecycle, advertisements).also {
            activityViewModel.dataReady.value = true
        }
    }

    private fun initClicks() = lifecycleScope.launch {
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
                //  viewModel.advertisementsList?.let {
//                    advertisementAdapter.differ.submitList(
//                        advertisementsModelMapper.mapToAdvertisementModelPresentation(
//                            it
//                        )
//                    )
                //       }
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