package com.example.inventoryapp.data.local.database

import com.example.inventoryapp.data.local.dao.CategoryDao
import com.example.inventoryapp.data.local.dao.ProductDao
import com.example.inventoryapp.data.local.entity.CategoryEntity
import com.example.inventoryapp.data.local.entity.ProductEntity
import javax.inject.Inject

class DatabaseSeeder @Inject constructor(
    private val categoryDao: CategoryDao,
    private val productDao: ProductDao
) {
    suspend fun seed(){
        val categories = listOf(
            CategoryEntity(id = 1, name = "Electronics", iconName = "devices"),
            CategoryEntity(id = 2, name = "Food & Beverage", iconName = "restaurant"),
            CategoryEntity(id = 3, name = "Clothing", iconName = "checkroom"),
            CategoryEntity(id = 4, name = "Books", iconName = "book"),
            CategoryEntity(id = 5, name = "General", iconName = "category")
        )
        categoryDao.insertCategories(categories)

        val sampleProducts = listOf(
            ProductEntity(name = "Laptop Asus", categoryId = 1, price = 8500000.0, stock = 10,
                description = "Laptop untuk kebutuhan kantor dan belajar"),
            ProductEntity(name = "Mouse Wireless", categoryId = 1, price = 150000.0, stock = 25),
            ProductEntity(name = "Indomie Goreng", categoryId = 2, price = 3500.0, stock = 100),
            ProductEntity(name = "Kaos Polos", categoryId = 3, price = 75000.0, stock = 50),
            ProductEntity(name = "Clean Code Book", categoryId = 4, price = 120000.0, stock = 5)
        )

        sampleProducts.forEach { productDao.insertProduct(it) }
    }
}