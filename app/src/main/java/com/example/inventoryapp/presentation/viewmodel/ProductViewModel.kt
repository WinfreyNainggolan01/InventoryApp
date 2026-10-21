package com.example.inventoryapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventoryapp.data.remote.dto.CreateProductRequestDto
import com.example.inventoryapp.data.remote.dto.UpdateProductRequestDto
import com.example.inventoryapp.domain.model.Category
import com.example.inventoryapp.domain.model.Product
import com.example.inventoryapp.domain.repository.RemoteProductRepository
import com.example.inventoryapp.domain.usecase.AddProductUseCase
import com.example.inventoryapp.domain.usecase.CreateRemoteProductUseCase
import com.example.inventoryapp.domain.usecase.DeleteProductUseCase
import com.example.inventoryapp.domain.usecase.DeleteRemoteProductUseCase
import com.example.inventoryapp.domain.usecase.GetCategoriesUseCase
import com.example.inventoryapp.domain.usecase.GetProductsUseCase
import com.example.inventoryapp.domain.usecase.GetRemoteCategoriesUseCase
import com.example.inventoryapp.domain.usecase.SearchProductUseCase
import com.example.inventoryapp.domain.usecase.UpdateProductUseCase
import com.example.inventoryapp.domain.usecase.UpdateRemoteProductUseCase
import com.example.inventoryapp.domain.usecase.UpdateRemoteStockUseCase
import com.example.inventoryapp.domain.usecase.UpdateStockUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration.Companion.milliseconds

enum class DataSouceOption {
    LOCAL,
    REMOTE
}

data class InventoryUiState(
//    LOCAL
    val products: List<Product> = emptyList(),
    val categories: List<Category> = emptyList(),
    val searchQuery: String = "",
    val selectedCategoryId: Int? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val totalInventoryValue: Double = 0.0,
    val lowStockCount: Int = 0,

//    REMOTE
    val selectedDataSource: DataSouceOption = DataSouceOption.LOCAL,
    val remoteProducts: List<Product> = emptyList(),
    val remoteCategories: List<Category> = emptyList(),
    val isRemoteLoading: Boolean = false,
    val isRemoteActionLoading: Boolean = false,
    val remoteErrorMessage: String? = null,
    val userFeedbackMessage: String? = null
)

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val addProductUseCase: AddProductUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val searchProductUseCase: SearchProductUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val updateStockUseCase: UpdateStockUseCase,

    // REMOTE
    private val remoteProductRepository: RemoteProductRepository,
    private val createRemoteProductUseCase: CreateRemoteProductUseCase,
    private val updateRemoteProductUseCase: UpdateRemoteProductUseCase,
    private val deleteRemoteProductUseCase: DeleteRemoteProductUseCase,
    private val updateRemoteStockUseCase: UpdateRemoteStockUseCase,
    private val getRemoteCategoriesUseCase: GetRemoteCategoriesUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedCategoryId = MutableStateFlow<Int?>(null)
    private val _uiState = MutableStateFlow(InventoryUiState())
    val uiState: StateFlow<InventoryUiState> = _uiState.asStateFlow()

    init {
        observeProducts()
        observeCategories()
    }


    // REMOTE
    fun setDataSource(source: DataSouceOption) {
        _uiState.update { it.copy(selectedDataSource = source) }
        if(source == DataSouceOption.REMOTE) {
            if(_uiState.value.remoteProducts.isEmpty()) {
                fetchRemoteProducts()
            }
            if(_uiState.value.remoteCategories.isEmpty()) {
                fetchRemoteCategories()
            }
        }
    }

    fun fetchRemoteProducts(search: String? = null, categoryId: Int? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isRemoteLoading = true, remoteErrorMessage = null) }

            val result = remoteProductRepository.getRemoteProduct(search, categoryId)
            result.onSuccess { remoteList ->
                _uiState.update {
                    it.copy(
                        remoteProducts = remoteList,
                        isRemoteLoading = false,
                        remoteErrorMessage = null
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isRemoteLoading = false,
                        remoteErrorMessage = error.localizedMessage ?: "Gagal mengambil data dari server"
                    )
                }
            }
        }
    }

    fun fetchRemoteCategories(){
        viewModelScope.launch {
            _uiState.update { it.copy(isRemoteLoading = true, remoteErrorMessage = null) }

            val result = getRemoteCategoriesUseCase.execute()
            result.onSuccess { categories ->
                _uiState.update {
                    it.copy(
                        remoteCategories = categories,
                        isRemoteLoading = false,
                        remoteErrorMessage = null
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isRemoteLoading = false,
                        remoteErrorMessage = error.localizedMessage ?: "Gagal mengambil data kategori dari server"
                    )
                }
            }
        }
    }

    fun createRemoteProducts(name: String, categoryId: Int, price: Double, stock: Int, description: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isRemoteActionLoading = true)}
            val req = CreateProductRequestDto(name, categoryId, price, stock, description)
            val result = createRemoteProductUseCase.execute(req)

            result.onSuccess { newProduct ->
                _uiState.update {
                    it.copy(
                        isRemoteActionLoading = false,
                        userFeedbackMessage = "Produk ${newProduct.name} berhasil disimpan ke server"
                    )
                }
                fetchRemoteProducts()
            }.onFailure { err ->
                _uiState.update {
                    it.copy(
                        isRemoteActionLoading = false,
                        userFeedbackMessage = err.message
                    )
                }
            }
        }
    }

    fun updateRemoteProduct(id: Int, name: String, categoryId: Int, price: Double, stock: Int, description: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isRemoteActionLoading = true
                )
            }
            val req = UpdateProductRequestDto(name, categoryId, price, stock, description)
            val result = updateRemoteProductUseCase.execute(id, req)

            result.onSuccess { updated ->
                _uiState.update {
                    it.copy(
                        isRemoteActionLoading = false,
                        userFeedbackMessage = "Produk ${updated.name} berhasil diperbaharui!"
                    )
                }
                fetchRemoteProducts()
            }.onFailure { err ->
                _uiState.update {
                    it.copy(
                        isRemoteActionLoading = false,
                        userFeedbackMessage = "Gagal memperbaharui produk: ${err.message}"
                    )
                }
            }
        }
    }

    fun deleteRemoteProduct(id: Int) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isRemoteActionLoading = true
                )
            }
            val result = deleteRemoteProductUseCase.execute(id)
            result.onSuccess {
                _uiState.update {
                    val updatedList = it.remoteProducts.filter { p -> p.id != id }
                    it.copy(
                        remoteProducts = updatedList,
                        isRemoteActionLoading = false,
                        userFeedbackMessage = "Produk berhasil dihapus dari server"
                    )
                }
            }.onFailure { err ->
                _uiState.update {
                    it.copy(
                        isRemoteActionLoading = false,
                        userFeedbackMessage = "Gagal menghapus produk: ${err.message}"
                    )
                }
            }
        }
    }

    fun updateRemoteStockProduct(id: Int, newStock: Int) {
        if (newStock < 0) return
        viewModelScope.launch {
            val result = updateRemoteStockUseCase.execute(id, newStock)
            result.onSuccess { updated ->
                _uiState.update {
                    val updatedList = it.remoteProducts.map { p -> if (p.id == id) updated else p }
                    it.copy(
                        remoteProducts = updatedList,
                        userFeedbackMessage = "Stok berhasil diubah menjadi ${updated.stock}"
                    )
                }
            }.onFailure { err ->
                _uiState.update {
                    it.copy(
                        userFeedbackMessage = "Gagal mengubah stok: ${err.message}"
                    )
                }
            }
        }
    }

    fun clearFeedbackMessage(){
        _uiState.update {
            it.copy(
                userFeedbackMessage = null,
                remoteErrorMessage = null
            )
        }
    }

    // LOCAL ROOM
    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private fun observeProducts() {
        combine(
            _searchQuery.debounce(300.milliseconds),
            _selectedCategoryId
        ) { query, categoryId ->
            Pair(query, categoryId)
        }
            .flatMapLatest { (query, categoryId) ->
                val flow = if (query.isBlank()) {
                    getProductsUseCase()
                } else {
                    searchProductUseCase(query)
                }

                flow.map { products ->
                    if (categoryId != null) {
                        products.filter { it.categoryId == categoryId }
                    } else {
                        products
                    }
                }
            }
            .onEach { products ->
                _uiState.update { it.copy(products = products, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }

    private fun observeCategories() {
        getCategoriesUseCase()
            .onEach { categories ->
                _uiState.update { it.copy(categories = categories) }
            }
            .launchIn(viewModelScope)
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onCategoryFilterChanged(categoryId: Int?) {
        _selectedCategoryId.value = categoryId
        _uiState.update { it.copy(selectedCategoryId = categoryId) }
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                addProductUseCase.execute(product)
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                _uiState.update {
                    it.copy(errorMessage = e.localizedMessage ?: "Gagal menambahkan produk")
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                updateProductUseCase(product)
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                _uiState.update {
                    it.copy(errorMessage = e.localizedMessage ?: "Gagal memperbaharui product")
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                deleteProductUseCase(product)
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                _uiState.update {
                    it.copy(errorMessage = e.localizedMessage ?: "Gagal menghapus produk")
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun updateStock(product: Product, change: Int, note: String = "") {
        viewModelScope.launch {
            val result = updateStockUseCase(
                productId = product.id,
                currentStock = product.stock,
                change = change,
                note = note
            )
            when (result) {
                is UpdateStockUseCase.Result.Error -> {
                    _uiState.update { it.copy(errorMessage = result.message) }
                }
                UpdateStockUseCase.Result.Success -> {
                    _uiState.update { it.copy(errorMessage = null) }
                }
            }
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
