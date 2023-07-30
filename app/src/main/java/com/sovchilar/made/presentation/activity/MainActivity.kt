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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.sovchilar.made.R
import com.sovchilar.made.data.local.usecases.EncryptedSharedPrefsUseCase
import com.sovchilar.made.databinding.ActivityMainBinding
import com.sovchilar.made.domain.models.remote.auth.AuthState
import com.sovchilar.made.presentation.viewmodel.MainViewModel
import com.sovchilar.made.uitls.authenticated
import com.sovchilar.made.uitls.login
import com.sovchilar.made.uitls.password
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModels()
    private val encryptedSharedPrefsUseCase by lazy { EncryptedSharedPrefsUseCase(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        lifecycleScope.launch {
            initNavHostFragment()
            authenticate()
            initSplashAnimation()
            setStatusBarLightText(window)
            observeAuth()
            makeBlurBackground()
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

    private fun authenticate() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                viewModel.loginOrRegisterRequest(
                    encryptedSharedPrefsUseCase.readFromFile(login),
                    encryptedSharedPrefsUseCase.readFromFile(password)
                )
            }
        }
    }

    private fun observeAuth() {
        viewModel.loginLiveData.observe(this) {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    viewModel.loginLiveData = MutableLiveData()
                    when (it.state) {
                        AuthState.AUTHENTICATED -> {
                            withContext(Dispatchers.IO) {
                                encryptedSharedPrefsUseCase.saveAuthState(authenticated)
                                viewModel.dataReady.postValue(true)
                            }
                        }

                        AuthState.INVALID_AUTHENTICATION -> {
                            withContext(Dispatchers.IO) {
                                viewModel.dataReady.postValue(true)
                            }
                        }

                        else -> {
                            withContext(Dispatchers.IO) {
                                viewModel.dataReady.postValue(true)
                            }
                        }
                    }
                }

            }
        }
    }

    private fun setStatusBarLightText(window: Window) {
        setStatusBarLightTextOldApi(window, false)
        setStatusBarLightTextNewApi(window, false)
    }


    private fun setStatusBarLightTextOldApi(window: Window, isLight: Boolean) {
        val decorView = window.decorView
        decorView.systemUiVisibility = if (isLight) {
            decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        } else {
            decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    private fun setStatusBarLightTextNewApi(window: Window, isLightText: Boolean) {
        ViewCompat.getWindowInsetsController(window.decorView)?.apply {
            isAppearanceLightStatusBars = !isLightText
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

    private fun initNavHostFragment() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bnvSovchilar.setupWithNavController(navController)
    }

}