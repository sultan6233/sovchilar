package com.sovchilar.made.presentation.usecases

import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import kotlinx.coroutines.suspendCancellableCoroutine

object GradientTextViewUseCase {
    fun TextView.setGradientTextColor(context: Context, vararg colorRes: Int) {
        val floatArray = ArrayList<Float>(colorRes.size)
        for (i in colorRes.indices) {
            floatArray.add(i, i.toFloat() / (colorRes.size - 1))
        }
        val textShader: Shader = LinearGradient(
            0f,
            0f,
            this.width.toFloat(),
            this.height.toFloat(),
            colorRes.map { ContextCompat.getColor(context, it) }.toIntArray(),
            floatArray.toFloatArray(),
            Shader.TileMode.CLAMP
        )
        this.paint.shader = textShader
    }

    suspend fun View.awaitLayoutChange() = suspendCancellableCoroutine { cont ->
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
}