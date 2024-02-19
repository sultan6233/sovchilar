package com.sovchilar.made.presentation.navigation_utils

import com.sovchilar.made.presentation.fragments.view.AccountFragment
import com.sovchilar.made.presentation.fragments.view.AccountFragmentContainer
import com.sovchilar.made.presentation.fragments.view.AddFragment
import com.sovchilar.made.presentation.fragments.view.AdvertisementFragment
import com.sovchilar.made.presentation.fragments.view.RegisterFragment

object Screens {
    val advertisementsFragment by lazy { AdvertisementFragment() }
    val accountFragment by lazy { AccountFragment() }
    val accountContainer by lazy { AccountFragmentContainer() }
    val registerFragment by lazy { RegisterFragment() }
    val addFragment by lazy { AddFragment() }
}

