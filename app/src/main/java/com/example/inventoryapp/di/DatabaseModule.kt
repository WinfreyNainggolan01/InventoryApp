package com.example.inventoryapp.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.inventoryapp.data.local.dao.CategoryDao
import com.example.inventoryapp.data.local.dao.ProductDao
import com.example.inventoryapp.data.local.dao.TransactionLogDao
import com.example.inventoryapp.data.local.database.AppDatabase
import com.example.inventoryapp.data.local.database.DatabaseSeeder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,

//        seederProvider: Provider<DatabaseSeeder>

    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            
//            .addCallback(object : RoomDatabase.Callback() {
//                override fun onCreate(db: SupportSQLiteDatabase) {
//                    super.onCreate(db)
//
//                    CoroutineScope(Dispatchers.IO).launch {
//                        seederProvider.get().seed()
//                    }
//                }
//            })
            
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    fun provideProductDao(database: AppDatabase): ProductDao = database.productDao()

    @Provides
    fun provideCategoryDao(database: AppDatabase): CategoryDao = database.categoryDao()

    @Provides
    fun provideTransactionLogDao(database: AppDatabase): TransactionLogDao = database.transactionLogDao()
}