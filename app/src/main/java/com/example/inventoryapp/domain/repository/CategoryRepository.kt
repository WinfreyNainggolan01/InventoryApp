package com.example.inventoryapp.domain.repository

import com.example.inventoryapp.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository{
    fun getAllCategories(): Flow<List<Category>>
}