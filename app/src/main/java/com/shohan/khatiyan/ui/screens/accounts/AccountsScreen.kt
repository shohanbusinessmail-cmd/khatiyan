package com.shohan.khatiyan.ui.screens.accounts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.shohan.khatiyan.ui.screens.emi.EmiListScreen
import com.shohan.khatiyan.ui.screens.income_expense.IncomeExpenseScreen
import com.shohan.khatiyan.ui.screens.loan.LoanListScreen
import com.shohan.khatiyan.ui.screens.personal.PersonalDebtListScreen
import com.shohan.khatiyan.ui.screens.shop.ShopListScreen
import com.shohan.khatiyan.ui.screens.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountsScreen(
    viewModel: MainViewModel,
    initialTab: Int = 0,
    onNavigateToShopDetail: (Long) -> Unit,
    onNavigateToAddShop: () -> Unit,
    onNavigateToLoanDetail: (Long) -> Unit,
    onNavigateToAddLoan: () -> Unit,
    onNavigateToEmiDetail: (Long) -> Unit,
    onNavigateToAddEmi: () -> Unit,
    onNavigateToPersonalDetail: (Long) -> Unit,
    onNavigateToAddPersonal: () -> Unit,
    onNavigateToAddIncome: () -> Unit,
    onNavigateToAddExpense: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(initialTab) }
    val tabs = listOf("দোকানের বাকী", "ব্যাংক/এনজিও লোন", "ইএমআই/কিস্তি", "ব্যক্তিগত ধার", "আয়-ব্যয়")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("হিসাবের খাতসমূহ") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            when (selectedTab) {
                0 -> ShopListScreen(
                    viewModel = viewModel,
                    onShopClick = onNavigateToShopDetail,
                    onAddShopClick = onNavigateToAddShop
                )
                1 -> LoanListScreen(
                    viewModel = viewModel,
                    onLoanClick = onNavigateToLoanDetail,
                    onAddLoanClick = onNavigateToAddLoan
                )
                2 -> EmiListScreen(
                    viewModel = viewModel,
                    onEmiClick = onNavigateToEmiDetail,
                    onAddEmiClick = onNavigateToAddEmi
                )
                3 -> PersonalDebtListScreen(
                    viewModel = viewModel,
                    onPersonClick = onNavigateToPersonalDetail,
                    onAddPersonalClick = onNavigateToAddPersonal
                )
                4 -> IncomeExpenseScreen(
                    viewModel = viewModel,
                    onAddIncomeClick = onNavigateToAddIncome,
                    onAddExpenseClick = onNavigateToAddExpense
                )
            }
        }
    }
}
