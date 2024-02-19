package com.sovchilar.made.presentation.fragments.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.sovchilar.made.R
import com.sovchilar.made.databinding.FragmentAccountContainerBinding
import com.sovchilar.made.presentation.usecases.BaseFragment
import com.sovchilar.made.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sovchilar.uz.domain.models.remote.auth.AuthState

class AccountFragmentContainer :
    BaseFragment<FragmentAccountContainerBinding>(FragmentAccountContainerBinding::inflate) {
    private val activityViewModel: MainViewModel by activityViewModels()
    private val navHostFragment by lazy { childFragmentManager.findFragmentById(R.id.nav_host_account_fragment) as NavHostFragment }
    private val navController by lazy { navHostFragment.navController }
    private val navGraph by lazy { navController.navInflater.inflate(R.navigation.account_graph) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showActiveFragment()
//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            if (destination.id == R.id.addFragment) {
//                navController.popBackStack(R.id.accountFragment, false)
//                if (navController.currentDestination?.id != R.id.accountFragment) {
//                    navController.navigate(R.id.accountFragment)
//                }
//            }
//        }
    }

    private fun showActiveFragment() {
        activityViewModel.loginLiveData.value?.let {
            setStartDestination(it)
        } ?: {
            navGraph.setStartDestination(R.id.registerFragment)
            navController.graph = navGraph
        }
    }

    private fun setStartDestination(authState: AuthState) =
        lifecycleScope.launch(Dispatchers.Main) {
            when (authState) {
                is AuthState.AUTHENTICATED -> {
                    navGraph.setStartDestination(R.id.accountFragment)
                }

                else -> {
                    navGraph.setStartDestination(R.id.registerFragment)
                }
            }
            navController.graph = navGraph
            }
        }

