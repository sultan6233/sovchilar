package com.sovchilar.made.presentation.fragments.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sovchilar.made.EncryptedSharedPrefsUseCase
import com.sovchilar.made.R
import com.sovchilar.made.databinding.FragmentChangeLanguageBinding
import com.sovchilar.made.presentation.activity.MainActivity
import com.sovchilar.made.presentation.fragments.language.Language
import com.sovchilar.made.presentation.fragments.view.adapter.ChangeLanguageAdapter
import com.sovchilar.made.presentation.usecases.GradientTextViewUseCase.awaitLayoutChange
import com.sovchilar.made.presentation.usecases.GradientTextViewUseCase.setGradientTextColor
import com.sovchilar.made.presentation.viewmodel.ChangeLanguageViewModel
import com.sovchilar.made.presentation.usecases.BaseFragment
import com.sovchilar.made.presentation.usecases.navigateSafe
import kotlinx.coroutines.launch
import sovchilar.uz.comm.first_launch

class ChangeLanguageFragment :
    BaseFragment<FragmentChangeLanguageBinding>(FragmentChangeLanguageBinding::inflate) {
    private val viewModel: ChangeLanguageViewModel by viewModels()
    private val languageAdapter by lazy { ChangeLanguageAdapter() }
    private val encryptedSharedPrefsUseCase by lazy { EncryptedSharedPrefsUseCase(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            binding.tvTitleLanguage.awaitLayoutChange()
            binding.tvTitleLanguage.setGradientTextColor(
                requireContext(),
                R.color.light_pink, R.color.dark_pink
            )
        }
        binding.btnNext.setOnClickListener {
            if (encryptedSharedPrefsUseCase.readBoolean(first_launch)) {
                encryptedSharedPrefsUseCase.putBoolean(first_launch)
                firstLaunch()
            } else {
                if (binding.btnUzbek.isChecked) {
                    (requireActivity() as MainActivity).setLanguage("uz")
                } else {
                    (requireActivity() as MainActivity).setLanguage("ru")
                }
                findNavController().popBackStack()
            }
        }

    }

    private fun firstLaunch() {
        if (binding.btnUzbek.isChecked) {
            (requireActivity() as MainActivity).setLanguage("uz")
        } else {
            (requireActivity() as MainActivity).setLanguage("ru")
        }
        findNavController().navigateSafe(R.id.action_changeLanguageFragment_to_advertisementFragment)

    }

    private fun provideLanguages(): List<Language.Supported> {
        val languageStrings = requireContext().resources.getStringArray(R.array.languages)
        return languageStrings.map { Language.Supported(it) }
    }

}