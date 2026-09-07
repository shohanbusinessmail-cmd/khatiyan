package com.shohan.khatiyan.ui.screens.shop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Payments
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shohan.khatiyan.ui.components.ConfirmDialog
import com.shohan.khatiyan.ui.screens.viewmodels.MainViewModel
import com.shohan.khatiyan.util.CurrencyFormatter
import com.shohan.khatiyan.util.DateUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopDetailScreen(
    shopId: Long,
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onEditClick: (Long) -> Unit,
    onAddCreditClick: (Long) -> Unit,
    onAddPaymentClick: (Long) -> Unit
) {
    val shopState by viewModel.repository.getShopByIdFlow(shopId).collectAsState(initial = null)
    val credits by viewModel.repository.getCreditsForShopFlow(shopId).collectAsState(initial = emptyList())
    val payments by viewModel.repository.getPaymentsForShopFlow(shopId).collectAsState(initial = emptyList())

    val totalCredit = credits.sumOf { it.totalAmountPaisa }
    val totalPaid = payments.sumOf { it.amountPaisa }
    val remaining = (totalCredit - totalPaid).coerceAtLeast(0L)

    var showDeleteDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(shopState?.name ?: "দোকানের বিস্তারিত") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { onEditClick(shopId) }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Shop")
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Shop")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        val shop = shopState
        if (shop != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                // Shop Summary Box
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "দোকানের হিসাব খতিয়ান",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(text = "মোট কেনাকাটা:", style = MaterialTheme.typography.bodySmall)
                                    Text(
                                        text = CurrencyFormatter.formatPaisa(totalCredit),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                                Column {
                                    Text(text = "মোট পরিশোধ:", style = MaterialTheme.typography.bodySmall)
                                    Text(
                                        text = CurrencyFormatter.formatPaisa(totalPaid),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Column {
                                    Text(text = "বর্তমান বাকি:", style = MaterialTheme.typography.bodySmall)
                                    Text(
                                        text = CurrencyFormatter.formatPaisa(remaining),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Action Buttons
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = { onAddCreditClick(shopId) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("বাকী যোগ")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedButton(
                            onClick = { onAddPaymentClick(shopId) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Default.Payments, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("পেমেন্ট পরিশোধ")
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "লেনদেনের ইতিহাস (Ledger History)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Combine Credits & Payments into Timeline
                val combinedHistory = (credits.map { HistoryItem.Credit(it) } + payments.map { HistoryItem.Payment(it) })
                    .sortedByDescending { it.date }

                items(combinedHistory) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = when (item) {
                                        is HistoryItem.Credit -> "বাকী কেনাকাটা"
                                        is HistoryItem.Payment -> "পেমেন্ট পরিশোধ (${item.payment.paymentMethod})"
                                    },
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = DateUtils.formatDate(item.date),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                val note = when (item) {
                                    is HistoryItem.Credit -> item.credit.note
                                    is HistoryItem.Payment -> item.payment.note
                                }
                                if (note.isNotBlank()) {
                                    Text(
                                        text = "নোট: $note",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                            Text(
                                text = when (item) {
                                    is HistoryItem.Credit -> "+ ${CurrencyFormatter.formatPaisa(item.credit.totalAmountPaisa)}"
                                    is HistoryItem.Payment -> "- ${CurrencyFormatter.formatPaisa(item.payment.amountPaisa)}"
                                },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = when (item) {
                                    is HistoryItem.Credit -> MaterialTheme.colorScheme.error
                                    is HistoryItem.Payment -> MaterialTheme.colorScheme.primary
                                }
                            )
                        }
                    }
                }
            }
        }

        if (showDeleteDialog && shop != null) {
            ConfirmDialog(
                title = "দোকান মুছে ফেলবেন?",
                message = "${shop.name} এবং এর সব হিসাব চিরতরে মুছে যাবে। আপনি কি নিশ্চিত?",
                onConfirm = {
                    scope.launch {
                        viewModel.repository.deleteShop(shop)
                        showDeleteDialog = false
                        onBack()
                    }
                },
                onDismiss = { showDeleteDialog = false }
            )
        }
    }
}

private sealed class HistoryItem(val date: Long) {
    data class Credit(val credit: com.shohan.khatiyan.data.local.entities.ShopCreditEntity) : HistoryItem(credit.date)
    data class Payment(val payment: com.shohan.khatiyan.data.local.entities.ShopPaymentEntity) : HistoryItem(payment.date)
}
