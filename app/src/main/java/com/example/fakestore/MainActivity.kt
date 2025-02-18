package com.example.fakestore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.airbnb.epoxy.Carousel
import com.example.fakestore.databinding.ActivityMainBinding
import com.example.fakestore.mvi.ApplicationState
import com.example.fakestore.mvi.Store
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var store: Store<ApplicationState>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = (
                supportFragmentManager
                    .findFragmentById(
                        R.id.fragment_container_view_in_main_activity
                    ) as NavHostFragment).navController
        val navView = binding.bottomNavigationView
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.productListFragment, R.id.profileFragment
            )
        )
//для того чтобы убрать стрелку в аппбаре (материал вьюшка в верху экрана))
// и для того чтобы назначения были верхнего уровня topLevelDestination

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        Carousel.setDefaultGlobalSnapHelperFactory(null)

        store.stateFlow.map { it.inCartProductIds.size }.distinctUntilChanged().asLiveData()
            .observe(this) { numberOfProductsInCart ->
                navView.getOrCreateBadge(R.id.cartFragment).apply {
//apply мощная штука тем что как будто одна инструкция на множество свойств и методов
                    number = numberOfProductsInCart
                    isVisible = numberOfProductsInCart > 0
                }
            }
    }
}