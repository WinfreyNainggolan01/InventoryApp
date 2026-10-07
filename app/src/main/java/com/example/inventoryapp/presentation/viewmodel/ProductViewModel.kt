package com.example.inventoryapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventoryapp.domain.model.Product
import com.example.inventoryapp.domain.usecase.AddProductUseCase
import com.example.inventoryapp.domain.usecase.DeleteProductUseCase
import com.example.inventoryapp.domain.usecase.GetProductsUseCase
import com.example.inventoryapp.domain.usecase.UpdateProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val addProductUseCase: AddProductUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase
) : ViewModel() {

    val products: StateFlow<List<Product>> = getProductsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    fun addProduct(product: Product) {
        viewModelScope.launch {
            try {
                addProductUseCase(product)
                _message.value = "Produk berhasil ditambahkan"
            } catch (e: Exception) {
                _message.value = e.message
            }
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            try {
                updateProductUseCase(product)
                _message.value = "Produk berhasil diperbarui"
            } catch (e: Exception) {
                _message.value = e.message
            }
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            try {
                deleteProductUseCase(product)
                _message.value = "Produk berhasil dihapus"
            } catch (e: Exception) {
                _message.value = e.message
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}