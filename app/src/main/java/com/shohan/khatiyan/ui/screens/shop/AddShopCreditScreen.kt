package com.shohan.khatiyan.ui.screens.shop

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shohan.khatiyan.data.local.entities.ShopCreditEntity
import com.shohan.khatiyan.data.local.entities.ShopCreditItemEntity
import com.shohan.khatiyan.ui.components.DatePickerTextField
import com.shohan.khatiyan.ui.components.KhatiyanOutlinedTextField
import com.shohan.khatiyan.ui.screens.viewmodels.MainViewModel
import com.shohan.khatiyan.util.CurrencyFormatter

data class ItemInputRow(
    var itemName: String = "",
    var amountText: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddShopCreditScreen(
    shopId: Long,
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val shopState by viewModel.repository.getShopByIdFlow(shopId).collectAsState(initial = null)
    var directTotalText by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var note by remember { mutableStateOf("") }

    val itemRows = remember { mutableStateListOf<ItemInputRow>() }
    var amountError by remember { mutableStateOf<String?>(null) }

    // Calculated total from line items if added
    val itemsTotal = itemRows.sumOf { it.amountText.toDoubleOrNull() ?: 0.0 }
    val isUsingItemRows = itemRows.isNotEmpty()
    val grandTotalAmount = if (isUsingItemRows) itemsTotal else (directTotalText.toDoubleOrNull() ?: 0.0)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${shopState?.name ?: "দোকানে"} বাকী যোগ করুন") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
        ) {
            item {
                DatePickerTextField(
                    selectedDate = selectedDate,
                    onDateSelected = { }
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "কেনাকাটার তালিকা (পণ্যভিত্তিক বাকী)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Dynamic Item Rows
            itemsIndexed(itemRows) { index, row ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        KhatiyanOutlinedTextField(
                            value = row.itemName,
                            onValueChange = {
                                itemRows[index] = row.copy(itemName = it)
                            },
                            label = "পণ্যের নাম (যেমন: চাল)",
                            modifier = Modifier.weight(1.5f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        KhatiyanOutlinedTextField(
                            value = row.amountText,
                            onValueChange = {
                                itemRows[index] = row.copy(amountText = it)
                            },
                            label = "মূল্য (৳)",
                            isNumber = true,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { itemRows.removeAt(index) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Remove",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = { itemRows.add(ItemInputRow()) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("পণ্য যোগ করুন (যেমন: চাল, তেল, ডাল)")
                }
                Spacer(modifier = Modifier.height(16.dp))

                if (itemRows.isEmpty()) {
                    KhatiyanOutlinedTextField(
                        value = directTotalText,
                        onValueChange = {
                            directTotalText = it
                            if (it.isNotBlank()) amountError = null
                        },
                        label = "মোট বাকীর পরিমাণ (৳) *",
                        isNumber = true,
                        errorMessage = amountError
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "সর্বমোট কেনাকাটা:",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = CurrencyFormatter.formatAmount(itemsTotal),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                KhatiyanOutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = "নোট (ঐচ্ছিক)"
                )
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (grandTotalAmount <= 0) {
                            amountError = "দয়া করে সঠিক বাকীর পরিমাণ লিখুন"
                        } else {
                            val grandTotalPaisa = (grandTotalAmount * 100).toLong()
                            val creditEntities = itemRows.mapNotNull { row ->
                                val price = row.amountText.toDoubleOrNull() ?: 0.0
                                if (row.itemName.isNotBlank() && price > 0) {
                                    val linePaisa = (price * 100).toLong()
                                    ShopCreditItemEntity(
                                        creditId = 0, // Assigned by repository
                                        itemName = row.itemName.trim(),
                                        quantity = 1.0,
                                        unit = "টি",
                                        unitPricePaisa = linePaisa,
                                        lineTotalPaisa = linePaisa
                                    )
                                } else null
                            }

                            val autoNote = if (creditEntities.isNotEmpty() && note.isBlank()) {
                                creditEntities.joinToString(", ") { "${it.itemName} (${CurrencyFormatter.formatPaisa(it.lineTotalPaisa)})" }
                            } else {
                                note.trim()
                            }

                            viewModel.addShopCredit(
                                credit = ShopCreditEntity(
                                    shopId = shopId,
                                    date = selectedDate,
                                    totalAmountPaisa = grandTotalPaisa,
                                    note = autoNote
                                ),
                                items = creditEntities,
                                shopName = shopState?.name ?: "দোকান"
                            ) {
                                onBack()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("সংরক্ষণ করুন")
                }
            }
        }
    }
}
