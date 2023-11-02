package com.sovchilar.made.presentation.fragments.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sovchilar.made.R
import com.sovchilar.made.databinding.FragmentChangeLanguageBinding
import com.sovchilar.made.presentation.fragments.language.Language
import com.sovchilar.made.presentation.fragments.view.adapter.ChangeLanguageAdapter
import com.sovchilar.made.presentation.usecases.GradientTextViewUseCase.awaitLayoutChange
import com.sovchilar.made.presentation.usecases.GradientTextViewUseCase.setGradientTextColor
import com.sovchilar.made.presentation.viewmodel.ChangeLanguageViewModel
import com.sovchilar.made.uitls.utils.BaseFragment
import kotlinx.coroutines.launch

class ChangeLanguageFragment :
    BaseFragment<FragmentChangeLanguageBinding>(FragmentChangeLanguageBinding::inflate) {
    private val viewModel: ChangeLanguageViewModel by viewModels()
    private val languageAdapter by lazy { ChangeLanguageAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            binding.tvTitleLanguage.awaitLayoutChange()
            binding.tvTitleLanguage.setGradientTextColor(
                requireContext(),
                R.color.light_pink, R.color.dark_pink
            )
        }

        binding.rvLanguages.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvLanguages.adapter = languageAdapter
        languageAdapter.differ.submitList(provideLanguages())
    }

    private fun provideLanguages(): List<Language.Supported> {
        val languageStrings = requireContext().resources.getStringArray(R.array.languages)
        return languageStrings.map { Language.Supported(it) }
    }

}