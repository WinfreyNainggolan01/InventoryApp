package com.example.inventoryapp.data.mapper

import com.example.inventoryapp.data.local.entity.*
import com.example.inventoryapp.data.local.entity.ProductWithCategory
import com.example.inventoryapp.domain.model.*

fun ProductEntity.toDomain(categoryName: String = ""): Product {
    return Product(
        id = this.id,
        name = this.name,
        categoryId = this.categoryId,
        categoryName = categoryName,
        price = this.price,
        stock = this.stock,
        description = this.description,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        id = this.id,
        name = this.name,
        categoryId = this.categoryId,
        price = this.price,
        stock = this.stock,
        description = this.description,
        createdAt = this.createdAt,
        updatedAt = System.currentTimeMillis()
    )
}

fun ProductWithCategory.toDomain(): Product {
    return Product(
        id = this.product.id,
        name = this.product.name,
        categoryId = this.product.categoryId,
        categoryName = this.category.name,
        price = this.product.price,
        stock = this.product.stock,
        description = this.product.description,
        createdAt = this.product.createdAt,
        updatedAt = this.product.updatedAt
    )
}

fun CategoryEntity.toDomain() = Category(id, name, description, iconName)
fun Category.toEntity() = CategoryEntity(id, name, description, iconName)

fun TransactionLogEntity.toDomain(productName: String = "") = TransactionLog(
    id = id,
    productId = productId,
    productName = productName,
    type = type,
    quantityChange = quantityChange,
    stockBefore = stockBefore,
    stockAfter = stockAfter,
    note = note,
    timestamp = timestamp
)

fun TransactionLog.toEntity() = TransactionLogEntity(
    id = id,
    productId = productId,
    type = type,
    quantityChange = quantityChange,
    stockBefore = stockBefore,
    stockAfter = stockAfter,
    note = note,
    timestamp = timestamp
)