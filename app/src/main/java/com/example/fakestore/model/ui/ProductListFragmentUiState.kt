package com.example.fakestore.model.ui

sealed interface ProductListFragmentUiState {
    data class Success(
        val filters: Set<UiFilter>,
        val products: List<UiProduct>,
    ) : ProductListFragmentUiState

    object Loading : ProductListFragmentUiState
}