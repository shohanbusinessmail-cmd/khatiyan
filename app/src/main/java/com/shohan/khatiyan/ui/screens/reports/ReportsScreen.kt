package com.shohan.khatiyan.ui.screens.reports

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shohan.khatiyan.ui.components.ChartSlice
import com.shohan.khatiyan.ui.components.DonutChart
import com.shohan.khatiyan.ui.components.SimpleBarChart
import com.shohan.khatiyan.ui.screens.viewmodels.MainViewModel
import com.shohan.khatiyan.util.CurrencyFormatter
import com.shohan.khatiyan.util.PdfGenerator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    viewModel: MainViewModel
) {
    val summary by viewModel.dashboardSummary.collectAsState()
    val userPrefs by viewModel.userPreferences.collectAsState()
    val context = LocalContext.current

    val pdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf")
    ) { uri: Uri? ->
        if (uri != null) {
            PdfGenerator.generatePdfReport(
                context = context,
                uri = uri,
                userName = userPrefs.userName,
                summary = summary
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("রিপোর্ট ও আর্থিক বিশ্লেষণ") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // PDF Generation Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "আর্থিক স্টেটমেন্ট ডাউনলোড",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "আপনার সমস্ত হিসাবের সুন্দর পিডিএফ রিপোর্ট তৈরি করুন।",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { pdfLauncher.launch("Khatiyan_Report_${System.currentTimeMillis()}.pdf") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("পিডিএফ রিপোর্ট তৈরি করুন")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Income vs Expense Bar Chart
            item {
                Text(
                    text = "আয় বনাম ব্যয় (এই মাস)",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    SimpleBarChart(
                        incomePaisa = summary.thisMonthIncomePaisa,
                        expensePaisa = summary.thisMonthExpensePaisa
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Debt Distribution Donut Chart
            if (summary.totalDebtPaisa > 0) {
                item {
                    Text(
                        text = "দেনার খাতভিত্তিক অনুপাত",
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
                }
            }
        }
    }
}
