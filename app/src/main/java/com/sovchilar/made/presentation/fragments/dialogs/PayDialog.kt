package com.sovchilar.made.presentation.fragments.dialogs

import android.content.res.Resources
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.sovchilar.made.R
import com.sovchilar.made.data.local.usecases.EncryptedSharedPrefsUseCase
import com.sovchilar.made.databinding.FragmentDialogPayBinding
import com.sovchilar.made.domain.PostI
import com.sovchilar.made.presentation.viewmodel.PayViewModel
import com.sovchilar.made.uitls.humo
import com.sovchilar.made.uitls.token
import com.sovchilar.made.uitls.uzcard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class PayDialog(val postI: PostI) : DialogFragment() {
    private var _binding: FragmentDialogPayBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PayViewModel by viewModels()
    private val encryptedSharedPrefsUseCase: EncryptedSharedPrefsUseCase by lazy {
        EncryptedSharedPrefsUseCase(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogPayBinding.inflate(inflater, container, false)
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        val wmp = dialog?.window?.attributes
        wmp?.gravity = Gravity.CENTER
        wmp?.y = 100
        dialog?.setCanceledOnTouchOutside(false)
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
        initClicks()
        controlPaymentFields()
        initPaymentObserver()
        initConfirmObserver()
        confirmPaymentClick()
    }

    private suspend fun confirmPayment(smsCode: String) {
        withContext(Dispatchers.IO) {
            viewModel.confirmPaymentRequest(
                smsCode,
                viewModel.paymentResult!!.result.session,
                encryptedSharedPrefsUseCase.readFromFile(token)
            )
        }
    }

    private fun confirmPaymentClick() {
        binding.btnConfirmPay.setOnClickListener {
            lifecycleScope.launch {
                val smsCode = binding.tedConfirmSmsCode.text.toString()

                if (binding.tedConfirmSmsCode.toString()
                        .isNotEmpty() && binding.tedConfirmSmsCode.text.toString().length == 6
                ) {
                    confirmPayment(smsCode)
                } else {
                    binding.tipConfirmSmsCode.error = getString(R.string.required_field)
                }

            }

        }

    }

    private fun initConfirmObserver() {
        viewModel.paymentConfirmResult.observe(viewLifecycleOwner) { paymentConfirmResult ->
            paymentConfirmResult?.let {
                postI.successFullPayment()
                dismiss()
            }
        }
    }

    private fun initClicks() {
        binding.btnPay.setOnClickListener {
            if (checkFields()) {
                pay()
            }
        }
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun initPaymentObserver() {
        viewModel.paymentLiveData.observe(viewLifecycleOwner) {
            viewModel.paymentResult = it
            it?.let {
                binding.clPayment.isVisible = false
                binding.clPaymentConfirmation.isVisible = true
                binding.tvSendSms.text = getString(R.string.smsSentText, it.result.otpSentPhone)
            }
        }
    }

    private fun controlPaymentFields() {
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
        binding.tedExpireDate.addTextChangedListener {
            binding.tipExpireDate.error = null
        }
        binding.tedCardNumber.addTextChangedListener {
            binding.tipCardNumber.error = null
        }
    }

    private fun pay() {
        lifecycleScope.launch {
            val cardNumber: String
            val expireDate: String
            withContext(Dispatchers.Main) {
                cardNumber = binding.tedCardNumber.text.toString().replace(" ", "")
                expireDate = binding.tedExpireDate.text.toString().replace("/", "").rotate(2)
            }
            withContext(Dispatchers.IO) {
                viewModel.payRequest(
                    cardNumber, expireDate, encryptedSharedPrefsUseCase.readFromFile(token)
                )
            }
        }
    }

    private fun String.rotate(n: Int) = drop(n % length) + take(n % length)
    private fun checkFields(): Boolean {
        if (binding.tedCardNumber.text.isNullOrEmpty() && binding.tedExpireDate.text.isNullOrEmpty()) {
            binding.tipCardNumber.error = getString(R.string.empty_card_number)
            binding.tipExpireDate.error = getString(R.string.empty_expire_date)
            return false
        } else if (binding.tedCardNumber.text.isNullOrEmpty()) {
            binding.tipCardNumber.error = getString(R.string.empty_card_number)
            return false
        } else if (binding.tedExpireDate.text.isNullOrEmpty()) {
            binding.tipExpireDate.error = getString(R.string.empty_expire_date)
            return false
        } else if (binding.tedCardNumber.text?.length != 19) {
            binding.tipCardNumber.error = getString(R.string.invalid_card_number)
            return false
        } else if (binding.tedExpireDate.text?.length != 5) {
            binding.tipExpireDate.error = getString(R.string.invalid_expire_date)
            return false
        } else if ((binding.tedExpireDate.text?.substring(0, 2)?.toInt() ?: 0) > 12) {
            binding.tipExpireDate.error = getString(R.string.invalid_expire_date)
            return false
        } else if (!viewModel.dateUseCase.compareExpireDate(
                binding.tedExpireDate.text.toString()
            )
        ) {
            binding.tipExpireDate.error = getString(R.string.invalid_expire_date)
            return false
        } else if (!binding.tedCardNumber.text.toString().substring(0, 4).contains(uzcard)) {
            return if (!binding.tedCardNumber.text.toString().substring(0, 4).contains(humo)) {
                binding.tipCardNumber.error = getString(R.string.invalid_card_number)
                false
            } else {
                true
            }

        } else {
            return true
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}