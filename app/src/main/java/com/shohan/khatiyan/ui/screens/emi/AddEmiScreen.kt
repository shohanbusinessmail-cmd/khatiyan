package com.shohan.khatiyan.ui.screens.emi

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shohan.khatiyan.data.local.entities.EmiEntity
import com.shohan.khatiyan.ui.components.DatePickerTextField
import com.shohan.khatiyan.ui.components.KhatiyanOutlinedTextField
import com.shohan.khatiyan.ui.screens.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEmiScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    var productName by remember { mutableStateOf("") }
    var sellerName by remember { mutableStateOf("") }
    var totalAmountText by remember { mutableStateOf("") }
    var downPaymentText by remember { mutableStateOf("0") }
    var installmentText by remember { mutableStateOf("") }
    var totalInstallmentsText by remember { mutableStateOf("6") }
    var firstDueDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var notes by remember { mutableStateOf("") }

    var productError by remember { mutableStateOf<String?>(null) }
    var amountError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("নতুন ইএমআই / কিস্তি যুক্ত করুন") },
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
            KhatiyanOutlinedTextField(
                value = productName,
                onValueChange = {
                    productName = it
                    if (it.isNotBlank()) productError = null
                },
                label = "পণ্যের নাম (যেমন: ল্যাপটপ, ফোন, ফ্রিজ) *",
                errorMessage = productError
            )
            Spacer(modifier = Modifier.height(12.dp))

            KhatiyanOutlinedTextField(
                value = sellerName,
                onValueChange = { sellerName = it },
                label = "বিক্রেতা/প্রতিষ্ঠানের নাম (যেমন: ওয়ালটন, সিঙ্গার)"
            )
            Spacer(modifier = Modifier.height(12.dp))

            KhatiyanOutlinedTextField(
                value = totalAmountText,
                onValueChange = {
                    totalAmountText = it
                    if (it.isNotBlank()) amountError = null
                },
                label = "পণ্যের মোট ইএমআই মূল্য (৳) *",
                isNumber = true,
                errorMessage = amountError
            )
            Spacer(modifier = Modifier.height(12.dp))

            KhatiyanOutlinedTextField(
                value = downPaymentText,
                onValueChange = { downPaymentText = it },
                label = "ডাউন পেমেন্ট (৳) (ঐচ্ছিক)",
                isNumber = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            KhatiyanOutlinedTextField(
                value = installmentText,
                onValueChange = { installmentText = it },
                label = "প্রতি কিস্তির পরিমাণ (৳)",
                isNumber = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            DatePickerTextField(
                selectedDate = firstDueDate,
                onDateSelected = { }
            )
            Spacer(modifier = Modifier.height(12.dp))

            KhatiyanOutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = "নোট (ঐচ্ছিক)"
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val totalDouble = totalAmountText.toDoubleOrNull()
                    if (productName.isBlank()) {
                        productError = "পণ্যের নাম দেওয়া আবশ্যক"
                    } else if (totalDouble == null || totalDouble <= 0) {
                        amountError = "সঠিক টাকা লিখুন"
                    } else {
                        val downDouble = downPaymentText.toDoubleOrNull() ?: 0.0
                        val financedDouble = (totalDouble - downDouble).coerceAtLeast(0.0)
                        val financedPaisa = (financedDouble * 100).toLong()
                        val totalPayablePaisa = (totalDouble * 100).toLong()

                        val instDouble = installmentText.toDoubleOrNull() ?: (financedDouble / 6)
                        val instPaisa = (instDouble * 100).toLong()

                        viewModel.addEmi(
                            EmiEntity(
                                productName = productName.trim(),
                                sellerName = sellerName.ifBlank { "সাধারণ শোরুম" },
                                purchaseDate = System.currentTimeMillis(),
                                cashPricePaisa = totalPayablePaisa,
                                financedAmountPaisa = financedPaisa,
                                downPaymentPaisa = (downDouble * 100).toLong(),
                                totalPayablePaisa = totalPayablePaisa,
                                installmentAmountPaisa = instPaisa,
                                numberOfInstallments = totalInstallmentsText.toIntOrNull() ?: 6,
                                firstDueDate = firstDueDate,
                                notes = notes.trim()
                            )
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
