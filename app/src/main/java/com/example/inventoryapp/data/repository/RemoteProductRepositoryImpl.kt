package com.example.inventoryapp.data.repository

import com.example.inventoryapp.data.remote.api.ProductApiService
import com.example.inventoryapp.data.remote.dto.ProductDto
import com.example.inventoryapp.data.remote.dto.toDomain
import com.example.inventoryapp.domain.model.Product
import com.example.inventoryapp.domain.repository.RemoteProductRepository
import javax.inject.Inject

class RemoteProductRepositoryImpl @Inject constructor(
    private val apiService: ProductApiService
) : RemoteProductRepository {

    override suspend fun getRemoteProduct(): Result<List<Product>> {
        return try {
            val response = apiService.getProducts()
            if (response.isSuccessful && response.body() != null) {
                val dtoList = response.body()?.data ?: emptyList()
                val domainList = dtoList.map { it .toDomain() }
                Result.success(domainList)
            } else {
                Result.failure(Exception("Gagal mengambil data product dari server (HTTP ${response.code()})"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Tidak dapat terhubung ke server : ${e.localizedMessage}"))
        }
    }

}