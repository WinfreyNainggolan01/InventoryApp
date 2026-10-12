package com.example.inventoryapp.domain.usecase

import com.example.inventoryapp.domain.model.Category
import com.example.inventoryapp.domain.model.Product
import com.example.inventoryapp.domain.repository.CategoryRepository
import com.example.inventoryapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private  val repository: CategoryRepository
) {
    operator fun invoke(): Flow<List<Category>> = repository.getAllCategories()
}