package com.sovchilar.made.presentation.fragments.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sovchilar.made.R
import com.sovchilar.made.databinding.FragmentAccountBinding
import com.sovchilar.made.presentation.fragments.viewmodel.AccountViewModel
import com.sovchilar.made.uitls.utils.BaseFragment

class AccountFragment : BaseFragment<FragmentAccountBinding>(FragmentAccountBinding::inflate) {

    private val viewModel: AccountViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAddAdvertisement.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_addFragment)
        }
    }

}