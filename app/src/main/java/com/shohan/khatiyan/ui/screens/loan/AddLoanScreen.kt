package com.shohan.khatiyan.ui.screens.loan

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
import com.shohan.khatiyan.data.local.entities.LoanEntity
import com.shohan.khatiyan.ui.components.DatePickerTextField
import com.shohan.khatiyan.ui.components.KhatiyanOutlinedTextField
import com.shohan.khatiyan.ui.screens.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLoanScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    var institutionName by remember { mutableStateOf("") }
    var loanTitle by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var installmentText by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("মাসিক") }
    var totalInstallmentsText by remember { mutableStateOf("12") }
    var firstPaymentDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var notes by remember { mutableStateOf("") }

    var institutionError by remember { mutableStateOf<String?>(null) }
    var amountError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("নতুন লোন যুক্ত করুন") },
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
                value = institutionName,
                onValueChange = {
                    institutionName = it
                    if (it.isNotBlank()) institutionError = null
                },
                label = "প্রতিষ্ঠানের নাম (যেমন: ব্র্যাক ব্যাংক, আশা) *",
                errorMessage = institutionError
            )
            Spacer(modifier = Modifier.height(12.dp))

            KhatiyanOutlinedTextField(
                value = loanTitle,
                onValueChange = { loanTitle = it },
                label = "লোনের নাম/টাইটেল (যেমন: পার্সোনাল লোন)"
            )
            Spacer(modifier = Modifier.height(12.dp))

            KhatiyanOutlinedTextField(
                value = amountText,
                onValueChange = {
                    amountText = it
                    if (it.isNotBlank()) amountError = null
                },
                label = "মোট লোনের পরিমাণ (৳) *",
                isNumber = true,
                errorMessage = amountError
            )
            Spacer(modifier = Modifier.height(12.dp))

            KhatiyanOutlinedTextField(
                value = installmentText,
                onValueChange = { installmentText = it },
                label = "প্রতি কিস্তির পরিমাণ (৳)",
                isNumber = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            KhatiyanOutlinedTextField(
                value = frequency,
                onValueChange = { frequency = it },
                label = "কিস্তির সময়কাল (যেমন: মাসিক, সাপ্তাহিক)"
            )
            Spacer(modifier = Modifier.height(12.dp))

            DatePickerTextField(
                selectedDate = firstPaymentDate,
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
                    val amountDouble = amountText.toDoubleOrNull()
                    if (institutionName.isBlank()) {
                        institutionError = "প্রতিষ্ঠানের নাম আবশ্যক"
                    } else if (amountDouble == null || amountDouble <= 0) {
                        amountError = "সঠিক লোন পরিমাণ লিখুন"
                    } else {
                        val loanPaisa = (amountDouble * 100).toLong()
                        val instDouble = installmentText.toDoubleOrNull() ?: (amountDouble / 12)
                        val instPaisa = (instDouble * 100).toLong()

                        viewModel.addLoan(
                            LoanEntity(
                                institutionName = institutionName.trim(),
                                loanTitle = loanTitle.ifBlank { "সাধারণ লোন" },
                                loanAmountPaisa = loanPaisa,
                                totalPayablePaisa = loanPaisa,
                                installmentAmountPaisa = instPaisa,
                                frequency = frequency,
                                totalInstallments = totalInstallmentsText.toIntOrNull() ?: 12,
                                dateTaken = System.currentTimeMillis(),
                                firstPaymentDate = firstPaymentDate,
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
