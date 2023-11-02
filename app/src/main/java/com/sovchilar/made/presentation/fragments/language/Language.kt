package com.sovchilar.made.presentation.fragments.language

sealed class Language {
    data class Supported(val name: String, var selected:Boolean = false) : Language()
    object ENGLISH : Language()
    object RUSSIAN : Language()
    object UZBEK : Language()
}