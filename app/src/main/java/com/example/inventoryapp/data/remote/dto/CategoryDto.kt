package com.example.inventoryapp.data.remote.dto

import com.example.inventoryapp.domain.model.Category
import com.google.gson.annotations.SerializedName

data class CategoryDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("icon_name") val iconName: String?
)

fun CategoryDto.toDomain(): Category {
    return Category(
        id = this.id,
        name = this.name,
        description = this.description ?: "",
        iconName = this.iconName ?: "default"
    )
}