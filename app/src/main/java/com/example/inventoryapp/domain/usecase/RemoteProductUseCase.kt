package com.example.inventoryapp.domain.usecase

import com.example.inventoryapp.data.remote.dto.CreateProductRequestDto
import com.example.inventoryapp.data.remote.dto.UpdateProductRequestDto
import com.example.inventoryapp.domain.model.Category
import com.example.inventoryapp.domain.model.Product
import com.example.inventoryapp.domain.repository.RemoteProductRepository
import javax.inject.Inject

class CreateRemoteProductUseCase @Inject constructor(
    private val repository: RemoteProductRepository
){
    suspend fun execute(request: CreateProductRequestDto): Result<Product> {
        return repository.createRemoteProduct(request)
    }
}

class UpdateRemoteProductUseCase @Inject constructor(
    private val repository: RemoteProductRepository
) {
    suspend fun execute(id: Int, request: UpdateProductRequestDto): Result<Product> {
        return repository.updateRemoteProduct(id, request)
    }
}

class DeleteRemoteProductUseCase @Inject constructor(
    private val repository: RemoteProductRepository
) {
    suspend fun execute(id: Int): Result<Unit> {
        return repository.deleteRemoteProduct(id)
    }
}

class UpdateRemoteStockUseCase @Inject constructor(
    private val repository: RemoteProductRepository
) {
    suspend fun execute(id: Int, newStock: Int): Result<Product> {
        return repository.updateRemoteStock(id, newStock)
    }
}

class GetRemoteCategoriesUseCase @Inject constructor(
    private val repository: RemoteProductRepository
) {
    suspend fun execute(): Result<List<Category>> {
        return repository.getRemoteCategories()
    }
}