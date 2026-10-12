package com.example.inventoryapp.domain.usecase

import com.example.inventoryapp.domain.model.Category
import com.example.inventoryapp.domain.model.Product
import com.example.inventoryapp.domain.repository.CategoryRepository
import com.example.inventoryapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchProductUseCase @Inject constructor(
    private  val repository: ProductRepository
) {
    suspend operator fun invoke(query: String): Flow<List<Product>> {
        return if (query.isBlank()) {
            repository.getProducts()
        } else {
            repository.searchProducts(query.trim())
        }
    }
}