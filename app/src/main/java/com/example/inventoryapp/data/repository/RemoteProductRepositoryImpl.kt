package com.example.inventoryapp.data.repository

import com.example.inventoryapp.data.remote.api.ProductApiService
import com.example.inventoryapp.data.remote.dto.CreateProductRequestDto
import com.example.inventoryapp.data.remote.dto.ProductDto
import com.example.inventoryapp.data.remote.dto.UpdateProductRequestDto
import com.example.inventoryapp.data.remote.dto.UpdateStockRequestDto
import com.example.inventoryapp.data.remote.dto.toDomain
import com.example.inventoryapp.domain.model.Category
import com.example.inventoryapp.domain.model.Product
import com.example.inventoryapp.domain.repository.RemoteProductRepository
import javax.inject.Inject

class RemoteProductRepositoryImpl @Inject constructor(
    private val productApiService: ProductApiService
) : RemoteProductRepository {

//    override suspend fun getRemoteProduct(): Result<List<Product>> {
//        return try {
//            val response = productApiService.getProducts()
//            if (response.isSuccessful && response.body() != null) {
//                val dtoList = response.body()?.data ?: emptyList()
//                val domainList = dtoList.map { it .toDomain() }
//                Result.success(domainList)
//            } else {
//                Result.failure(Exception("Gagal mengambil data product dari server (HTTP ${response.code()})"))
//            }
//        } catch (e: Exception) {
//            Result.failure(Exception("Tidak dapat terhubung ke server : ${e.localizedMessage}"))
//        }
//    }

    override suspend fun getRemoteProduct(search: String?, categoryId: Int?): Result<List<Product>> {
        return try {
            val response = productApiService.getProducts(search, categoryId)
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

    override suspend fun getRemoteProductById(id: Int): Result<Product> {
        return try {
            val response = productApiService.getProductById(id)
            val dto = response.body()?.data
            if (response.isSuccessful && dto != null) {
                Result.success(dto.toDomain())
            } else {
                Result.failure(Exception("Gagal mengambil data product dari server (HTTP ${response.code()})"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Tidak dapat terhubung ke server : ${e.localizedMessage}"))
        }
    }

    override suspend fun deleteRemoteProduct(id: Int): Result<Unit> {
        return try {
            val response = productApiService.deleteProduct(id)
            val dto = response.body()?.data
            if (response.isSuccessful && dto != null) {
                Result.success(Unit)
            } else {
                val errorMsg = response.body()?.message ?: "Gagal menghapus produk (HTTP ${response.code()})"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Gagal menghapus produk: ${e.localizedMessage}"))
        }
    }

    override suspend fun createRemoteProduct(request: CreateProductRequestDto): Result<Product> {
        return try {
            val response = productApiService.createProduct(request)
            val dto = response.body()?.data
            if(response.isSuccessful && dto != null) {
                Result.success(dto.toDomain())
            } else {
                val errorMsg = response.body()?.message ?: "Gagal menambah produk (HTTP ${response.code()})"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Gagal menambah produk: ${e.localizedMessage}"))
        }
    }

    override suspend fun updateRemoteProduct(
        id: Int,
        request: UpdateProductRequestDto
    ): Result<Product> {
        return try {
            val response = productApiService.updateProduct(id, request)
            val dto = response.body()?.data
            if (response.isSuccessful && dto != null) {
                Result.success(dto.toDomain())
            } else {
                val errorMsg = response.body()?.message ?: "Gagal mengupdate produk ( HTTP ${response.code()}"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Gagal mengupdate produk: ${e.localizedMessage}"))
        }
    }

    override suspend fun updateRemoteStock(id: Int, newStock: Int): Result<Product> {
        return try {
            val response = productApiService.updateStock(id, UpdateStockRequestDto(stock = newStock))
            val dto = response.body()?.data
            if (response.isSuccessful && dto != null) {
                Result.success(dto.toDomain())
            } else {
                val errorMsg = response.body()?.message ?: "Gagal update stock produk (HTTP ${response.code()}"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Gagal update stock produk: ${e.localizedMessage}"))
        }
    }

    override suspend fun getRemoteCategories(): Result<List<Category>> {
        return try {
            val response = productApiService.getCategories()
            if (response.isSuccessful && response.body() != null) {
                val dtoList = response.body()?.data ?: emptyList()
                val domainList = dtoList.map { it.toDomain() }
                Result.success(domainList)
            } else {
                val errorMsg = response.body()?.message ?: "Gagal mengambil data kategori"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Gagal mengambil kategori: ${e.localizedMessage}"))
        }
    }


}