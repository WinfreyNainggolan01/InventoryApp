package com.example.inventoryapp.domain.repository

import com.example.inventoryapp.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository{
    fun getProducts(): Flow<List<Product>>
    suspend fun getProductById(id: Int): Product?
    suspend fun addProduct(product: Product)
    suspend fun updateProduct(product: Product)
    suspend fun deleteProduct(product: Product)
}