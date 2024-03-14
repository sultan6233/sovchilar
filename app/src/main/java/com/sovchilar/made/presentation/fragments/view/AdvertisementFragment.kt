package com.sovchilar.made.presentation.fragments.view

import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.map
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.material.snackbar.Snackbar
import com.sovchilar.made.CustomLogger
import com.sovchilar.made.R
import com.sovchilar.made.databinding.FragmentAdvertisementBinding
import com.sovchilar.made.presentation.fragments.view.adapter.AdvertisementAdapter
import com.sovchilar.made.presentation.mappers.AdvertisementsModelMapper
import com.sovchilar.made.presentation.usecases.BaseFragment
import com.sovchilar.made.presentation.viewmodel.AdvertisementViewModel
import com.sovchilar.made.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.appmetrica.analytics.AdType
import kotlinx.coroutines.Dispatchers
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

    private val adapterDataObserver by lazy {
        object : AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0) {
                    scrollToFirst()
                }
            }
        }
    }

    private val adView by lazy { AdView(requireContext()) }
    private var initialLayoutComplete = false
    private val adSize: AdSize by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (activity != null) {
                val windowMetrics = requireActivity().windowManager.currentWindowMetrics
                val bounds = windowMetrics.bounds

                var adWidthPixels = binding.adView.width.toFloat()
                if (adWidthPixels == 0f) {
                    adWidthPixels = bounds.width().toFloat()
                }

                val density = resources.displayMetrics.density
                val adWidth = (adWidthPixels / density).toInt()

                AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                    requireContext(),
                    adWidth
                )
            } else {
                AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                    requireContext(),
                    0
                )
            }
        } else {
            val display = activity?.windowManager?.defaultDisplay
            val outMetrics = DisplayMetrics()
            display?.getMetrics(outMetrics)

            val density = outMetrics.density

            var adWidthPixels = binding.adView.width.toFloat()
            if (adWidthPixels == 0f) {
                adWidthPixels = outMetrics.widthPixels.toFloat()
            }

            val adWidth = (adWidthPixels / density).toInt()
            AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                requireContext(),
                adWidth
            )
        }
    }

    private val loadingStateListener: (CombinedLoadStates) -> Unit by lazy {
        { loadState ->
            binding.srRefresh.isRefreshing = loadState.source.refresh is LoadState.Loading
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Snackbar.make(binding.root, it.error.message.toString(), Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getList()
        initRecyclerView()
        initClicks()
        submitAdapterList()
        observerAdapterInsertion()
        initAdapter()
        initBanner()
    }

    private fun initBanner() {
        binding.adView.addView(adView)
        binding.adView.viewTreeObserver.addOnGlobalLayoutListener {
            if (!initialLayoutComplete) {
                initialLayoutComplete = true
                loadBanner()
            }
        }
    }

    private fun loadBanner() = lifecycleScope.launch {
        adView.adUnitId = getString(R.string.admob_banner_block_id)
        adView.setAdSize(adSize)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        adView.adListener = object : AdListener() {
            override fun onAdClicked() {}
            override fun onAdClosed() {}
            override fun onAdFailedToLoad(adError: LoadAdError) {
                CustomLogger.log("bannerFailedToLoad", adError.message)
            }

            override fun onAdImpression() {}
            override fun onAdLoaded() {
                initLevelAdRevenue()
                CustomLogger.log("bannerLoaded", "true")
            }

            override fun onAdOpened() {}
        }
    }

    private fun initLevelAdRevenue() {
        adView.setOnPaidEventListener {adValue->
            val revenue = adValue.valueMicros.toDouble() / 1_000_000
            val precision = adValue.precisionType
            val adUnitId = adView.adUnitId
            val loadedAdapterResponseInfo =
                adView.responseInfo?.loadedAdapterResponseInfo
            val adSourceName = loadedAdapterResponseInfo?.adSourceName ?: ""
            val adSourceInstanceName =
                loadedAdapterResponseInfo?.adSourceInstanceName ?: ""
            val adSourceInstanceId =
                loadedAdapterResponseInfo?.adSourceInstanceId ?: ""
            CustomLogger.logAdRevenue(
                revenue,
                adSourceName,
                adSourceInstanceId,
                adSourceInstanceName,
                AdType.REWARDED,
                adUnitId,
                "banner",
                precision.toString()
            )
        }
    }

    private fun initAdapter() = lifecycleScope.launch {
        advertisementAdapter.addLoadStateListener(loadingStateListener)
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
            }
        }
    }

    private fun scrollToFirst() = lifecycleScope.launch(Dispatchers.Main) {
        val position =
            (binding.rvAdvertisement.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
        if (position < 1 && position != RecyclerView.NO_POSITION) {
            binding.rvAdvertisement.scrollToPosition(0)
        }
    }

    private fun observerAdapterInsertion() {
        binding.rvAdvertisement.itemAnimator = object : DefaultItemAnimator() {
            override fun canReuseUpdatedViewHolder(
                viewHolder: RecyclerView.ViewHolder, payloads: List<Any>
            ): Boolean {
                return true
            }
        }
        advertisementAdapter.registerAdapterDataObserver(adapterDataObserver)
    }

    private fun initRecyclerView() {
        lifecycleScope.launch {
            binding.rvAdvertisement.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            binding.rvAdvertisement.adapter = advertisementAdapter
        }
    }

    override fun onDestroyView() {
        binding.adView.removeAllViews()
        advertisementAdapter.removeLoadStateListener(loadingStateListener)
        advertisementAdapter.unregisterAdapterDataObserver(adapterDataObserver)
        super.onDestroyView()
    }
}