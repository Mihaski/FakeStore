package com.example.fakestore.model

import com.example.fakestore.model.network.ProductsService
import com.example.fakestore.model.domain.Product
import com.example.fakestore.model.mapper.ProductMapper
import javax.inject.Inject

class ProductRepository@Inject constructor(
    private val productsService: ProductsService,
    private val productMapper: ProductMapper
) {

    suspend fun fetchAllProducts(): List<Product> {
        // todo better error handling
        return productsService.getAllProducts().body()?.let { networkProducts ->
            networkProducts.map { productMapper.buildFrom(it) }
        } ?: emptyList()
    }
}