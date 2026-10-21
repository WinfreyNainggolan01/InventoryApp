package com.example.inventoryapp.domain.repository

import com.example.inventoryapp.data.remote.dto.CreateProductRequestDto
import com.example.inventoryapp.data.remote.dto.UpdateProductRequestDto
import com.example.inventoryapp.data.remote.dto.UpdateStockRequestDto
import com.example.inventoryapp.data.remote.dto.WebResponseDto
import com.example.inventoryapp.domain.model.Category
import com.example.inventoryapp.domain.model.Product

interface RemoteProductRepository {
//    suspend fun getRemoteProduct(): Result<List<Product>>
    suspend fun getRemoteProduct(search: String? = null, categoryId: Int? = null): Result<List<Product>>
    suspend fun getRemoteProductById(id: Int): Result<Product>
    suspend fun createRemoteProduct(request: CreateProductRequestDto): Result<Product>
    suspend fun updateRemoteProduct(id: Int, request: UpdateProductRequestDto): Result<Product>
    suspend fun deleteRemoteProduct(id: Int): Result<Unit>
    suspend fun updateRemoteStock(id: Int, newStock: Int): Result<Product>

    suspend fun getRemoteCategories(): Result<List<Category>>
}