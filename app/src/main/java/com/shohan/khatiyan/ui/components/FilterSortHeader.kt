package com.shohan.khatiyan.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FilterSortHeader(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.FilterList,
            contentDescription = "Filter",
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(8.dp))
        FilterChip(
            selected = selectedFilter == "ALL",
            onClick = { onFilterSelected("ALL") },
            label = { Text("সব") }
        )
        Spacer(modifier = Modifier.width(8.dp))
        FilterChip(
            selected = selectedFilter == "PENDING",
            onClick = { onFilterSelected("PENDING") },
            label = { Text("বকেয়া") }
        )
        Spacer(modifier = Modifier.width(8.dp))
        FilterChip(
            selected = selectedFilter == "PAID",
            onClick = { onFilterSelected("PAID") },
            label = { Text("পরিশোধিত") }
        )
    }
}
