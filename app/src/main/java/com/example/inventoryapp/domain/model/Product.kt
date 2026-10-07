package com.example.inventoryapp.domain.model

data class Product(
    val id:Int = 0,
    val name:String,
    val category:String,
    val price:Double,
    val stock:Int
)