package com.example.inventoryapp.data.repository

import com.example.inventoryapp.data.local.dao.TransactionLogDao
import com.example.inventoryapp.data.mapper.toEntity
import com.example.inventoryapp.domain.model.TransactionLog
import com.example.inventoryapp.domain.repository.TransactionLogRepository
import javax.inject.Inject

class TransactionLogRepositoryImpl @Inject constructor(
    private val dao: TransactionLogDao
) : TransactionLogRepository {

    override suspend fun insertLog(transactionLog: TransactionLog) {
        dao.insertLog(transactionLog.toEntity())
    }
}
