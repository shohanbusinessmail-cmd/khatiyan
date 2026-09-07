package com.shohan.khatiyan.ui.screens.emi

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
fun EmiDetailScreen(
    emiId: Long,
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onAddPaymentClick: (Long) -> Unit
) {
    val emiState by viewModel.repository.getEmiByIdFlow(emiId).collectAsState(initial = null)
    val payments by viewModel.repository.getPaymentsForEmiFlow(emiId).collectAsState(initial = emptyList())

    val totalPaid = payments.sumOf { it.amountPaisa }
    val emi = emiState
    val remaining = if (emi != null) (emi.totalPayablePaisa - totalPaid).coerceAtLeast(0L) else 0L

    var showDeleteDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(emi?.productName ?: "ইএমআই বিস্তারিত") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete EMI")
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
        if (emi != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "${emi.productName} — ${emi.sellerName}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(text = "মোট ইএমআই:", style = MaterialTheme.typography.bodySmall)
                                    Text(
                                        text = CurrencyFormatter.formatPaisa(emi.totalPayablePaisa),
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
                                    Text(text = "বাকি আছে:", style = MaterialTheme.typography.bodySmall)
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

                    if (remaining > 0) {
                        Button(
                            onClick = { onAddPaymentClick(emiId) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(imageVector = Icons.Default.Payments, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("ইএমআই কিস্তি পরিশোধ করুন")
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "পরিশোধের ইতিহাস (Payment History)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                items(payments) { payment ->
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
                                    text = "কিস্তি পরিশোধ (${payment.paymentMethod})",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = DateUtils.formatDate(payment.date),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                if (payment.note.isNotBlank()) {
                                    Text(text = "নোট: ${payment.note}", style = MaterialTheme.typography.labelSmall)
                                }
                            }
                            Text(
                                text = CurrencyFormatter.formatPaisa(payment.amountPaisa),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }

        if (showDeleteDialog && emi != null) {
            ConfirmDialog(
                title = "ইএমআই মুছে ফেলবেন?",
                message = "${emi.productName} এর ইএমআই হিসাবটি মুছে যাবে। আপনি কি নিশ্চিত?",
                onConfirm = {
                    scope.launch {
                        viewModel.repository.deleteEmi(emi)
                        showDeleteDialog = false
                        onBack()
                    }
                },
                onDismiss = { showDeleteDialog = false }
            )
        }
    }
}
