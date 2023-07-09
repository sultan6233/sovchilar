package com.sovchilar.made.presentation.fragments.view

import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.sovchilar.made.R
import com.sovchilar.made.databinding.FragmentAddBinding
import com.sovchilar.made.presentation.fragments.dialogs.PayDialog
import com.sovchilar.made.presentation.fragments.view.extentions.markRequiredInRed
import com.sovchilar.made.presentation.usecases.navigateSafe
import com.sovchilar.made.presentation.viewmodel.AddViewModel
import com.sovchilar.made.presentation.viewmodel.MainViewModel
import com.sovchilar.made.uitls.utils.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine

@AndroidEntryPoint
class AddFragment : BaseFragment<FragmentAddBinding>(FragmentAddBinding::inflate) {

    private val viewModel: AddViewModel by viewModels()
    private val activityViewModel by activityViewModels<MainViewModel>()
    private val marriageStatusItems by lazy {
        arrayListOf(
            getString(R.string.divorced), getString(R.string.never_married)
        )
    }

    private val childrenItems by lazy {
        arrayListOf(
            getString(R.string.no_children), getString(R.string.have_children)
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            initMarriageStatus()
            initChildren()
            initCountry()
            initCity()
            initEditTextsHint()
            initClicks()
            initPayButton()
        }

        lifecycleScope.launch {
            binding.tvDescription.awaitLayoutChange()
            binding.tvDescription.setGradientTextColor(
                R.color.light_pink, R.color.dark_pink
            )
        }
        lifecycleScope.launch {
            fixAutoCompleteTextViewsError()
        }

    }

    private fun TextView.setGradientTextColor(vararg colorRes: Int) {
        val floatArray = ArrayList<Float>(colorRes.size)
        for (i in colorRes.indices) {
            floatArray.add(i, i.toFloat() / (colorRes.size - 1))
        }
        val textShader: Shader = LinearGradient(
            0f,
            0f,
            this.width.toFloat(),
            this.height.toFloat(),
            colorRes.map { ContextCompat.getColor(requireContext(), it) }.toIntArray(),
            floatArray.toFloatArray(),
            Shader.TileMode.CLAMP
        )
        this.paint.shader = textShader
    }

    private suspend fun View.awaitLayoutChange() = suspendCancellableCoroutine { cont ->
        val listener = object : View.OnLayoutChangeListener {
            override fun onLayoutChange(
                view: View?,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int
            ) {
                view?.removeOnLayoutChangeListener(this)
                cont.resumeWith(Result.success(Unit))
            }
        }

        addOnLayoutChangeListener(listener)
        cont.invokeOnCancellation { removeOnLayoutChangeListener(listener) }
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
        binding.btnSubmit.setOnClickListener {
            checkAllFields()
        }
        binding.clAdd.setOnClickListener {
            hideKeyboard(it)
        }
        binding.btnPay.setOnClickListener {
            view?.findNavController()?.navigateSafe(R.id.action_addFragment_to_payDialog)
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

    private fun checkAllFields() {
        var countErrors = 0
        if (binding.tedName.text.isNullOrEmpty()) {
            binding.tedName.error = getString(R.string.required_field)
            countErrors++
        }
        if (binding.tedAge.text.isNullOrEmpty()) {
            binding.tedAge.error = getString(R.string.required_field)
            countErrors++
        } else if (binding.tedAge.text.toString().toInt() < 18) {
            Snackbar.make(requireView(), getString(R.string.adult), Snackbar.LENGTH_LONG).show()
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
        }
        if (binding.tedTillAge.text.isNullOrEmpty()) {
            binding.tedTillAge.error = getString(R.string.required_field)
            countErrors++
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
        if (countErrors > 0) {
            Snackbar.make(requireView(), getString(R.string.errors), Snackbar.LENGTH_LONG).show()
        }

    }

    private fun initPayButton() {
        binding.btnPay.text = getString(
            R.string.pay_and_amount, viewModel.provideSum().toString() + getString(R.string.sum)
        )
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

}