package com.example.inventoryapp.data.remote.api

import com.example.inventoryapp.data.remote.dto.CategoryDto
import com.example.inventoryapp.data.remote.dto.CreateProductRequestDto
import com.example.inventoryapp.data.remote.dto.ProductDto
import com.example.inventoryapp.data.remote.dto.UpdateProductRequestDto
import com.example.inventoryapp.data.remote.dto.UpdateStockRequestDto
import com.example.inventoryapp.data.remote.dto.WebResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApiService {

//    @GET("api/v1/products")
//    suspend fun getProducts(): Response<WebResponseDto<List<ProductDto>>>

    @GET("api/v1/products")
    suspend fun getProducts(
        @Query("search") search: String? = null,
        @Query("category_id") categoryId: Int? = null,
    ): Response<WebResponseDto<List<ProductDto>>>

    @GET("api/v1/categories")
    suspend fun getCategories(): Response<WebResponseDto<List<CategoryDto>>>

    @GET("api/v1/products/{id}")
    suspend fun getProductById(
        @Path("id") id: Int
    ): Response<WebResponseDto<ProductDto>>

    @POST("api/v1/products")
    suspend fun createProduct(
        @Body request: CreateProductRequestDto
    ): Response<WebResponseDto<ProductDto>>

    @PUT("api/v1/products/{id}")
    suspend fun updateProduct(
        @Path("id") id: Int,
        @Body request: UpdateProductRequestDto
    ): Response<WebResponseDto<ProductDto>>

    @DELETE("api/v1/products/{id}")
    suspend fun deleteProduct(
        @Path("id") id: Int
    ): Response<WebResponseDto<Map<String, Int>>>

    @PATCH("api/v1/products/{id}/stock")
    suspend fun updateStock(
        @Path("id") id: Int,
        @Body request: UpdateStockRequestDto
    ): Response<WebResponseDto<ProductDto>>
    
}