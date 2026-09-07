package com.shohan.khatiyan.ui.screens.dashboard

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shohan.khatiyan.data.model.DashboardSummary
import com.shohan.khatiyan.data.model.DueItemModel
import com.shohan.khatiyan.ui.components.ChartSlice
import com.shohan.khatiyan.ui.components.DonutChart
import com.shohan.khatiyan.ui.components.QuickAddBottomSheet
import com.shohan.khatiyan.ui.components.SummaryStatCard
import com.shohan.khatiyan.ui.screens.viewmodels.MainViewModel
import com.shohan.khatiyan.util.CurrencyFormatter
import com.shohan.khatiyan.util.DateUtils
import com.shohan.khatiyan.util.InsightEngine

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: MainViewModel,
    onNavigateToSearch: () -> Unit,
    onNavigateToAccountTab: (String) -> Unit,
    onQuickAction: (String) -> Unit
) {
    val summary by viewModel.dashboardSummary.collectAsState()
    val upcomingDues by viewModel.upcomingDues.collectAsState()
    val userPrefs by viewModel.userPreferences.collectAsState()
    var showQuickAddSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "শুভ দিন, ${userPrefs.userName}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            text = "খতিয়ান — সব হিসাব, এক জায়গায়",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToSearch) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showQuickAddSheet = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Quick Add")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Main Net Outstanding Debt Banner Card
            item {
                SummaryStatCard(
                    title = "মোট বকেয়া দেনা",
                    amountPaisa = summary.totalDebtPaisa,
                    icon = Icons.Default.AccountBalance,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    subtitle = "দোকান, লোন, ইএমআই এবং ব্যক্তিগত ধারের সর্বমোট অবস্থান"
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Category Overview Cards (Grid 2x2)
            item {
                Text(
                    text = "বকেয়ার সংক্ষিপ্ত বিবরণ",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    CategoryMiniCard(
                        title = "দোকানের বাকী",
                        amountPaisa = summary.shopDebtPaisa,
                        icon = Icons.Default.ShoppingBag,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigateToAccountTab("SHOP") }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    CategoryMiniCard(
                        title = "ব্যাংক লোন",
                        amountPaisa = summary.loanDebtPaisa,
                        icon = Icons.Default.AccountBalance,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigateToAccountTab("LOAN") }
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    CategoryMiniCard(
                        title = "ইএমআই কিস্তি",
                        amountPaisa = summary.emiDebtPaisa,
                        icon = Icons.Default.CreditCard,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigateToAccountTab("EMI") }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    CategoryMiniCard(
                        title = "ব্যক্তিগত ধার",
                        amountPaisa = summary.personalDebtPaisa,
                        icon = Icons.Default.Person,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigateToAccountTab("PERSONAL") }
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Debt Distribution Donut Chart
            if (summary.totalDebtPaisa > 0) {
                item {
                    Text(
                        text = "দেনার অনুপাত",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    val slices = listOf(
                        ChartSlice("দোকানের বাকী", summary.shopDebtPaisa, MaterialTheme.colorScheme.primary),
                        ChartSlice("ব্যাংক লোন", summary.loanDebtPaisa, MaterialTheme.colorScheme.secondary),
                        ChartSlice("ইএমআই কিস্তি", summary.emiDebtPaisa, MaterialTheme.colorScheme.tertiary),
                        ChartSlice("ব্যক্তিগত ধার", summary.personalDebtPaisa, MaterialTheme.colorScheme.error)
                    ).filter { it.value > 0 }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        DonutChart(slices = slices, modifier = Modifier.padding(16.dp))
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            // Upcoming Payments List
            item {
                Text(
                    text = "সামনের পেমেন্ট (Upcoming Dues)",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (upcomingDues.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Text(
                            text = "সামনে কোনো পেমেন্ট বাকি নেই।",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            } else {
                items(upcomingDues) { due ->
                    DueItemCard(due = due)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item { Spacer(modifier = Modifier.height(12.dp)) }
            }

            // Smart Insights Section
            item {
                val insights = InsightEngine.generateInsights(summary, upcomingDues)
                if (insights.isNotEmpty()) {
                    Text(
                        text = "স্মার্ট ইনসাইট",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    insights.forEach { insight ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (insight.iconType == "danger") MaterialTheme.colorScheme.errorContainer
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (insight.iconType == "danger") Icons.Default.Warning else Icons.Default.Info,
                                    contentDescription = null,
                                    tint = if (insight.iconType == "danger") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = insight.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = insight.description,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showQuickAddSheet) {
            QuickAddBottomSheet(
                onDismiss = { showQuickAddSheet = false },
                onActionSelected = onQuickAction
            )
        }
    }
}

@Composable
private fun CategoryMiniCard(
    title: String,
    amountPaisa: Long,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = CurrencyFormatter.formatPaisa(amountPaisa),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun DueItemCard(due: DueItemModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (due.isOverdue) MaterialTheme.colorScheme.errorContainer
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = due.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${due.subtitle} • শেষ তারিখ: ${DateUtils.formatDate(due.dueDate)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (due.isOverdue) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = CurrencyFormatter.formatPaisa(due.amountPaisa),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (due.isOverdue) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
        }
    }
}
