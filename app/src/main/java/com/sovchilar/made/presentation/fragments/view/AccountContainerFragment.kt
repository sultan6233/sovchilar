package com.sovchilar.made.presentation.fragments.view

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.sovchilar.made.R
import com.sovchilar.made.data.local.usecases.EncryptedSharedPrefsUseCase
import com.sovchilar.made.databinding.FragmentAccountContainerBinding
import com.sovchilar.made.domain.models.remote.auth.AuthState
import com.sovchilar.made.domain.models.remote.auth.AuthStateModel
import com.sovchilar.made.presentation.activity.MainActivity
import com.sovchilar.made.presentation.viewmodel.AccountContainerViewModel
import com.sovchilar.made.presentation.viewmodel.RegisterViewModel
import com.sovchilar.made.uitls.login
import com.sovchilar.made.uitls.password
import com.sovchilar.made.uitls.token
import com.sovchilar.made.uitls.utils.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccountContainerFragment :
    BaseFragment<FragmentAccountContainerBinding>(FragmentAccountContainerBinding::inflate) {

    private val viewModel: AccountContainerViewModel by viewModels()
    private val registerViewModel: RegisterViewModel by activityViewModels()
    private val encryptedSharedPrefsUseCase by lazy { EncryptedSharedPrefsUseCase(requireContext()) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        lifecycleScope.launch {
            registerViewModel.loginLiveData.observe(viewLifecycleOwner) {
                binding.pbAccountContainer.isVisible = false

            }

            withContext(Dispatchers.IO) {
                val login = encryptedSharedPrefsUseCase.readFromFile(login)
                val password = encryptedSharedPrefsUseCase.readFromFile(password)
                val token = encryptedSharedPrefsUseCase.readFromFile(token)
            }
        }
    }
}