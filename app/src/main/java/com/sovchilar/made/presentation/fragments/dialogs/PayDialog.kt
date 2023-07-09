package com.sovchilar.made.presentation.fragments.dialogs

import android.content.res.Resources
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.sovchilar.made.R
import com.sovchilar.made.databinding.FragmentDialogPayBinding
import com.sovchilar.made.presentation.viewmodel.PayViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class PayDialog : DialogFragment() {
    private var _binding: FragmentDialogPayBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PayViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogPayBinding.inflate(inflater, container, false)
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        val wmp = dialog?.window?.attributes
        wmp?.gravity = Gravity.CENTER
        wmp?.y = 100
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    private fun DialogFragment.setWidthPercent(percentage: Int) {
        val percent = percentage.toFloat() / 100
        val displayMetrics = Resources.getSystem().displayMetrics
        val rect = displayMetrics.run { Rect(0, 0, widthPixels, heightPixels) }
        val percentWidth = rect.width() * percent
        dialog?.window?.setLayout(percentWidth.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setWidthPercent(90)

        binding.tedCardNumber.doAfterTextChanged { text ->
            binding.tipCardNumber.startIconDrawable = ContextCompat.getDrawable(
                requireContext(), viewModel.provideCard(text.toString(), text?.length ?: 0).drawable
            )
            binding.tipCardNumber.setStartIconTintMode(
                viewModel.provideCard(
                    text.toString(), text?.length ?: 0
                ).tintMode
            )
            val formattedText = text.toString().replace(" ", "").chunked(4).joinToString(" ")
            if (formattedText != text.toString()) {
                binding.tedCardNumber.setText(formattedText)
                binding.tedCardNumber.setSelection(binding.tedCardNumber.length())
            }
        }
        binding.tedExpireDate.doAfterTextChanged { text ->
            val formattedText = text.toString().replace("/", "").chunked(2).joinToString("/")
            if (formattedText != text.toString()) {
                binding.tedExpireDate.setText(formattedText)
                binding.tedExpireDate.setSelection(binding.tedExpireDate.length())
            }
        }
        binding.btnPay.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    viewModel.payRequest(
                        1000.00,
                        binding.tedCardNumber.text.toString().replace(" ", ""),
                        "2403"
//                        binding.tedExpireDate.text.toString().replace("/", "")
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}