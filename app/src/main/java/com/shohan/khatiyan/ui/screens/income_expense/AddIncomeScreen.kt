package com.shohan.khatiyan.ui.screens.income_expense

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
import com.shohan.khatiyan.data.local.entities.IncomeEntity
import com.shohan.khatiyan.ui.components.DatePickerTextField
import com.shohan.khatiyan.ui.components.KhatiyanOutlinedTextField
import com.shohan.khatiyan.ui.screens.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddIncomeScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    var source by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("বেতন") }
    var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var note by remember { mutableStateOf("") }

    var sourceError by remember { mutableStateOf<String?>(null) }
    var amountError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("আয় যোগ করুন") },
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
                value = source,
                onValueChange = {
                    source = it
                    if (it.isNotBlank()) sourceError = null
                },
                label = "আয়ের উৎস (যেমন: বেতন, ব্যবসা, ফ্রিল্যান্সিং) *",
                errorMessage = sourceError
            )
            Spacer(modifier = Modifier.height(12.dp))

            KhatiyanOutlinedTextField(
                value = amountText,
                onValueChange = {
                    amountText = it
                    if (it.isNotBlank()) amountError = null
                },
                label = "টাকার পরিমাণ (৳) *",
                isNumber = true,
                errorMessage = amountError
            )
            Spacer(modifier = Modifier.height(12.dp))

            KhatiyanOutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = "ক্যাটাগরি"
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
                    if (source.isBlank()) {
                        sourceError = "উৎস আবশ্যক"
                    } else if (amountDouble == null || amountDouble <= 0) {
                        amountError = "সঠিক টাকা লিখুন"
                    } else {
                        val paisa = (amountDouble * 100).toLong()
                        viewModel.addIncome(
                            IncomeEntity(
                                source = source.trim(),
                                amountPaisa = paisa,
                                category = category.ifBlank { "সাধারণ" },
                                date = selectedDate,
                                note = note.trim()
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
