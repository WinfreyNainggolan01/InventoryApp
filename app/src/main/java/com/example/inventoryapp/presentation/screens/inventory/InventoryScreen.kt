package com.example.inventoryapp.presentation.screens.inventory

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.inventoryapp.domain.model.Product
import com.example.inventoryapp.presentation.components.*
import com.example.inventoryapp.presentation.viewmodel.DataSouceOption
import com.example.inventoryapp.presentation.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
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
            if (uiState.selectedDataSource == DataSouceOption.LOCAL) {
                FloatingActionButton(onClick = onNavigateToAddProduct) {
                    Icon(Icons.Default.Add, contentDescription = "Tambah Produk Lokal DB")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Inventory",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Bold
                )

                if (uiState.selectedDataSource == DataSouceOption.REMOTE) {
                    IconButton(onClick = { viewModel.fetchRemoteProducts() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Muat Ulang Data Server",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // TAB SELECTOR
            PrimaryTabRow(
                selectedTabIndex = if (uiState.selectedDataSource == DataSouceOption.LOCAL) 0 else 1,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Tab(
                    selected =  uiState.selectedDataSource == DataSouceOption.LOCAL,
                    onClick = { viewModel.setDataSource(DataSouceOption.LOCAL)},
                    text = { Text("Data Lokal (Room)") },
                    icon = { Icon(Icons.Default.Storage, contentDescription = null)}
                )

                Tab(
                    selected =  uiState.selectedDataSource == DataSouceOption.REMOTE,
                    onClick = { viewModel.setDataSource(DataSouceOption.REMOTE)},
                    text = { Text("Data Server (API)") },
                    icon = { Icon(Icons.Default.Cloud, contentDescription = null)}
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // TAB 1 : LOKAL
            if (uiState.selectedDataSource == DataSouceOption.LOCAL) {
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

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${uiState.products.size} produk lokal ditemukan",
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
                                "Belum ada produk lokal.",
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

            // TAB 2: DATA SERVER

            if (uiState.selectedDataSource == DataSouceOption.REMOTE) {
                if(uiState.isRemoteLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Mengambil data dari server")
                        }
                    }
                }

                if(!uiState.isRemoteLoading && uiState.remoteErrorMessage != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Gagal terhubung ke server",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = uiState.remoteErrorMessage ?: "Error tidak diketahui",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = { viewModel.fetchRemoteProducts() },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                                ) { 
                                    Icon(Icons.Default.Refresh, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Coba lagi")
                                }
                            }
                        }
                    }
                }
                
                if(!uiState.isRemoteLoading && uiState.remoteErrorMessage == null && uiState.remoteProducts.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Tidak ada produk di server",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (!uiState.isRemoteLoading && uiState.remoteProducts.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${uiState.remoteProducts.size} produk ditemukan di Supabase",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFE8F5E9)
                        ) {
                            Text(
                                text = "Live Server",
                                color = Color(0xFF2E7D32),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(
                            items = uiState.remoteProducts,
                            key = { "remote_${it.id}" }
                        ) { product ->
                            ProductCard(
                                product = product,
                                onClick = { null },
                                onStockAdjust = { null },
                                onDelete = { null }
                            )
                        }
                    }
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