package com.example.inventoryapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

enum class TransactionType {
    STOCK_IN,
    STOCK_OUT,
    ADJUSTMENT,
    INITIAL
}

@Entity(
    tableName = "transaction_logs",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["productId"])]
)
data class TransactionLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val productId: Int,
    val type: TransactionType,
    val quantityChange: Int,
    val stockBefore: Int,
    val stockAfter: Int,
    val note: String,
    val timestamp: Long = System.currentTimeMillis(),
)
