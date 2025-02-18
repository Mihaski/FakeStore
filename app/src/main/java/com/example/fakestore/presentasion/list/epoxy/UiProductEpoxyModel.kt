package com.example.fakestore.presentasion.list.epoxy

import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import coil3.load
import com.example.fakestore.R
import com.example.fakestore.databinding.EpoxyModelProductItemBinding
import com.example.fakestore.model.ui.UiProduct
import java.text.NumberFormat

data class UiProductEpoxyModel(
    val uiProduct: UiProduct?,
    val onFavoriteIconClicked: (Int) -> Unit,
    val onUiProductClicked: (Int) -> Unit,
    val onAddToCartClicked: (Int) -> Unit,
) : ViewBindingKotlinModel<EpoxyModelProductItemBinding>(R.layout.epoxy_model_product_item) {

    private val currencyFormatter = NumberFormat.getCurrencyInstance()

    override fun EpoxyModelProductItemBinding.bind() {
        shimmerLayout.isVisible = uiProduct == null
        cardView.isInvisible = uiProduct == null

        uiProduct?.let { uiProduct -> //only for the ?.
            shimmerLayout.stopShimmer()

            productTitleTextView.text = uiProduct.product.title
            productCategoryTextView.text = uiProduct.product.category
            productDescriptionTextView.text = uiProduct.product.description
            productPriceTextView.text = currencyFormatter.format(uiProduct.product.price)

            productDescriptionTextView.isVisible = uiProduct.isExpanded
            root.setOnClickListener { onUiProductClicked(uiProduct.product.id) }

            inCartView.isVisible = uiProduct.isInCart
            addToCartButton.setOnClickListener {
                onAddToCartClicked(uiProduct.product.id)
            }

            favoriteImageView.setIconResource(
                if (uiProduct.isFavorite)
                    R.drawable.ic_round_favorite_24
                else R.drawable.ic_round_favorite_border_24
            )

            favoriteImageView.setOnClickListener {
                onFavoriteIconClicked(uiProduct.product.id)
            }

            productImageViewLoadingProgressBar.isVisible = true
            productImageView.load(
                uiProduct.product.image
            ) {
                productImageViewLoadingProgressBar.isGone = true
            }
        }
    }
}