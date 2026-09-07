package com.shohan.khatiyan.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class QuickActionItem(
    val title: String,
    val icon: ImageVector,
    val actionType: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickAddBottomSheet(
    onDismiss: () -> Unit,
    onActionSelected: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    val actions = listOf(
        QuickActionItem("দোকানের বাকী", Icons.Default.ShoppingBag, "ADD_SHOP_CREDIT"),
        QuickActionItem("পেমেন্ট পরিশোধ", Icons.Default.Payments, "ADD_PAYMENT"),
        QuickActionItem("নতুন লোন", Icons.Default.AccountBalance, "ADD_LOAN"),
        QuickActionItem("নতুন ইএমআই", Icons.Default.CreditCard, "ADD_EMI"),
        QuickActionItem("ব্যক্তিগত ধার", Icons.Default.Person, "ADD_PERSONAL"),
        QuickActionItem("আয় যোগ করুন", Icons.Default.TrendingUp, "ADD_INCOME"),
        QuickActionItem("ব্যয় যোগ করুন", Icons.Default.TrendingDown, "ADD_EXPENSE"),
        QuickActionItem("নতুন দোকান", Icons.Default.PointOfSale, "ADD_SHOP")
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "দ্রুত হিসাব যুক্ত করুন",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(actions.size) { index ->
                    val action = actions[index]
                    Card(
                        modifier = Modifier
                            .padding(6.dp)
                            .clickable {
                                onActionSelected(action.actionType)
                                onDismiss()
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = action.icon,
                                contentDescription = action.title,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = action.title,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
