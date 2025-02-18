package com.example.fakestore.presentasion.list.epoxy

import androidx.lifecycle.viewModelScope
import com.airbnb.epoxy.CarouselModel_
import com.airbnb.epoxy.TypedEpoxyController
import com.example.fakestore.model.domain.Filter
import com.example.fakestore.model.ui.ProductListFragmentUiState
import com.example.fakestore.presentasion.ProductRefreshViewModel
import kotlinx.coroutines.launch

class UiProductEpoxyController(
    private val viewModel: ProductRefreshViewModel,
) : TypedEpoxyController<ProductListFragmentUiState>() {

    override fun buildModels(data: ProductListFragmentUiState?) {

        when (data) {
            is ProductListFragmentUiState.Success -> {
                val uiFilterModels = data.filters.map {
                    UiProductFilterEpoxyModel(it, ::onFilterSelected).id(it.filter.value)
                }
                CarouselModel_().models(uiFilterModels).id("filters").addTo(this)

                data.products.forEach { uiProduct ->
                    UiProductEpoxyModel(
                        uiProduct,
                        ::onFavoriteIconClicked,
                        ::onUiProductClicked,
                        ::onAddToCartClicked
                    )
                        .id(uiProduct.product.id).addTo(this)
                }
            }

            is ProductListFragmentUiState.Loading -> {
                repeat(7) {
                    val epoxyId = it + 1
                    UiProductEpoxyModel(
                        null,
                        ::onFavoriteIconClicked,
                        ::onUiProductClicked,
                        ::onAddToCartClicked
                    )
                        .id(epoxyId).addTo(this)
                }
            }

            else -> {
                throw RuntimeException("Unhandled branch $data")
            }

        }
    }

    private fun onFavoriteIconClicked(selectedProductId: Int) {
        viewModel.viewModelScope.launch {
            viewModel.store.update { currentState ->
                val currentFavoriteIds = currentState.favoriteProductIds
                val newFavoriteIds = if (currentFavoriteIds.contains(selectedProductId))
                    currentFavoriteIds.filter { it != selectedProductId }.toSet()
                else currentFavoriteIds + setOf(selectedProductId)
                return@update currentState.copy(favoriteProductIds = newFavoriteIds)
            }
        }
    }

    private fun onUiProductClicked(productId: Int) {
        viewModel.viewModelScope.launch {
            viewModel.store.update { currentState ->
                val currentIds = currentState.expandedProductIds
                val newIds = if (currentIds.contains(productId))
                    currentIds.filter { it != productId }.toSet()
                else currentIds + setOf(productId)
                return@update currentState.copy(expandedProductIds = newIds)
            }
        }
    }

    private fun onFilterSelected(itemFilter: Filter) {
        viewModel.viewModelScope.launch {
            viewModel.store.update { currentState ->
                val currentlySelectedFilter = currentState.productsFilterInfo.selectedFilter
                return@update currentState.copy(
                    productsFilterInfo = currentState.productsFilterInfo.copy(
                        selectedFilter = if (currentlySelectedFilter != itemFilter) itemFilter
                        else null
                    )
                )
            }
        }
    }

    private fun onAddToCartClicked(productId: Int) { //ну и название для приличия
        viewModel.viewModelScope.launch {
            viewModel.store.update { currentState ->
                val currentIds = currentState.inCartProductIds //менять тут
                val newIds = if (currentIds.contains(productId))
                    currentIds.filter { it != productId }.toSet()
                else currentIds + setOf(productId)
                return@update currentState.copy(inCartProductIds = newIds) // и тут
            }
        }
    }
}