package com.example.fakestore.model.ui

import com.example.fakestore.model.domain.Product

data class UiProduct(
    val product: Product,
    val isFavorite: Boolean = false,
    val isExpanded: Boolean = false,
    val isInCart: Boolean = false
)