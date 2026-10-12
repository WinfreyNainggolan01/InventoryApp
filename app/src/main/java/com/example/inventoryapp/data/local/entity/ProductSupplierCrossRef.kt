package com.example.inventoryapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.example.inventoryapp.domain.model.Product

@Entity(
    tableName = "product_supplier",
    primaryKeys = ["productId", "supplierId"],
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SupplierEntity::class,
            parentColumns = ["id"],
            childColumns = ["supplierId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["productId"]),
        Index(value = ["supplierId"])
    ]
)

data class ProductSupplierCrossRef(
    val productId: Int,
    val supplierId: Int,
    val lastSupplyDate: Long? = null,
    val unitCost: Double = 0.0
)
