package com.sovchilar.made.presentation.fragments.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.sovchilar.made.databinding.FragmentAdvertisementBinding
import com.sovchilar.made.presentation.fragments.viewmodel.AdvertisementViewModel
import com.sovchilar.made.utils.BaseFragment

class AdvertisementFragment : BaseFragment<FragmentAdvertisementBinding>(FragmentAdvertisementBinding::inflate) {
    private val viewModel: AdvertisementViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.advertisements.observe(viewLifecycleOwner){

        }

    }

}