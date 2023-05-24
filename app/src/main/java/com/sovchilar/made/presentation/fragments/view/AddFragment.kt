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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import com.sovchilar.made.R
import com.sovchilar.made.databinding.FragmentAddBinding
import com.sovchilar.made.presentation.fragments.view.extentions.markRequiredInRed
import com.sovchilar.made.presentation.fragments.viewmodel.AddViewModel
import com.sovchilar.made.presentation.fragments.viewmodel.MainViewModel
import com.sovchilar.made.uitls.utils.BaseFragment
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine

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

    private suspend fun View.awaitLayoutChange() = suspendCancellableCoroutine<Unit> { cont ->
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.clAdd.setOnClickListener {
            hideKeyboard(it)
        }
        initMarriageStatus()
        initChildren()
        initCountry()
        initCity()
        initEditTextsHint()
        lifecycle.coroutineScope.launch {
            binding.tvDescription.awaitLayoutChange()
            binding.tvDescription.setGradientTextColor(
                R.color.light_pink, R.color.dark_pink
            )
        }

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