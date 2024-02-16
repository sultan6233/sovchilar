package com.sovchilar.made.presentation.navigation_utils

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.sovchilar.made.presentation.fragments.view.AccountFragment
import com.sovchilar.made.presentation.fragments.view.AdvertisementFragment
import com.sovchilar.made.presentation.fragments.view.RegisterFragment

object Screens {
    fun advertisementsFragment() = FragmentScreen { AdvertisementFragment() }
    fun accountFragment() = FragmentScreen { AccountFragment() }
    fun registerFragment() = FragmentScreen { RegisterFragment() }
}