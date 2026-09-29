package com.example.mobile1.ui.viewmodel

import android.os.Message
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile1.data.model.Category
import com.example.mobile1.data.model.Product
import com.example.mobile1.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    sealed interface ProductUiState{
        object Loading : ProductUiState
        data class Success(val categories: List<Category>, val products: List<Product>) : ProductUiState
        data class Error(val message: String) : ProductUiState
    }

    private val _uiState = MutableStateFlow<ProductUiState>(ProductUiState.Loading)

    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    init {
        fetchData()
    }
    private fun fetchData(){
        viewModelScope.launch {
            _uiState.value= ProductUiState.Loading
            try{
                val categoriesResponse= ApiClient.instance.getCategories()
                val productResponse= ApiClient.instance.getProducts()

                val mappedProducts=productResponse.map{product ->
                    val matchedCategory=categoriesResponse.find{it.id ==product.category_id}
                    product.copy(category = matchedCategory)
                }

                _uiState.value= ProductUiState.Success(
                    categories = categoriesResponse,
                    products = mappedProducts
                )
            }catch (e: Exception){
                _uiState.value= ProductUiState.Error(
                    message = "Gagal memuat data: ${e.localizedMessage}"
                )
            }
        }
    }
}