package com.sovchilar.made.presentation.activity

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.animation.AnticipateInterpolator
import androidx.activity.viewModels
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate
import com.akexorcist.localizationactivity.core.OnLocaleChangedListener
import com.appodeal.ads.Appodeal
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sovchilar.made.EncryptedSharedPrefsUseCase
import com.sovchilar.made.R
import com.sovchilar.made.databinding.ActivityMainBinding
import com.sovchilar.made.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import sovchilar.uz.comm.first_launch
import sovchilar.uz.comm.login
import sovchilar.uz.comm.password
import sovchilar.uz.domain.models.remote.auth.AuthModel
import java.lang.ref.WeakReference
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnLocaleChangedListener {

    val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModels()

    private val encryptedSharedPrefsUseCase by lazy { EncryptedSharedPrefsUseCase(applicationContext) }

    private val navHostFragment by lazy { supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment }
    private val navController by lazy { navHostFragment.navController }
    private val navGraph by lazy { navController.navInflater.inflate(R.navigation.nav_graph) }
    private val localizationDelegate = LocalizationActivityDelegate(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        localizationDelegate.addOnLocaleChangedListener(this)
        localizationDelegate.onCreate()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        lifecycleScope.launch {
            initBottomNavigationView()
            initSplashAnimation()
            setStatusBarLightText(window)
            makeBlurBackground()
            showActiveFragment()
            initAppodeal()
            //   updateBottomNavigationSelection()

        }
    }

    private fun initAppodeal() {
        Appodeal.setTesting(true)

        Appodeal.setBannerViewId(R.id.adView)
        Appodeal.initialize(
            this,
            "15ab0c351c180baa49f962b8b37b072d55e9cbfb2d6468a2",
            Appodeal.BANNER_VIEW or Appodeal.REWARDED_VIDEO
        ) {
            viewModel.loadBanner()
        }
    }

    private fun showActiveFragment() {
        if (encryptedSharedPrefsUseCase.readBoolean(first_launch)) {
            viewModel.dataReady.value = true
            binding.bnvSovchilar.isVisible = false
            navGraph.setStartDestination(R.id.changeLanguageFragment)
            navController.graph = navGraph
        } else {
            viewModel.loginOrRegister(
                AuthModel(
                    encryptedSharedPrefsUseCase.readFromFile(
                        login
                    ), encryptedSharedPrefsUseCase.readFromFile(password)
                )
            )
            binding.bnvSovchilar.isVisible = true
        }
    }

    private fun initBottomNavigationView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bnvSovchilar.setupWithNavController2(navController)
//        binding.bnvSovchilar.setOnItemSelectedListener {
//            when (it.itemId) {
//                R.id.accountContainerFragment -> {
//                    if (navController.currentDestination?.id != R.id.accountContainerFragment) {
//                        navController.navigate(R.id.accountContainerFragment)
//                    }
//
//                }
//
//                else -> NavigationUI.onNavDestinationSelected(it, navController)
//            }
//            true
//        }


        val navControllerAccount by lazy {
            findNavController(R.id.nav_host_account_fragment)
        }
//        binding.bnvSovchilar.setOnItemSelectedListener {
//            when (it.itemId) {
//                R.id.accountContainerFragment ->
//                    navController.navigate(R.id.accountContainerFragment)
//
//                R.id.advertisementFragment -> navController.popBackStack()
//            }
//            true
//        }

//        binding.bnvSovchilar.setOnItemReselectedListener {
//            val reselectedDestinationId = it.itemId
//            if (reselectedDestinationId == R.id.accountContainerFragment) {
//                navControllerAccount.popBackStack(
//                    R.id.accountFragment,
//                    false
//                )
//            }
//        }
    }

    private fun BottomNavigationView.setupWithNavController2(navController: NavController) {
        val bottomNavigationView = this
        bottomNavigationView.setOnItemSelectedListener { item ->
            onNavDestinationSelected(item, navController)
        }
        val weakReference = WeakReference(bottomNavigationView)
        navController.addOnDestinationChangedListener(
            object : NavController.OnDestinationChangedListener {
                override fun onDestinationChanged(
                    controller: NavController,
                    destination: NavDestination,
                    arguments: Bundle?
                ) {
                    val view = weakReference.get()
                    if (view == null) {
                        navController.removeOnDestinationChangedListener(this)
                        return
                    }
                    val menu = view.menu
                    var i = 0
                    val size = menu.size()
                    while (i < size) {
                        val item = menu.getItem(i)
                        if (matchDestination(destination, item.itemId)) {
                            item.isChecked = true
                        }
                        i++
                    }
                }
            })

        // Add your own reselected listener
        bottomNavigationView.setOnItemReselectedListener { item ->
            // Pop everything up to the reselected item
            val reselectedDestinationId = item.itemId
            navController.popBackStack(reselectedDestinationId, false)
        }
    }

    private fun onNavDestinationSelected(
        item: MenuItem,
        navController: NavController
    ): Boolean {
        val builder = NavOptions.Builder()
            .setLaunchSingleTop(true)

        if (item.order and Menu.CATEGORY_SECONDARY == 0) {
            val findStartDestination = findStartDestination(navController.graph)
            if (findStartDestination != null) {
                builder.setPopUpTo(findStartDestination.id, false)
            }
        }
        val options = builder.build()
        //region The code was added to avoid adding already exist item
        if (navController.popBackStack(item.itemId, false)) {
            return true
        }
        //endregion
        return try {
            // TODO provide proper API instead of using Exceptions as Control-Flow.
            navController.navigate(item.itemId, null, options)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    private fun findStartDestination(graph: NavGraph): NavDestination? {
        var startDestination: NavDestination? = graph
        while (startDestination is NavGraph) {
            val parent = startDestination
            startDestination = parent.findNode(parent.startDestinationId)
        }
        return startDestination
    }

    fun matchDestination(
        destination: NavDestination,
        @IdRes destId: Int
    ): Boolean {
        var currentDestination: NavDestination? = destination
        while (currentDestination?.id != destId && currentDestination?.parent != null) {
            currentDestination = currentDestination.parent
        }
        return currentDestination?.id == destId
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

    public override fun onResume() {
        super.onResume()
        localizationDelegate.onResume(this)
    }

    override fun attachBaseContext(newBase: Context) {
        applyOverrideConfiguration(localizationDelegate.updateConfigurationLocale(newBase))
        super.attachBaseContext(newBase)
    }

    override fun getApplicationContext(): Context {
        return localizationDelegate.getApplicationContext(super.getApplicationContext())
    }

    override fun getResources(): android.content.res.Resources {
        return localizationDelegate.getResources(super.getResources())
    }

    fun setLanguage(language: String?) {
        localizationDelegate.setLanguage(this, language!!)
    }

    fun setLanguage(locale: Locale?) {
        localizationDelegate.setLanguage(this, locale!!)
    }

    val currentLanguage: Locale
        get() = localizationDelegate.getLanguage(this)


    override fun onAfterLocaleChanged() {

    }

    override fun onBeforeLocaleChanged() {
    }
}