package com.example.inventoryapp.domain.repository

import com.example.inventoryapp.domain.model.TransactionLog

interface TransactionLogRepository {
    suspend fun insertLog(transactionLog: TransactionLog)
}