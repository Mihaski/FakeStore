package com.example.fakestore.mvi


import com.example.fakestore.model.domain.Filter
import com.example.fakestore.model.domain.Product

data class ApplicationState(
    val products: List<Product> = emptyList(),
    val productsFilterInfo: ProductFilterInfo = ProductFilterInfo(),
    val favoriteProductIds: Set<Int> = emptySet(),
    val expandedProductIds: Set<Int> = emptySet(),
    val inCartProductIds: Set<Int> = emptySet()
) {
    data class ProductFilterInfo(
        val filters: Set<Filter> = emptySet(),
        val selectedFilter: Filter? = null,
    )
}