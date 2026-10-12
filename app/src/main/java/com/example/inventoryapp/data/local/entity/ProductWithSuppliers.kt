package com.example.inventoryapp.data.local.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class ProductWithSuppliers(
    @Embedded val product: ProductEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = ProductSupplierCrossRef::class,
            parentColumn = "productId",
            entityColumn = "supplierId"
        )
    )
    val suppliers: List<SupplierEntity>
)
