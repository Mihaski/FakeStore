package com.example.fakestore.presentasion.list.epoxy

import androidx.core.content.ContextCompat
import com.example.fakestore.R
import com.example.fakestore.databinding.EpoxyModelProductFilterBinding
import com.example.fakestore.model.domain.Filter
import com.example.fakestore.model.ui.UiFilter

data class UiProductFilterEpoxyModel(
    val uiFilter: UiFilter,
    val onFilterSelected: (Filter) -> Unit,
) : ViewBindingKotlinModel<EpoxyModelProductFilterBinding>(R.layout.epoxy_model_product_filter) {

    override fun EpoxyModelProductFilterBinding.bind() {
        root.setOnClickListener { onFilterSelected(uiFilter.filter) }
        filterNameTextView.text = uiFilter.filter.displayText

        val cardBackgroundColorResId = if (uiFilter.isSelectedFilter) R.color.purple_500
        else R.color.purple_200
        root.setCardBackgroundColor(ContextCompat.getColor(root.context, cardBackgroundColorResId))
    }
}