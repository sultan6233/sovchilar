package com.sovchilar.made.presentation.fragments.view

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.sovchilar.made.EncryptedSharedPrefsUseCase
import com.sovchilar.made.R
import com.sovchilar.made.databinding.FragmentRegisterBinding
import com.sovchilar.made.presentation.activity.MainActivity
import com.sovchilar.made.presentation.navigation_utils.Screens
import com.sovchilar.made.presentation.usecases.BaseFragment
import com.sovchilar.made.presentation.usecases.TelegramSymbolInputFilter
import com.sovchilar.made.presentation.usecases.navigateSafe
import com.sovchilar.made.presentation.viewmodel.MainViewModel
import com.sovchilar.made.presentation.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import sovchilar.uz.domain.models.remote.auth.AuthModel
import sovchilar.uz.domain.models.remote.auth.AuthState

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {

    private val viewModel: MainViewModel by activityViewModels()

    // private val viewModel: RegisterViewModel by viewModels()
    private val encryptedSharedPrefsUseCase by lazy {
        EncryptedSharedPrefsUseCase(
            requireContext()
        )
    }
    private val telegramSymbolInputFilter by lazy { TelegramSymbolInputFilter("@") }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authenticate()
        initClicks()
    }

    private fun initClicks() = lifecycleScope.launch {
        binding.btnLoginOrRegister.setOnClickListener {
            binding.btnLoginOrRegister.setLoading(true)
            loginOrRegister()
        }

        binding.tedName.filters = arrayOf(telegramSymbolInputFilter)
        binding.tedName.addTextChangedListener {
            binding.tipName.error = null
        }
        binding.tedPassword.addTextChangedListener {
            binding.tipPassword.error = null
        }
    }

    private fun loginOrRegister() = lifecycleScope.launch {
        if (binding.tedName.text.isNullOrEmpty() && binding.tedPassword.text.isNullOrEmpty()) {
            binding.tipName.error = getString(R.string.required_field)
            binding.tipPassword.error = getString(R.string.required_field)
            binding.btnLoginOrRegister.setLoading(false)
        } else if (binding.tedName.text.isNullOrEmpty()) {
            binding.tipName.error = getString(R.string.required_field)
            binding.btnLoginOrRegister.setLoading(false)
        } else if (binding.tedPassword.text.isNullOrEmpty()) {
            binding.tipPassword.error = getString(R.string.required_field)
            binding.btnLoginOrRegister.setLoading(false)
        } else {
            viewModel.loginOrRegister(
                AuthModel(
                    binding.tedName.text.toString(),
                    binding.tedPassword.text.toString()
                )
            )
        }
    }

    private fun authenticate() {
        viewModel.loginLiveData.observe(viewLifecycleOwner) { authState ->
            handleAuth(authState)
        }
    }

    private fun handleAuth(authState: AuthState) = lifecycleScope.launch {
        if (!viewModel.loginFromStart) {
            when (authState) {
                is AuthState.AUTHENTICATED -> {
                    authenticated(authState.authData.token)
                }

                is AuthState.INVALID_AUTHENTICATION -> {
                    binding.btnLoginOrRegister.setLoading(false)
                    binding.tipPassword.error = authState.error
                }

                is AuthState.CONNECTION_ERROR -> {
                    binding.btnLoginOrRegister.setLoading(false)
                    Snackbar.make(
                        requireView(),
                        authState.error,
                        Snackbar.LENGTH_LONG
                    ).show()
                }

                is AuthState.Loading -> binding.btnLoginOrRegister.setLoading(true)

                is AuthState.UNAUTHENTICATED -> ""
            }
        } else {
            viewModel.loginFromStart = false
        }

    }

    private fun authenticated(token: String) = lifecycleScope.launch {
        binding.btnLoginOrRegister.setLoading(false)
        val loginText = binding.tedName.text.toString()
        val passwordText = binding.tedPassword.text.toString()
        viewModel.saveCredentials(
            encryptedSharedPrefsUseCase,
            loginText,
            passwordText,
            token
        )
        val navController = findNavController()
        val graph = navController.navInflater.inflate(R.navigation.account_graph)
        graph.setStartDestination(R.id.accountFragment)
        findNavController().graph = graph
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (!hidden) {
            (requireActivity() as MainActivity).binding.bnvSovchilar.selectedItemId.apply {
                if (this != R.id.accountFragment) {
                    R.id.accountFragment
                }
            }
        }
        super.onHiddenChanged(hidden)
    }

    override fun onDestroyView() {
        viewModel.loginFromStart = true
        super.onDestroyView()
    }
}