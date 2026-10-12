package com.example.inventoryapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.inventoryapp.data.local.entity.TransactionLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionLogDao{
    @Query("SELECT * FROM transaction_logs ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentLogs(limit: Int = 20): Flow<List<TransactionLogEntity>>

    @Query("SELECT * FROM transaction_logs WHERE productId = :productId ORDER BY timestamp DESC")
    fun getLogsByProduct(productId: Int): Flow<List<TransactionLogEntity>>

    @Insert
    suspend fun insertLog(log: TransactionLogEntity)

    @Query(
        "SELECT SUM(ABS(quantityChange)) FROM transaction_logs " +
                " WHERE 1 = 1 " +
                " AND type = 'STOCK_OUT' " +
                " AND timestamp " +
                " BETWEEN :startTime AND :endTime")
    fun getTotalStockOut(startTime: Long, endTime: Long): Int?
}
