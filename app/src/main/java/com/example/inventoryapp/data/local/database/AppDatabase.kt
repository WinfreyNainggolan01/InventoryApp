package com.example.inventoryapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.inventoryapp.data.local.dao.CategoryDao
import com.example.inventoryapp.data.local.dao.ProductDao
import com.example.inventoryapp.data.local.dao.TransactionLogDao
import com.example.inventoryapp.data.local.entity.CategoryEntity
import com.example.inventoryapp.data.local.entity.ProductEntity
import com.example.inventoryapp.data.local.entity.ProductSupplierCrossRef
import com.example.inventoryapp.data.local.entity.SupplierEntity
import com.example.inventoryapp.data.local.entity.TransactionLogEntity

@Database(
    entities=[
        ProductEntity::class,
        CategoryEntity::class,
        SupplierEntity::class,
        ProductSupplierCrossRef::class,
        TransactionLogEntity::class
    ],
    version=2,
    exportSchema = false
)

abstract class AppDatabase: RoomDatabase(){
    abstract fun productDao(): ProductDao
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionLogDao(): TransactionLogDao

    companion object{
        const val DATABASE_NAME = "inventory_db"

        val MIGRATION_1_2 = object : Migration(1,2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS categories" +
                        " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        " name TEXT NOT NULL," +
                        " description TEXT NOT NULL DEFAULT ''," +
                        " iconName TEXT NOT NULL DEFAULT '')")

                db.execSQL("ALTER TABLE products ADD COLUMN categoryId INTEGER NOT NULL DEFAULT 1")
                db.execSQL("ALTER TABLE products ADD COLUMN description TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE products ADD COLUMN createdAt INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE products ADD COLUMN updatedAt INTEGER NOT NULL DEFAULT 0")

                db.execSQL("CREATE TABLE IF NOT EXISTS suppliers" +
                        " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        " name TEXT NOT NULL," +
                        " contactPhone TEXT NOT NULL DEFAULT ''," +
                        " email TEXT NOT NULL DEFAULT ''," +
                        " address TEXT NOT NULL DEFAULT ''," +
                        " isActive INTEGER NOT NULL DEFAULT 1)")

                db.execSQL("CREATE TABLE IF NOT EXISTS product_suppliers" +
                        " (productId INTEGER NOT NULL," +
                        " supplierId INTEGER NOT NULL," +
                        " lastSupplyDate INTEGER," +
                        " unitCost REAL NOT NULL DEFAULT 0.0," +
                        " PRIMARY KEY (productId, supplierId))")

                db.execSQL("CREATE TABLE IF NOT EXISTS transaction_logs" +
                        " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        " productId INTEGER NOT NULL," +
                        " type TEXT NOT NULL," +
                        " quantityChange INTEGER NOT NULL," +
                        " stockBefore INTEGER NOT NULL," +
                        " stockAfter INTEGER NOT NULL," +
                        " note TEXT NOT NULL DEFAULT ''," +
                        " timestamp INTEGER NOT NULL DEFAULT 0)")

                db.execSQL("INSERT INTO categories (id, name)" +
                        "VALUES (1, 'General'")


            }
        }
    }
}