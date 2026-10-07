package com.example.inventoryapp.domain.usecase

import com.example.inventoryapp.domain.model.Product
import com.example.inventoryapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(): Flow<List<Product>> {
        return repository.getProducts()
    }
}