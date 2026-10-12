package com.example.inventoryapp.presentation.screens.inventory

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.inventoryapp.domain.model.Product
import com.example.inventoryapp.presentation.components.*
import com.example.inventoryapp.presentation.viewmodel.ProductViewModel

@Composable
fun InventoryScreen(
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToAddProduct: () -> Unit,
    viewModel: ProductViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showStockDialog by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAddProduct) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Produk")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = "Inventory",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )

            InventorySearchBar(
                query = uiState.searchQuery,
                onQueryChanged = viewModel::onSearchQueryChanged
            )

            if (uiState.categories.isNotEmpty()) {
                CategoryFilterChips(
                    categories = uiState.categories,
                    selectedCategoryId = uiState.selectedCategoryId,
                    onCategorySelected = viewModel::onCategoryFilterChanged
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${uiState.products.size} produk ditemukan",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            if (!uiState.isLoading && uiState.products.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = if (uiState.searchQuery.isNotBlank())
                            "Produk '${uiState.searchQuery}' tidak ditemukan"
                        else
                            "Belum ada produk",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(
                    items = uiState.products,
                    key = { it.id }
                ) { product ->
                    ProductCard(
                        product = product,
                        onClick = { onNavigateToDetail(product.id) },
                        onStockAdjust = {
                            selectedProduct = product
                            showStockDialog = true
                        },
                        onDelete = {
                            viewModel.deleteProduct(product)
                        }
                    )
                }
            }
        }
    }

    if (showStockDialog && selectedProduct != null) {
        StockAdjustmentDialog(
            product = selectedProduct!!,
            onDismiss = { showStockDialog = false },
            onConfirm = { change, note ->
                viewModel.updateStock(selectedProduct!!, change, note)
            }
        )
    }
}