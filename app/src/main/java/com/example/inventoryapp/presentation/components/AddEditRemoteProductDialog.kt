package com.example.inventoryapp.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.inventoryapp.domain.model.Category
import com.example.inventoryapp.domain.model.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditRemoteProductDialog(
    productToEdit: Product? = null,
    categories: List<Category>,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onSave: (name: String, categoryId: Int, price: Double, stock: Int, desctiption: String) -> Unit
){
    val isEditMode = productToEdit != null

    var name by remember { mutableStateOf(productToEdit?.name ?: "") }
    var priceText by remember { mutableStateOf(productToEdit?.price?.toInt()?.toString() ?: "")}
    var stockText by remember { mutableStateOf(productToEdit?.stock?.toString() ?: "")}
    var description by remember { mutableStateOf(productToEdit?.description ?: "") }

    var selectedCategory by remember {
        mutableStateOf(
            categories.find { it.id == productToEdit?.categoryId } ?: categories.firstOrNull()
        )
    }

    var expandedDropdown by remember { mutableStateOf(false) }
    var validationError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        title = {
            Text(
                text = if (isEditMode) "Edit Produk " else "Tambah Produk",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama Produk *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // DROPDOWN KATEGORI
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = selectedCategory?.name ?: if (categories.isEmpty()) "Memuat kategori..." else "Pilih Kategori",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Kategori Produk *") },
                        trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable {
                                expandedDropdown = true
                            }
                    )
                    DropdownMenu(
                        expanded = expandedDropdown,
                        onDismissRequest = { expandedDropdown = false }
                    ) {
                        if (categories.isEmpty()) {
                            DropdownMenuItem(
                                text = { Text("Tidak ada kategori tersedia") },
                                onClick = { expandedDropdown = false },
                            )
                        } else {
                            categories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category.name) },
                                    onClick = {
                                        selectedCategory = category
                                        expandedDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = priceText,
                    onValueChange = { priceText = it },
                    label = { Text("Harga (Rp) *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = stockText,
                    onValueChange = { stockText = it },
                    label = { Text("Stok Awal *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Deskripsi (Opsional)") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                validationError?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        confirmButton = {
            Button(
                enabled = !isLoading,
                onClick = {
                    val price = priceText.toDoubleOrNull()
                    val stock = stockText.toIntOrNull()
                    val catId = selectedCategory?.id ?: 0

                    if (name.isBlank()) {
                        validationError = "Nama produk tidak boleh kosong"
                    } else if (catId <= 0) {
                        validationError = "Pilih kategori terlebih dahulu"
                    } else if (price == null || price < 0) {
                        validationError = "Harga harus berupa angka valid"
                    } else if (stock == null || stock < 0) {
                        validationError = "Stok harus berupa angka valid"
                    } else {
                        validationError = null
                        onSave(name.trim(), catId, price, stock, description.trim())
                    }
                }
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text(if (isEditMode) "Simpan Perubahan" else "Simpan ke Server")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isLoading) {
                Text("Batal")
            }
        }
    )

}