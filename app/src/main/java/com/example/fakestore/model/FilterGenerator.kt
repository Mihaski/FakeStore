package com.example.fakestore.model

import com.example.fakestore.model.domain.Filter
import com.example.fakestore.model.domain.Product
import javax.inject.Inject

class FilterGenerator @Inject constructor() {
    fun generateFrom(productsList: List<Product>): Set<Filter> =
        productsList.groupBy { it.category }.map {
            Filter(it.key, "${it.key} (${it.value.size})")
        }.toSet()
}