package com.example.inventoryapp.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ProductWithCategory(
    @Embedded val product: ProductEntity,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: CategoryEntity
)