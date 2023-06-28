package com.sovchilar.made.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val bottomHeight = MutableLiveData<Int>()
    val dataReady = MutableLiveData<Boolean>()
}