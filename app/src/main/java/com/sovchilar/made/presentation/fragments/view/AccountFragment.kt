package com.sovchilar.made.presentation.fragments.view

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.sovchilar.made.EncryptedSharedPrefsUseCase
import com.sovchilar.made.R
import com.sovchilar.made.databinding.FragmentAccountBinding
import com.sovchilar.made.presentation.activity.MainActivity
import com.sovchilar.made.presentation.navigation_utils.Screens
import com.sovchilar.made.presentation.usecases.navigateSafe
import com.sovchilar.made.presentation.viewmodel.AccountViewModel
import sovchilar.uz.comm.authenticated
import com.sovchilar.made.presentation.usecases.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccountFragment : BaseFragment<FragmentAccountBinding>(FragmentAccountBinding::inflate) {

    private val viewModel: AccountViewModel by viewModels()
    private val openTelegramUseCase = sovchilar.uz.domain.usecases.OpenTelegramUseCase()
    private val encryptedSharedPrefsUseCase by lazy {
        EncryptedSharedPrefsUseCase(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        lifecycleScope.launch {
//            val token = encryptedSharedPrefsUseCase.readFromFile(token)
//            withContext(Dispatchers.IO) {
//                ApiService.create().getOwnAdvertisements("Bearer $token")
//                    .enqueue(object : Callback<UserModel> {
//                        override fun onResponse(
//                            call: Call<UserModel>,
//                            response: Response<UserModel>
//                        ) {
//                            response.body()?.let {
//                                Log.d("suka", it.toString())
//                            }
//                        }
//
//                        override fun onFailure(call: Call<UserModel>, t: Throwable) {
//                            Log.d("suka", t.localizedMessage ?: "unknown error")
//                        }
//                    })
//            }
//        }


        lifecycleScope.launch {
            binding.ibLanguage.setOnClickListener {
                //    findNavController().navigateSafe(R.id.action_accountFragment_to_changeLanguageFragment)
            }

            binding.btnAddAdvertisement.setOnClickListener {
                findNavController().navigateSafe(
                    R.id.action_accountFragment_to_addFragment
                )
            }
            binding.btnHelp.setOnClickListener {
                openTelegramUseCase.openTelegramHelp(requireContext(), binding.root)
            }
        }
    }
}