package com.example.inventoryapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.inventoryapp.data.local.entity.ProductEntity
import com.example.inventoryapp.data.local.entity.ProductWithCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY id DESC")
    fun getProducts(): Flow<List<ProductEntity>>

    @Query(
        "SELECT * FROM products " +
                " WHERE 1 = 1" +
                " AND id = :id")
    suspend fun getProductById(id: Int): ProductEntity?

    @Insert
    suspend fun insertProduct(product: ProductEntity)

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Delete
    suspend fun deleteProduct(product: ProductEntity)

    @Transaction
    @Query(
        "SELECT * FROM products " +
                " ORDER BY updatedAt DESC")
    fun getAllProductsWithCategory(): Flow<List<ProductWithCategory>>

    @Transaction
    @Query(
        "SELECT * FROM products " +
                " WHERE 1 = 1 " +
                " AND name LIKE '%' || :query || '%' " +
                " ORDER BY name ASC ")
    fun searchProducts(query: String): Flow<List<ProductWithCategory>>

    @Transaction
    @Query(
        "SELECT * FROM products " +
                " WHERE 1 = 1" +
                " AND categoryId = :categoryId " +
                " ORDER BY name ASC")
    fun getProductsByCategory(categoryId: Int): Flow<List<ProductWithCategory>>

    @Query("SELECT SUM(price * stock) FROM products")
    fun getTotalInventoryValue(): Flow<Double?>

    @Query(
        "SELECT COUNT(*) FROM products " +
                " WHERE 1 = 1 " +
                " AND stock <= :threshold")
    fun getLowStockCount(threshold: Int = 5):Flow<Int>

    @Query(
        "UPDATE products SET stock = :newStock, updatedAt = :timestamp " +
                " WHERE 1 = 1" +
                " AND id = :productId")
    suspend fun updateStock(productId: Int, newStock: Int, timestamp: Long = System.currentTimeMillis())
}