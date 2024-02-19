package com.sovchilar.made.presentation.activity

import android.animation.ObjectAnimator
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.animation.AnticipateInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.sovchilar.made.EncryptedSharedPrefsUseCase
import com.sovchilar.made.R
import com.sovchilar.made.databinding.ActivityMainBinding
import com.sovchilar.made.presentation.navigation_utils.Screens
import com.sovchilar.made.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import sovchilar.uz.comm.first_launch
import sovchilar.uz.comm.login
import sovchilar.uz.comm.password
import sovchilar.uz.domain.models.remote.auth.AuthModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModels()
    private var oldFragment: Fragment = Screens.advertisementsFragment

    private val encryptedSharedPrefsUseCase by lazy { EncryptedSharedPrefsUseCase(applicationContext) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        lifecycleScope.launch {
            if (encryptedSharedPrefsUseCase.readBoolean(first_launch)) {
                viewModel.dataReady.value = true
                encryptedSharedPrefsUseCase.putBoolean(first_launch)
            } else {
                viewModel.loginOrRegister(
                    AuthModel(
                        encryptedSharedPrefsUseCase.readFromFile(
                            login
                        ), encryptedSharedPrefsUseCase.readFromFile(password)
                    )
                )
            }
            initBottomNavigationView()
            initSplashAnimation()
            setStatusBarLightText(window)
            makeBlurBackground()
            //   updateBottomNavigationSelection()

        }
    }

    private fun initBottomNavigationView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bnvSovchilar.setupWithNavController(navController)
        NavigationUI.setupWithNavController(binding.bnvSovchilar, navController, true)
//        binding.bnvSovchilar.setOnItemSelectedListener {
//            when (it.itemId) {
//                R.id.accountContainerFragment -> {
//                    if (navController.currentDestination?.id != R.id.accountContainerFragment) {
//                        navController.navigate(R.id.accountContainerFragment)
//                    }
//                    true
//                }
//
//                else -> NavigationUI.onNavDestinationSelected(it, navController)
//            }
//        }


        val navControllerAccount by lazy {
            findNavController(R.id.nav_host_account_fragment)
        }
        binding.bnvSovchilar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.accountContainerFragment ->
                    navController.navigate(R.id.accountContainerFragment)

                R.id.advertisementFragment -> navController.popBackStack()
            }
            true
        }

        binding.bnvSovchilar.setOnItemReselectedListener {
            val reselectedDestinationId = it.itemId
            if (reselectedDestinationId == R.id.accountContainerFragment) {
                navControllerAccount.popBackStack(
                    R.id.accountFragment,
                    false
                )
            }
        }
    }

    private fun makeBlurBackground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            binding.blurBg.setRenderEffect(
                RenderEffect.createBlurEffect(
                    10f,
                    10f,
                    Shader.TileMode.CLAMP
                )
            )
        }
    }

    private fun setStatusBarLightText(window: Window) {
        setStatusBarLightTextOldApi(window)
        setStatusBarLightTextNewApi(window)
    }


    private fun setStatusBarLightTextOldApi(window: Window) {
        val decorView = window.decorView
        decorView.systemUiVisibility =
            decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    private fun setStatusBarLightTextNewApi(window: Window) {
        ViewCompat.getWindowInsetsController(window.decorView)?.apply {
            isAppearanceLightStatusBars = true
        }
    }

    private fun initSplashAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                viewModel.dataReady.observe(this) { dataReady ->
                    if (dataReady) {
                        val slideUp = ObjectAnimator.ofFloat(
                            splashScreenView,
                            View.TRANSLATION_Y,
                            0f,
                            -splashScreenView.height.toFloat()
                        )
                        slideUp.interpolator = AnticipateInterpolator()
                        slideUp.duration = 500L
                        slideUp.doOnEnd {
                            splashScreenView.remove()
                        }
                        slideUp.start()
                    }
                }
            }
        }
    }
}