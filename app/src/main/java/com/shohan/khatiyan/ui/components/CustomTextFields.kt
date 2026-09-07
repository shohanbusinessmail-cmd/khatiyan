package com.shohan.khatiyan.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.shohan.khatiyan.util.DateUtils

@Composable
fun KhatiyanOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isNumber: Boolean = false,
    errorMessage: String? = null,
    readOnly: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
            isError = errorMessage != null,
            readOnly = readOnly,
            singleLine = true,
            keyboardOptions = if (isNumber) KeyboardOptions(keyboardType = KeyboardType.Decimal) else KeyboardOptions.Default
        )
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun DatePickerTextField(
    selectedDate: Long,
    onDateSelected: () -> Unit,
    label: String = "তারিখ",
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = DateUtils.formatDate(selectedDate),
        onValueChange = {},
        label = { Text(label) },
        readOnly = true,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onDateSelected() },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = "Select Date"
            )
        }
    )
}
