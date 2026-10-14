package com.example.inventoryapp.data.remote.dto

import com.example.inventoryapp.domain.model.Product
import com.google.gson.annotations.SerializedName

data class ProductDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("category_id") val categoryId: Int,
    @SerializedName("category_name") val categoryName: String?,
    @SerializedName("price") val price: Double,
    @SerializedName("stock") val stock: Int,
    @SerializedName("description") val description: String?,
    @SerializedName("created_at") val createdAt: Long,
    @SerializedName("updated_at") val updatedAt: Long
)

fun ProductDto.toDomain(): Product {
    return Product(
        id = this.id,
        name = this.name,
        categoryId = this.categoryId,
        categoryName = this.categoryName ?: "Umum",
        price = this.price,
        stock = this.stock,
        description = this.description ?: "",
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}