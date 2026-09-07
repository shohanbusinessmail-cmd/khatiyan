package com.shohan.khatiyan.ui.screens.personal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
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
import com.shohan.khatiyan.data.local.entities.PersonEntity
import com.shohan.khatiyan.data.local.entities.PersonalDebtEntity
import com.shohan.khatiyan.ui.components.DatePickerTextField
import com.shohan.khatiyan.ui.components.KhatiyanOutlinedTextField
import com.shohan.khatiyan.ui.screens.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPersonalDebtScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    var personName by remember { mutableStateOf("") }
    var relationship by remember { mutableStateOf("বন্ধু") }
    var amountText by remember { mutableStateOf("") }
    var isIoweThem by remember { mutableStateOf(true) } // true = I borrowed from them
    var dateTaken by remember { mutableStateOf(System.currentTimeMillis()) }
    var expectedReturnDate by remember { mutableStateOf(System.currentTimeMillis() + 7 * 86400000L) }
    var note by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf<String?>(null) }
    var amountError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ব্যক্তিগত ধার যোগ করুন") },
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
            Row(modifier = Modifier.fillMaxWidth()) {
                FilterChip(
                    selected = isIoweThem,
                    onClick = { isIoweThem = true },
                    label = { Text("আমি ধার নিয়েছি") }
                )
                Spacer(modifier = Modifier.padding(8.dp))
                FilterChip(
                    selected = !isIoweThem,
                    onClick = { isIoweThem = false },
                    label = { Text("আমি ধার দিয়েছি") }
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            KhatiyanOutlinedTextField(
                value = personName,
                onValueChange = {
                    personName = it
                    if (it.isNotBlank()) nameError = null
                },
                label = "ব্যক্তির নাম *",
                errorMessage = nameError
            )
            Spacer(modifier = Modifier.height(12.dp))

            KhatiyanOutlinedTextField(
                value = relationship,
                onValueChange = { relationship = it },
                label = "সম্পর্ক (যেমন: বন্ধু, আত্মীয়, কলিগ, পরিবার)"
            )
            Spacer(modifier = Modifier.height(12.dp))

            KhatiyanOutlinedTextField(
                value = amountText,
                onValueChange = {
                    amountText = it
                    if (it.isNotBlank()) amountError = null
                },
                label = "ধারের পরিমাণ (৳) *",
                isNumber = true,
                errorMessage = amountError
            )
            Spacer(modifier = Modifier.height(12.dp))

            DatePickerTextField(
                selectedDate = expectedReturnDate,
                onDateSelected = { },
                label = "পরিশোধের সম্ভাব্য তারিখ"
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
                    if (personName.isBlank()) {
                        nameError = "ব্যক্তির নাম আবশ্যক"
                    } else if (amountDouble == null || amountDouble <= 0) {
                        amountError = "সঠিক টাকা লিখুন"
                    } else {
                        val paisa = (amountDouble * 100).toLong()
                        viewModel.addPerson(
                            PersonEntity(
                                name = personName.trim(),
                                relationship = relationship.ifBlank { "পরিচিত" }
                            )
                        ) { personId ->
                            viewModel.addPersonalDebt(
                                debt = PersonalDebtEntity(
                                    personId = personId,
                                    amountPaisa = paisa,
                                    date = dateTaken,
                                    expectedReturnDate = expectedReturnDate,
                                    note = note.trim(),
                                    isIoweThem = isIoweThem
                                ),
                                personName = personName.trim()
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
                Text("সংরক্ষণ করুন")
            }
        }
    }
}
