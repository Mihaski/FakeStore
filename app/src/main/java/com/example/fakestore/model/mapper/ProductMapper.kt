package com.example.fakestore.model.mapper

import com.example.fakestore.model.domain.Product
import com.example.fakestore.model.network.NetworkProduct
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Locale
import javax.inject.Inject

class ProductMapper @Inject constructor() {
    fun buildFrom(networkProduct: NetworkProduct): Product {
        return Product(
            capitalize(networkProduct.category),
            networkProduct.description,
            networkProduct.id,
            networkProduct.image,
            BigDecimal(networkProduct.price).setScale(2, RoundingMode.HALF_UP),
            networkProduct.title
        )
    }

    private fun capitalize(sequence: String): String {
        return sequence.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
    }
}