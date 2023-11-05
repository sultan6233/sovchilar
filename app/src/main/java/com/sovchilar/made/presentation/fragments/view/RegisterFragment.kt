package com.sovchilar.made.presentation.fragments.view

import android.os.Bundle
import android.text.InputFilter
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.sovchilar.made.R
import com.sovchilar.made.data.local.usecases.EncryptedSharedPrefsUseCase
import com.sovchilar.made.databinding.FragmentRegisterBinding
import com.sovchilar.made.domain.models.remote.auth.AuthState
import com.sovchilar.made.presentation.usecases.TelegramSymbolInputFilter
import com.sovchilar.made.presentation.viewmodel.MainViewModel
import com.sovchilar.made.uitls.utils.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {

    private val viewModel: MainViewModel by activityViewModels()
    private val encryptedSharedPrefsUseCase by lazy { EncryptedSharedPrefsUseCase(requireContext()) }
    private val telegramSymbolInputFilter by lazy { TelegramSymbolInputFilter("@") }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authenticate()
        initClicks()
    }

    private fun initClicks() {
        binding.buttonBlack.setOnClickListener {
            binding.buttonBlack.setLoading(true)
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

    private fun loginOrRegister() {
        lifecycleScope.launch {
            if (binding.tedName.text.isNullOrEmpty() && binding.tedPassword.text.isNullOrEmpty()) {
                binding.tipName.error = getString(R.string.required_field)
                binding.tipPassword.error = getString(R.string.required_field)
                binding.buttonBlack.setLoading(false)
                //  hideLoginLoadingLayout()
            } else if (binding.tedName.text.isNullOrEmpty()) {
                binding.tipName.error = getString(R.string.required_field)
                binding.buttonBlack.setLoading(false)
                //hideLoginLoadingLayout()
            } else if (binding.tedPassword.text.isNullOrEmpty()) {
                binding.tipPassword.error = getString(R.string.required_field)
                binding.buttonBlack.setLoading(false)
//                hideLoginLoadingLayout()
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
                    //      hideLoginLoadingLayout()
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
                //     hideLoginLoadingLayout()
                binding.buttonBlack.setLoading(false)
                binding.tipPassword.error = it.message.toString()
            }
        }
    }
}