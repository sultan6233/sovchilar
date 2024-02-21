package com.sovchilar.made.presentation.fragments.view

import android.os.Bundle
import android.util.Log
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
import com.appodeal.ads.Appodeal
import com.appodeal.ads.BannerCallbacks
import com.google.android.material.snackbar.Snackbar
import com.sovchilar.made.R
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
import sovchilar.uz.domain.models.remote.AdvertisementsModel

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
        initAdapter()
        loadBanner()
    }

    private fun loadBanner() = lifecycleScope.launch {
        if (Appodeal.isLoaded(Appodeal.BANNER_VIEW)) {
            Appodeal.show(requireActivity(), Appodeal.BANNER_VIEW)
        }
        Appodeal.setBannerCallbacks(object : BannerCallbacks {
            override fun onBannerLoaded(height: Int, isPrecache: Boolean) {
                if (Appodeal.isLoaded(Appodeal.BANNER_VIEW)) {
                    Appodeal.show(requireActivity(), Appodeal.BANNER_VIEW)
                }
            }

            override fun onBannerFailedToLoad() {
            }

            override fun onBannerShown() {
            }

            override fun onBannerShowFailed() {
            }

            override fun onBannerClicked() {
            }

            override fun onBannerExpired() {
            }

        })
    }

    override fun onPause() {
        Appodeal.hide(requireActivity(), Appodeal.BANNER_VIEW)
        super.onPause()
    }

    //    override fun onHiddenChanged(hidden: Boolean) {
//        if (!hidden) {
//            (requireActivity() as MainActivity).binding.bnvSovchilar.apply {
//                if (selectedItemId != R.id.advertisementFragment) {
//                    selectedItemId = R.id.advertisementFragment
//                }
//            }
//        }
//        super.onHiddenChanged(hidden)
//    }

    private fun initAdapter() {
        advertisementAdapter.addLoadStateListener { loadState ->
            binding.srRefresh.isRefreshing = loadState.source.refresh is LoadState.Loading
            // Handle errors
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Snackbar.make(binding.root, it.error.message.toString(), Snackbar.LENGTH_LONG)
                    .show()
            }
        }
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
            if (binding.btnFemale.isChecked) {
                viewModel.getAdvertisements(femaleGender)
            } else {
                viewModel.getAdvertisements(maleGender)
            }

        }
    }

    private fun submitAdapterList() {
        lifecycleScope.launch {
            viewModel.gender.observe(viewLifecycleOwner) {
                if (binding.btnFemale.isChecked) {
                    viewModel.getAdvertisements(femaleGender)
                } else {
                    viewModel.getAdvertisements(maleGender)
                }
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