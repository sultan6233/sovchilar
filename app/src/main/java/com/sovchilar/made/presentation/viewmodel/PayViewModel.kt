package com.sovchilar.made.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.sovchilar.made.domain.models.CardModel
import com.sovchilar.made.presentation.usecases.Card
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PayViewModel @Inject constructor(private val card: Card) : ViewModel() {
    fun provideCard(input: String, size: Int): CardModel {
        return card.validateCard(input, size)
    }
}