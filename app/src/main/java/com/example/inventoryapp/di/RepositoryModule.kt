package com.example.inventoryapp.di

import com.example.inventoryapp.data.repository.CategoryRepositoryImpl
import com.example.inventoryapp.data.repository.ProductRepositoryImpl
import com.example.inventoryapp.data.repository.RemoteProductRepositoryImpl
import com.example.inventoryapp.data.repository.TransactionLogRepositoryImpl
import com.example.inventoryapp.domain.repository.CategoryRepository
import com.example.inventoryapp.domain.repository.ProductRepository
import com.example.inventoryapp.domain.repository.RemoteProductRepository
import com.example.inventoryapp.domain.repository.TransactionLogRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        impl: ProductRepositoryImpl
    ): ProductRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        impl: CategoryRepositoryImpl
    ): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindTransactionLogRepository(
        impl: TransactionLogRepositoryImpl
    ): TransactionLogRepository

    @Binds
    @Singleton
    abstract fun bindRemoteProductRepository(
        impl: RemoteProductRepositoryImpl
    ): RemoteProductRepository
}