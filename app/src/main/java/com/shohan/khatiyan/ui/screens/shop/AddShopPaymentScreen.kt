package com.shohan.khatiyan.ui.screens.shop

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shohan.khatiyan.data.local.entities.ShopPaymentEntity
import com.shohan.khatiyan.ui.components.DatePickerTextField
import com.shohan.khatiyan.ui.components.KhatiyanOutlinedTextField
import com.shohan.khatiyan.ui.screens.viewmodels.MainViewModel
import com.shohan.khatiyan.util.CurrencyFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddShopPaymentScreen(
    shopId: Long,
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val shopState by viewModel.repository.getShopByIdFlow(shopId).collectAsState(initial = null)
    val credits by viewModel.repository.getCreditsForShopFlow(shopId).collectAsState(initial = emptyList())
    val payments by viewModel.repository.getPaymentsForShopFlow(shopId).collectAsState(initial = emptyList())

    val totalCredit = credits.sumOf { it.totalAmountPaisa }
    val totalPaid = payments.sumOf { it.amountPaisa }
    val remainingBalancePaisa = (totalCredit - totalPaid).coerceAtLeast(0L)

    var amountText by remember { mutableStateOf("") }
    var method by remember { mutableStateOf("নগদ") }
    var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var note by remember { mutableStateOf("") }

    var amountError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${shopState?.name ?: "দোকানে"} পেমেন্ট পরিশোধ") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "বর্তমান বকেয়া বাকি: ${CurrencyFormatter.formatPaisa(remainingBalancePaisa)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            KhatiyanOutlinedTextField(
                value = amountText,
                onValueChange = {
                    amountText = it
                    if (it.isNotBlank()) amountError = null
                },
                label = "পরিশোধের পরিমাণ (৳) *",
                isNumber = true,
                errorMessage = amountError
            )
            Spacer(modifier = Modifier.height(12.dp))

            KhatiyanOutlinedTextField(
                value = method,
                onValueChange = { method = it },
                label = "পেমেন্টের মাধ্যম (যেমন: নগদ, বিকাশ, নগদ অ্যাপ, ব্যাংক)"
            )
            Spacer(modifier = Modifier.height(12.dp))

            DatePickerTextField(
                selectedDate = selectedDate,
                onDateSelected = { }
            )
            Spacer(modifier = Modifier.height(12.dp))

            KhatiyanOutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = "নোট (ঐচ্ছিক)"
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val amountDouble = amountText.toDoubleOrNull()
                    if (amountDouble == null || amountDouble <= 0) {
                        amountError = "দয়া করে সঠিক টাকার পরিমাণ লিখুন"
                    } else {
                        val inputPaisa = (amountDouble * 100).toLong()
                        if (inputPaisa > remainingBalancePaisa && remainingBalancePaisa > 0) {
                            amountError = "পরিশোধের পরিমাণ বকেয়ার (${CurrencyFormatter.formatPaisa(remainingBalancePaisa)}) চেয়ে বেশি হতে পারবে না।"
                        } else {
                            viewModel.addShopPayment(
                                payment = ShopPaymentEntity(
                                    shopId = shopId,
                                    date = selectedDate,
                                    amountPaisa = inputPaisa,
                                    paymentMethod = method.ifBlank { "নগদ" },
                                    note = note.trim()
                                ),
                                shopName = shopState?.name ?: "দোকান"
                            ) {
                                onBack()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("পরিশোধ নিশ্চিত করুন")
            }
        }
    }
}
