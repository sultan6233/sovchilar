package com.sovchilar.made.presentation.usecases

import android.graphics.PorterDuff
import com.sovchilar.made.R
import sovchilar.uz.domain.models.CardModel

class Card {
    fun validateCard(_input: String, size: Int): CardModel {
        return if (size >= 4) {
            val input = _input.substring(0, 4)
            if (input == "8600") {
                CardModel(R.drawable.uzcard_logo, PorterDuff.Mode.SRC_IN)
            } else {
                if (input == "9860") {
                    CardModel(
                        R.drawable.humo_logo,
                        PorterDuff.Mode.SCREEN
                    )
                } else {
                    CardModel(
                        R.drawable.card_icon_placeholder,
                        PorterDuff.Mode.SRC_IN
                    )
                }
            }
        } else {
            CardModel(
                R.drawable.card_icon_placeholder,
                PorterDuff.Mode.SRC_IN
            )
        }
    }
}