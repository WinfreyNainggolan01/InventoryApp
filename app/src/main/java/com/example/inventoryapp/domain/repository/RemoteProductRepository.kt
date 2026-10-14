package com.example.inventoryapp.domain.repository

import com.example.inventoryapp.domain.model.Product

interface RemoteProductRepository {
    suspend fun getRemoteProduct(): Result<List<Product>>
}