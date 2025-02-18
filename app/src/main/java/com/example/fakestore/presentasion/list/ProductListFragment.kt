package com.example.fakestore.presentasion.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import com.example.fakestore.model.ui.ProductListFragmentUiState
import com.example.fakestore.presentasion.ProductRefreshViewModel
import com.example.fakestore.presentasion.list.epoxy.UiProductEpoxyController
import com.example.fakestore.databinding.FragmentProductsListBinding
import com.example.fakestore.model.ui.UiFilter
import com.example.fakestore.model.ui.UiProduct
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
class ProductListFragment : Fragment() {

    private val binding by lazy {
        FragmentProductsListBinding.inflate(layoutInflater)
    }

    private val viewModel: ProductRefreshViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val controller = UiProductEpoxyController(viewModel)
        binding.epoxyRecycleView.setController(controller)

        combine(
            viewModel.store.stateFlow.map { it.products },
            viewModel.store.stateFlow.map { it.favoriteProductIds },
            viewModel.store.stateFlow.map { it.expandedProductIds },
            viewModel.store.stateFlow.map { it.productsFilterInfo },
            viewModel.store.stateFlow.map { it.inCartProductIds }
        ) {
                listOfProducts, setOfFavoriteIds,
                setOfExpandedIds, productFilterInfo,
                inCartProductIds
            ->

            if (listOfProducts.isEmpty()) {
                return@combine ProductListFragmentUiState.Loading
            }

            val uiProducts = listOfProducts.map { product ->
                UiProduct(
                    product,
                    setOfFavoriteIds.contains(product.id),
                    setOfExpandedIds.contains(product.id),
                    inCartProductIds.contains(product.id)
                )
            }

            val uiFilters = productFilterInfo.filters.map {
                UiFilter(
                    it,
                    productFilterInfo.selectedFilter?.equals(it) == true
                )
            }.toSet()

            val filteredProducts = if (productFilterInfo.selectedFilter == null) uiProducts
            else uiProducts.filter { it.product.category == productFilterInfo.selectedFilter.value }

            return@combine ProductListFragmentUiState.Success(uiFilters, filteredProducts)

        }.distinctUntilChanged().asLiveData().observe(viewLifecycleOwner) { uiState ->
            controller.setData(uiState)
        }



        viewModel.refreshProducts()
    }

//    override fun onDestroyView() { //todo подумать надо ли очистить свойство если инит бай лэзи
//        super.onDestroyView()
//        binding = null
//    }
}