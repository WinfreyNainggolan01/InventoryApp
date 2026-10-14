package com.example.inventoryapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WebResponseDto<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: T?
)