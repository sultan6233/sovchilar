package com.sovchilar.made.presentation.fragments.view

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.sovchilar.made.R
import com.sovchilar.made.data.local.usecases.EncryptedSharedPrefsUseCase
import com.sovchilar.made.databinding.FragmentAccountBinding
import com.sovchilar.made.domain.models.remote.auth.AuthState
import com.sovchilar.made.domain.usecases.OpenTelegramUseCase
import com.sovchilar.made.presentation.fragments.dialogs.ChangeLanguageDialog
import com.sovchilar.made.presentation.viewmodel.AccountViewModel
import com.sovchilar.made.presentation.viewmodel.RegisterViewModel
import com.sovchilar.made.uitls.authenticated
import com.sovchilar.made.uitls.login
import com.sovchilar.made.uitls.password
import com.sovchilar.made.uitls.utils.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccountFragment : BaseFragment<FragmentAccountBinding>(FragmentAccountBinding::inflate) {

    private val viewModel: AccountViewModel by viewModels()
    private val languageDialog by lazy { ChangeLanguageDialog() }
    private val openTelegramUseCase = OpenTelegramUseCase()
    private val encryptedSharedPrefsUseCase by lazy {
        EncryptedSharedPrefsUseCase(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            val authenticatedValue: Boolean
            withContext(Dispatchers.IO) {
                authenticatedValue = encryptedSharedPrefsUseCase.authenticated(authenticated)
            }
            withContext(Dispatchers.Main) {
                if (!authenticatedValue) {
                    view.findNavController()
                        .navigate(R.id.action_accountFragment_to_registerFragment)
                } else {
                    binding.pbAccount.isVisible = false
                    binding.clAccount.isVisible = true
                }
            }

        }

        binding.btnAddAdvertisement.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_addFragment)
        }
        binding.btnHelp.setOnClickListener {
            openTelegramUseCase.openTelegramHelp(requireContext())
        }
        binding.ibLanguage.setOnClickListener {
            languageDialog.show(childFragmentManager, "languageDialog")
        }
    }

}