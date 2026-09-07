package com.shohan.khatiyan.ui.screens.shop

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shohan.khatiyan.data.local.entities.ShopCreditEntity
import com.shohan.khatiyan.data.local.entities.ShopCreditItemEntity
import com.shohan.khatiyan.ui.components.DatePickerTextField
import com.shohan.khatiyan.ui.components.KhatiyanOutlinedTextField
import com.shohan.khatiyan.ui.screens.viewmodels.MainViewModel

data class ItemRowState(
    var name: String = "",
    var price: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddShopCreditScreen(
    shopId: Long,
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val shopState by viewModel.repository.getShopByIdFlow(shopId).collectAsState(initial = null)
    var amountText by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var note by remember { mutableStateOf("") }

    var amountError by remember { mutableStateOf<String?>(null) }
    val itemRows = remember { mutableStateListOf<ItemRowState>() }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
        ) {
            KhatiyanOutlinedTextField(
                value = amountText,
                onValueChange = {
                    amountText = it
                    if (it.isNotBlank()) amountError = null
                },
                label = "মোট বাকীর পরিমাণ (৳) *",
                isNumber = true,
                errorMessage = amountError
            )
            Spacer(modifier = Modifier.height(12.dp))

            DatePickerTextField(
                selectedDate = selectedDate,
                onDateSelected = { /* Default current date or picker */ }
            )
            Spacer(modifier = Modifier.height(12.dp))

            KhatiyanOutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = "পণ্য বা বিবরণী নোট (যেমন: চাল, ডাল, তেল)"
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val amountDouble = amountText.toDoubleOrNull()
                    if (amountDouble == null || amountDouble <= 0) {
                        amountError = "দয়া করে সঠিক টাকার পরিমাণ লিখুন"
                    } else {
                        val totalPaisa = (amountDouble * 100).toLong()
                        viewModel.addShopCredit(
                            credit = ShopCreditEntity(
                                shopId = shopId,
                                date = selectedDate,
                                totalAmountPaisa = totalPaisa,
                                note = note.trim()
                            ),
                            items = emptyList(),
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
