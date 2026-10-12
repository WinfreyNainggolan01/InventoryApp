package com.example.inventoryapp.data.repository

import com.example.inventoryapp.data.local.dao.CategoryDao
import com.example.inventoryapp.data.mapper.toDomain
import com.example.inventoryapp.domain.model.Category
import com.example.inventoryapp.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val dao: CategoryDao
) : CategoryRepository {

    override fun getAllCategories(): Flow<List<Category>> {
        return dao.getAllCategories().map { entities ->
            entities.map { it.toDomain() }
        }
    }
}
