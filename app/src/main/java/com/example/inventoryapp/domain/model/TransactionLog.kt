package com.example.inventoryapp.domain.model

import com.example.inventoryapp.data.local.entity.TransactionType

data class TransactionLog(
    val id:Int = 0,
    val productId:Int,
    val productName: String = "",
    val type: TransactionType,
    val quantityChange: Int,
    val stockBefore: Int,
    val stockAfter: Int,
    val note: String,
    val timestamp: Long = 0L,
)