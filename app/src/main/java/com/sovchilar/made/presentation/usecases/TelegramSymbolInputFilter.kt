package com.sovchilar.made.presentation.usecases

import android.text.InputFilter
import android.text.Spanned

class TelegramSymbolInputFilter(private val symbol: String) : InputFilter {
    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        return when {
            source.toString() == symbol && dest.toString().contains(symbol) -> {
                ""
            }

            source.toString().contains(symbol) && dest.toString().contains(symbol) -> {
                source.toString().replace(symbol,"")
            }

            source.toString() != symbol && dest.isNullOrEmpty() -> {
                symbol + source
            }
            else -> null
        }
    }
}