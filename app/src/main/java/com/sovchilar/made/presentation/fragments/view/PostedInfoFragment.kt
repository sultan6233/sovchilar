package com.sovchilar.made.presentation.fragments.view

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.sovchilar.made.R
import com.sovchilar.made.databinding.FragmentPostedInfoBinding
import com.sovchilar.made.presentation.usecases.BaseFragment

class PostedInfoFragment :
    BaseFragment<FragmentPostedInfoBinding>(FragmentPostedInfoBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        providedText()
        binding.btnOk.setOnClickListener {
            findNavController().popBackStack(R.id.accountFragment,true)
        }
    }

    private fun providedText() {
        val data = arguments?.getString("postStatus")
        data?.let {
            if (it != "null") {
                binding.tvSubmitText.text = getString(R.string.submittedToReview)
            } else {
                binding.tvSubmitText.text = String.format(
                    getString(R.string.submitError), getString(R.string.support)
                )
            }
        } ?: binding.tvSubmitText.setText(
            String.format(
                getString(R.string.submitError), getString(R.string.support)
            )
        )
    }
}