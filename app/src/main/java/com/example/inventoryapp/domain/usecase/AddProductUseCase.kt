package com.example.inventoryapp.domain.usecase

import com.example.inventoryapp.domain.model.Product
import com.example.inventoryapp.domain.repository.ProductRepository
import javax.inject.Inject

class AddProductUseCase @Inject constructor(
    private  val repository: ProductRepository
) {
    suspend operator fun invoke(product: Product) {
        require(product.name.isNotBlank()) {"Nama produk tidak boleh kosong"}
        require(product.price >= 0) {"Harga tidak boleh negatif"}
        require(product.stock >= 0) {"Stock tidak boleh negatif"}
        repository.addProduct(product)
    }

    suspend fun execute(product: Product) {
        require(product.name.isNotBlank()) { "Nama produk tidak boleh kosong" }
        require (product.price >= 0) { "Harga tidak boleh negatif" }
        require (product.stock >= 0) { "Stock tidak boleh negatif" }
        repository.addProduct(product)
    }
}