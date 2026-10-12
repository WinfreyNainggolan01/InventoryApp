package com.example.inventoryapp.data.local.entity

import android.location.Address
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "suppliers")
data class SupplierEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name:String,
    val contactPhone: String = "",
    val email: String = "",
    val address: String = "",
    val isActive: Boolean = true
)
