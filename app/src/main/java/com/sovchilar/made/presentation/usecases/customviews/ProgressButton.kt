package com.sovchilar.made.presentation.usecases.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.sovchilar.made.R

class ProgressButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val progressBar: ProgressBar
    private val buttonTextView: TextView

    init {
        val root = LayoutInflater.from(context).inflate(R.layout.progress_button, this, true)
        buttonTextView = root.findViewById(R.id.button_text)
        progressBar = root.findViewById(R.id.progress_indicator)
        loadAttr(attrs, defStyleAttr)
    }

    private fun loadAttr(attrs: AttributeSet?, defStyleAttr: Int) {
        val arr = context.obtainStyledAttributes(
            attrs,
            R.styleable.ProgressButton,
            defStyleAttr,
            0
        )

        val buttonText = arr.getString(R.styleable.ProgressButton_text)
        val loading = arr.getBoolean(R.styleable.ProgressButton_loading, false)
        val enabled = arr.getBoolean(R.styleable.ProgressButton_enabled, true)
        arr.recycle()
        isEnabled = enabled
        buttonTextView.isEnabled = enabled
        setText(buttonText)
        setLoading(loading)
    }

    fun setLoading(loading: Boolean) {
        isClickable = !loading //Disable clickable when loading
        if (loading) {
            buttonTextView.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            isEnabled = false
        } else {
            buttonTextView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            isEnabled = true
        }
    }

    fun setText(text: String?) {
        buttonTextView.text = text
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        buttonTextView.isEnabled = enabled
    }
}