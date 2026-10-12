package com.example.inventoryapp.domain.model

data class Product(
    val id:Int = 0,
    val name:String,
    val categoryId: Int,
    val categoryName:String = "",
    val price:Double,
    val stock:Int,
    val description: String = "",
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)