package com.sovchilar.made.presentation.fragments.view

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.sovchilar.made.R
import com.sovchilar.made.data.local.usecases.EncryptedSharedPrefsUseCase
import com.sovchilar.made.databinding.FragmentRegisterBinding
import com.sovchilar.made.domain.models.remote.auth.AuthState
import com.sovchilar.made.presentation.viewmodel.RegisterViewModel
import com.sovchilar.made.uitls.login
import com.sovchilar.made.uitls.password
import com.sovchilar.made.uitls.token
import com.sovchilar.made.uitls.utils.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {

    private val viewModel: RegisterViewModel by activityViewModels()
    private val encryptedSharedPrefsUseCase by lazy { EncryptedSharedPrefsUseCase(requireContext()) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLoginOrRegister.setOnClickListener {
            loginOrRegister()
        }
        binding.tedName.addTextChangedListener {
            binding.tipName.error = null
        }
        binding.tedPassword.addTextChangedListener {
            binding.tipPassword.error = null
        }
        authenticate()
    }

    private fun loginOrRegister() {
        lifecycleScope.launch {
            if (binding.tedName.text.isNullOrEmpty() && binding.tedPassword.text.isNullOrEmpty()) {
                binding.tipName.error = getString(R.string.required_field)
                binding.tipPassword.error = getString(R.string.required_field)
            } else if (binding.tedName.text.isNullOrEmpty()) {
                binding.tipName.error = getString(R.string.required_field)
            } else if (binding.tedPassword.text.isNullOrEmpty()) {
                binding.tipPassword.error = getString(R.string.required_field)
            } else {
                withContext(Dispatchers.IO) {
                    viewModel.loginOrRegisterRequest(
                        binding.tedName.text.toString(), binding.tedPassword.text.toString()
                    )
                }
            }
        }
    }

    private fun authenticate() {
        viewModel.loginLiveData.observe(viewLifecycleOwner) {
            if (it.state == AuthState.AUTHENTICATED) {
                lifecycleScope.launch {
                    val loginText = binding.tedName.text.toString()
                    val passwordText = binding.tedPassword.text.toString()
                    withContext(Dispatchers.IO) {
                        viewModel.saveCredentials(
                            encryptedSharedPrefsUseCase,
                            loginText,
                            passwordText,
                            it.token.toString()
                        )
                    }
                    withContext(Dispatchers.Main) {
                        requireView().findNavController().navigateUp()
                    }
                }

            }
            if (it.state == AuthState.INVALID_AUTHENTICATION) {
                binding.tipPassword.error = it.message.toString()
                Snackbar.make(
                    requireView(),
                    it.message.toString(),
                    Snackbar.LENGTH_SHORT
                ).show()
            }


        }
    }
}