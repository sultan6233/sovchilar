package com.sovchilar.made.presentation.fragments.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sovchilar.made.databinding.FragmentAdvertisementBinding
import com.sovchilar.made.presentation.fragments.view.adapter.AdvertisementAdapter
import com.sovchilar.made.presentation.fragments.viewmodel.AdvertisementViewModel
import com.sovchilar.made.utils.BaseFragment
import kotlinx.coroutines.launch

class AdvertisementFragment : BaseFragment<FragmentAdvertisementBinding>(FragmentAdvertisementBinding::inflate) {
    private val viewModel: AdvertisementViewModel by viewModels()
    private val advertisementAdapter = AdvertisementAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        submitAdapterList()
    }
    private fun submitAdapterList(){
        lifecycleScope.launch {
            viewModel.advertisements.observe(viewLifecycleOwner){
                advertisementAdapter.differ.submitList(it)
            }
        }
    }
    private fun initRecyclerView(){
        lifecycleScope.launch {
            binding.rvAdvertisement.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            binding.rvAdvertisement.adapter = advertisementAdapter
        }
    }

}