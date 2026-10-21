package com.example.inventoryapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CreateProductRequestDto(
    @SerializedName("name") val name: String,
    @SerializedName("category_id") val categoryId: Int,
    @SerializedName("price") val price: Double,
    @SerializedName("stock") val stock: Int,
    @SerializedName("description") val description: String
)

data class UpdateProductRequestDto(
    @SerializedName("name") val name: String,
    @SerializedName("category_id") val categoryId: Int,
    @SerializedName("price") val price: Double,
    @SerializedName("stock") val stock: Int,
    @SerializedName("description") val description: String
)

data class UpdateStockRequestDto(
    @SerializedName("stock") val stock: Int
)