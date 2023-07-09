package com.sovchilar.made.presentation.fragments.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.DialogFragment
import com.sovchilar.made.databinding.FragmentChangeLanguageBinding
import com.sovchilar.made.presentation.activity.MainActivity
import com.sovchilar.made.uitls.LocaleHelper
import java.util.Locale


class ChangeLanguageDialog : DialogFragment() {
    private var _binding: FragmentChangeLanguageBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangeLanguageBinding.inflate(inflater, container, false)
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        val wmp = dialog?.window?.attributes
        wmp?.gravity = Gravity.CENTER
        wmp?.y = 100
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvLanguageRu.setOnClickListener {
            LocaleHelper.setLocale(requireContext(), "ru")
            LocaleHelper.updateConfiguration(requireContext())
            dismiss()
            requireActivity().recreate()
//            val appLocale: LocaleListCompat =
//                LocaleListCompat.forLanguageTags("default")
//            AppCompatDelegate.setApplicationLocales(appLocale)
//            (requireActivity() as MainActivity).setLocale(requireContext(), "ru")
//            dismiss()

        }
        binding.tvLanguageUz.setOnClickListener {

            LocaleHelper.storeDefaultLocale()
            LocaleHelper.resetLocale(requireContext())
            LocaleHelper.updateConfiguration(requireContext())
            dismiss()
            requireActivity().recreate()
        }
    }


}