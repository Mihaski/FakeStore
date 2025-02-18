package com.example.fakestore.presentasion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakestore.model.FilterGenerator
import com.example.fakestore.model.ProductRepository
import com.example.fakestore.mvi.ApplicationState
import com.example.fakestore.mvi.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductRefreshViewModel @Inject constructor(
    val store: Store<ApplicationState>,
    private val productRepository: ProductRepository,
    private val filterGenerator: FilterGenerator,
) : ViewModel() {

    fun refreshProducts() = viewModelScope.launch {
        val products = productRepository.fetchAllProducts()
        val filter = filterGenerator.generateFrom(products)
        store.update { applicationState ->
            return@update applicationState.copy(
                products = products,
                productsFilterInfo = ApplicationState.ProductFilterInfo(
                    filter,
                    null
                )
            )
        }
    }
}