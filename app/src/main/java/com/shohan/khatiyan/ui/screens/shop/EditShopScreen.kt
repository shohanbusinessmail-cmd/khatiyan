package com.shohan.khatiyan.ui.screens.shop

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shohan.khatiyan.ui.components.KhatiyanOutlinedTextField
import com.shohan.khatiyan.ui.screens.viewmodels.MainViewModel

/**
 * Edit Shop screen — completes the CRUD capability of the Shop (দোকান) module.
 *
 * Before this screen existed the app could only Create / Read / Delete a shop, which meant a
 * simple typo in a shop name, owner name or phone number forced the user to delete the shop and
 * lose every linked credit (বাকী) and payment row through the CASCADE foreign key.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditShopScreen(
    shopId: Long,
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val shopState by viewModel.repository.getShopByIdFlow(shopId).collectAsState(initial = null)

    var initialised by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var ownerName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf<String?>(null) }

    // Pre-fill the form exactly once, as soon as the shop row is emitted by Room.
    LaunchedEffect(shopState) {
        val shop = shopState
        if (shop != null && !initialised) {
            name = shop.name
            ownerName = shop.ownerName
            phone = shop.phone
            address = shop.address
            category = shop.category
            note = shop.note
            initialised = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("দোকানের তথ্য সম্পাদনা") },
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
        val shop = shopState
        if (shop == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            KhatiyanOutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    if (it.isNotBlank()) nameError = null
                },
                label = "দোকানের নাম *",
                errorMessage = nameError
            )
            Spacer(modifier = Modifier.height(12.dp))

            KhatiyanOutlinedTextField(
                value = ownerName,
                onValueChange = { ownerName = it },
                label = "মালিকের নাম (ঐচ্ছিক)"
            )
            Spacer(modifier = Modifier.height(12.dp))

            KhatiyanOutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = "ফোন নম্বর (ঐচ্ছিক)",
                isNumber = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            KhatiyanOutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = "ঠিকানা (ঐচ্ছিক)"
            )
            Spacer(modifier = Modifier.height(12.dp))

            KhatiyanOutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = "ক্যাটাগরি (যেমন: মুদি, ফার্মেসি, কাপড়ের দোকান)"
            )
            Spacer(modifier = Modifier.height(12.dp))

            KhatiyanOutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = "নোট / বিবরণ (ঐচ্ছিক)"
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (name.isBlank()) {
                        nameError = "দোকানের নাম দেওয়া বাধ্যতামূলক"
                    } else {
                        // Copy preserves id and createdAt so no ledger row is orphaned.
                        viewModel.updateShop(
                            shop.copy(
                                name = name.trim(),
                                ownerName = ownerName.trim(),
                                phone = phone.trim(),
                                address = address.trim(),
                                category = category.trim().ifBlank { "সাধারণ" },
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
                Text("পরিবর্তন সংরক্ষণ করুন")
            }
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("বাতিল")
            }
        }
    }
}
