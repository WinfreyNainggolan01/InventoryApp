package com.example.inventoryapp.data.remote.api

import com.example.inventoryapp.data.remote.dto.ProductDto
import com.example.inventoryapp.data.remote.dto.WebResponseDto
import retrofit2.Response
import retrofit2.http.GET

interface ProductApiService {

    @GET("api/v1/products")
    suspend fun getProducts(): Response<WebResponseDto<List<ProductDto>>>
}