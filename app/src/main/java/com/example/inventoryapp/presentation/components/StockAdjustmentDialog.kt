package com.example.inventoryapp.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.inventoryapp.domain.model.Product

@Composable
fun StockAdjustmentDialog(
    product: Product,
    onDismiss: () -> Unit,
    onConfirm: (change: Int, note: String) -> Unit
) {
    var quantityText by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var isStockIn by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Sesuaikan Stok") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Product: ${product.name}")
                Text("Stock saat ini: ${product.stock}")

                Text("Tipe:")
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilterChip(
                        selected = isStockIn,
                        onClick = { isStockIn = true },
                        label = { Text("Stock Masuk (+)") }
                    )

                    FilterChip(
                        selected = !isStockIn,
                        onClick = { isStockIn = false },
                        label = { Text("Stock Keluar (-)") }
                    )
                }

                OutlinedTextField(
                    value = quantityText,
                    onValueChange = { quantityText = it.filter { c -> c.isDigit() } },
                    label = { Text("Jumlah") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Catatan (opsional)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val qty = quantityText.toIntOrNull() ?: 0
                    if (qty > 0) {
                        val change = if (isStockIn) qty else -qty
                        onConfirm(change, note)
                        onDismiss()
                    }
                },
                enabled = quantityText.toIntOrNull()?.let { it > 0 } == true
            ) {
                Text("Konfirmasi")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}
