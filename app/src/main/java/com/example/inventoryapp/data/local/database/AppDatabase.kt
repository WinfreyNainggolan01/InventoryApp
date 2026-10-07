package com.example.inventoryapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.inventoryapp.data.local.dao.ProductDao
import com.example.inventoryapp.data.local.entity.ProductEntity

@Database(entities=[ProductEntity::class], version=1, exportSchema = false)

abstract class AppDatabase: RoomDatabase(){
    abstract fun productDao(): ProductDao

    companion object{
        const val DATABASE_NAME = "inventory_db"
    }
}