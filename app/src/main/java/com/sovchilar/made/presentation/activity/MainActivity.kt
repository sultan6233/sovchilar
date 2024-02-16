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
import androidx.lifecycle.lifecycleScope
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.sovchilar.made.R
import com.sovchilar.made.core.MainApplication
import com.sovchilar.made.databinding.ActivityMainBinding
import com.sovchilar.made.presentation.navigation_utils.Screens
import com.sovchilar.made.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import sovchilar.uz.comm.first_launch
import kotlinx.coroutines.launch
import sovchilar.uz.domain.models.remote.auth.AuthState

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModels()


    private val navigator = AppNavigator(this, R.id.clMain)

    override fun onResumeFragments() {
        super.onResumeFragments()
        MainApplication.INSTANCE.navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        MainApplication.INSTANCE.navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        lifecycleScope.launch {
//            if (encryptedSharedPrefsUseCase.readBoolean(first_launch)) {
//                viewModel.dataReady.value = true
//            } else {
//                //  authenticate()
//            }
            initBottomNavigationView()
            initSplashAnimation()
            setStatusBarLightText(window)
            makeBlurBackground()
            viewModel.dataReady.value = true
        }
    }

    private fun initBottomNavigationView() {
        binding.bnvSovchilar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.advertisementFragment -> {
                    MainApplication.INSTANCE.router.newRootScreen(Screens.advertisementsFragment())
                    true
                }

                R.id.accountFragment -> {
                    when (viewModel.loginLiveData.value?.state) {
                        AuthState.AUTHENTICATED ->
                            MainApplication.INSTANCE.router.newRootScreen(Screens.accountFragment())

                        else -> MainApplication.INSTANCE.router.newRootScreen(
                            Screens.registerFragment()
                        )
                    }
                    true
                }

                else -> {
                    false
                }
            }
        }
        binding.bnvSovchilar.selectedItemId = R.id.advertisementFragment
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