package com.shohan.khatiyan.ui.screens.income_expense

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shohan.khatiyan.ui.screens.viewmodels.MainViewModel
import com.shohan.khatiyan.util.CurrencyFormatter
import com.shohan.khatiyan.util.DateUtils

@Composable
fun IncomeExpenseScreen(
    viewModel: MainViewModel,
    onAddIncomeClick: () -> Unit,
    onAddExpenseClick: () -> Unit
) {
    val incomes by viewModel.incomes.collectAsState()
    val expenses by viewModel.expenses.collectAsState()

    val totalIncome = incomes.sumOf { it.amountPaisa }
    val totalExpense = expenses.sumOf { it.amountPaisa }
    val netCashflow = totalIncome - totalExpense

    Scaffold { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Overview Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "আয়-ব্যয়ের হিসাব খতিয়ান",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = "সর্বমোট আয়:", style = MaterialTheme.typography.bodySmall)
                                Text(
                                    text = CurrencyFormatter.formatPaisa(totalIncome),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column {
                                Text(text = "সর্বমোট ব্যয়:", style = MaterialTheme.typography.bodySmall)
                                Text(
                                    text = CurrencyFormatter.formatPaisa(totalExpense),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.error,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column {
                                Text(text = "নিট উদ্বৃত্ত:", style = MaterialTheme.typography.bodySmall)
                                Text(
                                    text = CurrencyFormatter.formatPaisa(netCashflow),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // CTAs
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = onAddIncomeClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(imageVector = Icons.Default.TrendingUp, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("+ আয় যোগ")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedButton(
                        onClick = onAddExpenseClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(imageVector = Icons.Default.TrendingDown, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("+ ব্যয় যোগ")
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "আয় ও ব্যয়ের তালিকা",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            val combinedList = (incomes.map { IncomeExpenseItem.IncomeItem(it) } + expenses.map { IncomeExpenseItem.ExpenseItem(it) })
                .sortedByDescending { it.date }

            items(combinedList) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = when (item) {
                                    is IncomeExpenseItem.IncomeItem -> item.income.source
                                    is IncomeExpenseItem.ExpenseItem -> item.expense.category
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = DateUtils.formatDate(item.date),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = when (item) {
                                is IncomeExpenseItem.IncomeItem -> "+ ${CurrencyFormatter.formatPaisa(item.income.amountPaisa)}"
                                is IncomeExpenseItem.ExpenseItem -> "- ${CurrencyFormatter.formatPaisa(item.expense.amountPaisa)}"
                            },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = when (item) {
                                is IncomeExpenseItem.IncomeItem -> MaterialTheme.colorScheme.primary
                                is IncomeExpenseItem.ExpenseItem -> MaterialTheme.colorScheme.error
                            }
                        )
                    }
                }
            }
        }
    }
}

private sealed class IncomeExpenseItem(val date: Long) {
    data class IncomeItem(val income: com.shohan.khatiyan.data.local.entities.IncomeEntity) : IncomeExpenseItem(income.date)
    data class ExpenseItem(val expense: com.shohan.khatiyan.data.local.entities.ExpenseEntity) : IncomeExpenseItem(expense.date)
}
