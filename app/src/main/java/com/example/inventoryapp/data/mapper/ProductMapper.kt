package com.example.inventoryapp.data.mapper

import com.example.inventoryapp.data.local.entity.ProductEntity
import com.example.inventoryapp.domain.model.Product

fun ProductEntity.toDomain(): Product {
    return Product(
        id = this.id,
        name = this.name,
        category = this.category,
        price = this.price,
        stock = this.stock
    )
}

fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        id = this.id,
        name = this.name,
        category = this.category,
        price = this.price,
        stock = this.stock
    )
}
