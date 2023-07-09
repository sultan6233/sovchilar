package com.sovchilar.made.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor() : ViewModel() {
    suspend fun postAdvertisement() {

    }

    fun provideSum(): Int {
        return 13000
    }

}