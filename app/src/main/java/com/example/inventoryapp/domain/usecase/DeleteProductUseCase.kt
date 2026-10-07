package com.example.inventoryapp.domain.usecase

import com.example.inventoryapp.domain.model.Product
import com.example.inventoryapp.domain.repository.ProductRepository
import javax.inject.Inject

class DeleteProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(product: Product) {
        repository.deleteProduct(product)
    }
}