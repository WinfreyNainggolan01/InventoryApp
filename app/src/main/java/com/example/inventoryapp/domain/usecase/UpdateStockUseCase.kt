package com.example.inventoryapp.domain.usecase

import com.example.inventoryapp.data.local.entity.TransactionType
import com.example.inventoryapp.domain.model.Category
import com.example.inventoryapp.domain.model.Product
import com.example.inventoryapp.domain.model.TransactionLog
import com.example.inventoryapp.domain.repository.CategoryRepository
import com.example.inventoryapp.domain.repository.ProductRepository
import com.example.inventoryapp.domain.repository.TransactionLogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateStockUseCase @Inject constructor(
    private  val productRepository: ProductRepository,
    private  val logRepository: TransactionLogRepository
) {
    sealed class Result {
        object Success : Result()
        data class Error(val message: String) : Result()
    }

    suspend operator fun invoke(
        productId: Int,
        currentStock: Int,
        change: Int,
        note: String = "",
        type: TransactionType = if (change > 0) TransactionType.STOCK_IN else TransactionType.STOCK_OUT
    ): Result {
        val newStock = currentStock + change

        if (newStock < 0) {
            return Result.Error("Stok tidak cukup. Stok saat ini: $currentStock, Perubahan: $change")
        }

        productRepository.updateStock(productId, newStock)

        logRepository.insertLog(
            TransactionLog(
                productId = productId,
                type = type,
                quantityChange = change,
                stockBefore = currentStock,
                stockAfter = newStock,
                note = note
            )
        )

        return Result.Success
    }
}