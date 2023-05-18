package com.sovchilar.made.presentation.fragments.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import com.sovchilar.made.R

class AddAdvertisementCustomEditText : AppCompatEditText {

    private val outlinePaint: Paint = Paint()
    private val textBoundsRect: Rect = Rect()
    private val backgroundRect: RectF = RectF()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        // Set up the outline paint
        outlinePaint.isAntiAlias = true
        outlinePaint.style = Paint.Style.STROKE
        outlinePaint.strokeWidth = 2f  // Adjust this value to change the border width
        outlinePaint.color = resources.getColor(R.color.black, context.theme)  // Replace with your desired border color
    }

    override fun onDraw(canvas: Canvas) {
        // Draw the outline border
        backgroundRect.set(
            (paddingLeft - outlinePaint.strokeWidth),
            (paddingTop - outlinePaint.strokeWidth),
            (width - paddingRight + outlinePaint.strokeWidth),
            (height - paddingBottom + outlinePaint.strokeWidth)
        )
        canvas.drawRoundRect(backgroundRect, 8f, 8f, outlinePaint)  // Adjust the corner radius as desired

        // Draw the text
        val text = text?.toString()
        text?.let {
            paint.getTextBounds(it, 0, it.length, textBoundsRect)
            canvas.drawText(
                text, paddingLeft.toFloat(), (height - paddingBottom).toFloat(),
                paint
            )
        }
    }
}