package com.example.inventoryapp.data.repository

import com.example.inventoryapp.data.local.dao.ProductDao
import com.example.inventoryapp.data.local.entity.ProductEntity
import com.example.inventoryapp.data.mapper.toDomain
import com.example.inventoryapp.data.mapper.toEntity
import com.example.inventoryapp.domain.model.Product
import com.example.inventoryapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val dao: ProductDao
): ProductRepository {

    override fun getProducts(): Flow<List<Product>> {
        return dao.getProducts().map{
            items -> items.map { it.toDomain() }
        }
    }

    override suspend fun getProductById(id: Int): Product? {
        return dao.getProductById(id)?.toDomain()
    }

    override suspend fun addProduct(product: Product) {
        dao.insertProduct(product.toEntity())
    }

    override suspend fun updateProduct(product: Product) {
        dao.updateProduct(product.toEntity())
    }

    override suspend fun deleteProduct(product: Product) {
        dao.deleteProduct(product.toEntity())
    }
}