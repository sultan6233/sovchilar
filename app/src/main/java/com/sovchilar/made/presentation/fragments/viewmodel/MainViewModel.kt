package com.sovchilar.made.presentation.fragments.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val bottomHeight = MutableLiveData<Int>()
}