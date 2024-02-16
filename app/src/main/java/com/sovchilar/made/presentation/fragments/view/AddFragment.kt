package com.sovchilar.made.presentation.fragments.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.sovchilar.made.EncryptedSharedPrefsUseCase
import com.sovchilar.made.R
import com.sovchilar.made.databinding.FragmentAddBinding
import com.sovchilar.made.presentation.fragments.view.extentions.markRequiredInRed
import com.sovchilar.made.presentation.mappers.AdvertisementsModelMapper
import com.sovchilar.made.presentation.usecases.BaseFragment
import com.sovchilar.made.presentation.usecases.GradientTextViewUseCase.awaitLayoutChange
import com.sovchilar.made.presentation.usecases.GradientTextViewUseCase.setGradientTextColor
import com.sovchilar.made.presentation.usecases.TelegramSymbolInputFilter
import com.sovchilar.made.presentation.viewmodel.AddViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sovchilar.uz.comm.token
import sovchilar.uz.domain.models.remote.AdvertisementModelPresentation
import sovchilar.uz.domain.models.states.MarriageStatus
import sovchilar.uz.domain.utils.DataState

class AddFragment : BaseFragment<FragmentAddBinding>(FragmentAddBinding::inflate) {
    private val viewModel: AddViewModel by viewModels()

    private val encryptedSharedPrefsUseCase by lazy {
        EncryptedSharedPrefsUseCase(requireContext())
    }

    private val marriageStatusItems by lazy {
        arrayListOf(
            getString(R.string.divorced), getString(R.string.never_married)
        )
    }
    private val telegramSymbolInputFilter by lazy { TelegramSymbolInputFilter("@") }
    private val childrenItems by lazy {
        arrayListOf(
            getString(R.string.add_no_children), getString(R.string.add_have_children)
        )
    }
    private val countryItems by lazy { arrayListOf(getString(R.string.uzbekistan)) }
    private val cityItems by lazy {
        arrayListOf(
            getString(R.string.tashkent),
            getString(R.string.tashkent_region),
            getString(R.string.karakalpak_region),
            getString(R.string.andijan_region),
            getString(R.string.bukhara_region),
            getString(R.string.djizzak_region),
            getString(R.string.fergana_region),
            getString(R.string.kashkadarya_region),
            getString(R.string.horezm_region),
            getString(R.string.namangan_region),
            getString(R.string.novoi_region),
            getString(R.string.samarkand_region),
            getString(R.string.surxandarya_region),
            getString(R.string.sirdarya_region)
        )
    }

    private val advertisementsModelMapper by lazy { AdvertisementsModelMapper(requireContext()) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            initMarriageStatus()
            initChildren()
            initCountry()
            initCity()
            initEditTextsHint()
            initClicks()
            binding.tvDescription.awaitLayoutChange()
            binding.tvDescription.setGradientTextColor(
                requireActivity(),
                R.color.light_pink,
                R.color.dark_pink
            )
            fixAutoCompleteTextViewsError()
        }

        binding.tedTelegram.filters = arrayOf(telegramSymbolInputFilter)
    }

    private fun initEditTextsHint() {
        binding.tipName.markRequiredInRed()
        binding.tipAge.markRequiredInRed()
        binding.tipNationality.markRequiredInRed()
        binding.tipFromAge.markRequiredInRed()
        binding.tipTillAge.markRequiredInRed()
        binding.tipTelegram.markRequiredInRed()
        binding.tipMarriageStatus.markRequiredInRed()
        binding.tipChildren.markRequiredInRed()
        binding.tipCountry.markRequiredInRed()
        binding.tipCity.markRequiredInRed()
    }

    private fun hideKeyboard(view: View) {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun initClicks() {
        binding.clAdd.setOnClickListener {
            hideKeyboard(it)
        }
        binding.btnPay.setOnClickListener {
            when (checkAllFields()) {
                true -> {
//                    if (isAdded) {
//                        payDialog.show(parentFragmentManager, "payDialog")
//                    }
                }

                false -> Snackbar.make(
                    requireView(),
                    getString(R.string.errors),
                    Snackbar.LENGTH_LONG
                )
                    .show()
            }
        }

    }

    private fun collectUserInput() {
        lifecycleScope.launch {
            val name = binding.tedName.text.toString()
            val age = binding.tedAge.text.toString().toInt()
            val nationality = binding.tedNationality.text.toString()
            val fromAge = binding.tedFromAge.text.toString().toInt()
            val tillAge = binding.tedTillAge.text.toString().toInt()
            val telegram = binding.tedTelegram.text.toString()
            val marriageStatus = when (binding.spMarriageStatus.text.toString()) {
                getString(R.string.divorced) -> MarriageStatus.DIVORCED
                getString(R.string.never_married) -> MarriageStatus.NOMARRIAGE
                getString(R.string.widower) -> MarriageStatus.WIDOWER
                else -> MarriageStatus.NOMARRIAGE
            }
            val children = binding.spChildren.text.toString()
            binding.spChildren.text.toString()
            val country = binding.spCountry.text.toString()
            val city = binding.spCity.text.toString()
            val moreInfo = binding.tedMoreInfo.text.toString()
            val checkedId = binding.rgGender.checkedRadioButtonId
            val gender = requireView().findViewById<RadioButton>(checkedId).text.toString()


            withContext(Dispatchers.IO) {
                viewModel.postAdvertisement(
                    encryptedSharedPrefsUseCase.readFromFile(
                        token
                    ),
                    advertisementsModelMapper.mapToAdvertisementModel(
                        AdvertisementModelPresentation(
                            0,
                            name = name,
                            age = age,
                            nationality = nationality,
                            fromAge = fromAge,
                            tillAge = tillAge,
                            telegram = telegram,
                            marriageStatus = marriageStatus,
                            children = children,
                            country = country,
                            city = city,
                            gender = gender,
                            moreInfo = moreInfo
                        )
                    )
                )
            }
        }
    }

    private fun fixAutoCompleteTextViewsError() {
        binding.spMarriageStatus.addTextChangedListener {
            binding.tipMarriageStatus.error = null
        }
        binding.spChildren.addTextChangedListener {
            binding.tipChildren.error = null
        }
        binding.spCountry.addTextChangedListener {
            binding.tipCountry.error = null
        }
        binding.spCity.addTextChangedListener {
            binding.tipCity.error = null
        }
    }

    private fun checkAllFields(): Boolean {
        var countErrors = 0

        if (binding.tedName.text.isNullOrEmpty()) {
            binding.tedName.error = getString(R.string.required_field)
            countErrors++
        }

        if (binding.tedAge.text.isNullOrEmpty()) {
            binding.tedAge.error = getString(R.string.required_field)
            countErrors++
        } else if (binding.tedAge.text.toString().toInt() < 18) {
            binding.tedAge.error = getString(R.string.adult)
            countErrors++
        }

        if (binding.tedNationality.text.isNullOrEmpty()) {
            binding.tedNationality.error = getString(R.string.required_field)
            countErrors++
        }

        if (binding.spMarriageStatus.text.isNullOrEmpty()) {
            binding.tipMarriageStatus.error = getString(R.string.required_field)
            countErrors++
        }

        if (binding.spChildren.text.isNullOrEmpty()) {
            binding.tipChildren.error = getString(R.string.required_field)
            countErrors++
        }

        if (binding.tedFromAge.text.isNullOrEmpty()) {
            binding.tedFromAge.error = getString(R.string.required_field)
            countErrors++
        } else if (binding.tedFromAge.text.toString().toInt() < 18) {
            binding.tedFromAge.error = getString(R.string.adult)
            countErrors++
        }

        if (binding.tedTillAge.text.isNullOrEmpty()) {
            binding.tedTillAge.error = getString(R.string.required_field)
            countErrors++
        } else if (binding.tedTillAge.text.toString().toInt() < 18) {
            binding.tedTillAge.error = getString(R.string.adult)
            countErrors++
        } else if (!binding.tedFromAge.text.isNullOrEmpty()) {
            if (binding.tedTillAge.text.toString().toInt() < binding.tedFromAge.text.toString()
                    .toInt()
            ) {
                binding.tedTillAge.text = binding.tedFromAge.text
            }

        }

        if (binding.tedTelegram.text.isNullOrEmpty()) {
            binding.tedTelegram.error = getString(R.string.required_field)
            countErrors++
        }

        if (binding.spCountry.text.isNullOrEmpty()) {
            binding.tipCountry.error = getString(R.string.required_field)
            countErrors++
        }

        if (binding.spCity.text.isNullOrEmpty()) {
            binding.tipCity.error = getString(R.string.required_field)
            countErrors++
        }

        return countErrors <= 0
    }

    private fun initMarriageStatus() {
        val adapter =
            ArrayAdapter(requireContext(), R.layout.spinner_item_layout, marriageStatusItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spMarriageStatus.setAdapter(adapter)
    }

    private fun initChildren() {
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item_layout, childrenItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spChildren.setAdapter(adapter)
    }

    private fun initCountry() {
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item_layout, countryItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spCountry.setAdapter(adapter)
    }

    private fun initCity() {
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item_layout, cityItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spCity.setAdapter(adapter)
    }

    private fun observerSubmit() = lifecycleScope.launch {
        viewModel.postResponse.collectLatest {
            when (it) {
                is DataState.Success -> showPurchaseConfirmationDialog(getString(R.string.submittedToReview))
                is DataState.Error -> showPurchaseConfirmationDialog(
                    String.format(
                        getString(R.string.submitError), getString(R.string.support)
                    )
                )

                is DataState.Loading -> ""
            }
        }
    }

    private fun showPurchaseConfirmationDialog(text: String) {
        val confirmationDialogFragment = PurchaseConfirmationDialogFragment(text)
        if (isAdded) {
            confirmationDialogFragment.show(childFragmentManager, "")
        }
    }

}

class PurchaseConfirmationDialogFragment(val text: String) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage(text)
            .setCancelable(false)
            .setPositiveButton(getString(R.string.ok)) { _, _ -> findNavController().popBackStack() }
            .create()
}