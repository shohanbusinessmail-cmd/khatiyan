package com.shohan.khatiyan.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.shohan.khatiyan.R
import com.shohan.khatiyan.ui.screens.accounts.AccountsScreen
import com.shohan.khatiyan.ui.screens.backup.BackupRestoreScreen
import com.shohan.khatiyan.ui.screens.dashboard.DashboardScreen
import com.shohan.khatiyan.ui.screens.emi.AddEmiPaymentScreen
import com.shohan.khatiyan.ui.screens.emi.AddEmiScreen
import com.shohan.khatiyan.ui.screens.emi.EmiDetailScreen
import com.shohan.khatiyan.ui.screens.income_expense.AddExpenseScreen
import com.shohan.khatiyan.ui.screens.income_expense.AddIncomeScreen
import com.shohan.khatiyan.ui.screens.loan.AddLoanPaymentScreen
import com.shohan.khatiyan.ui.screens.loan.AddLoanScreen
import com.shohan.khatiyan.ui.screens.loan.LoanDetailScreen
import com.shohan.khatiyan.ui.screens.onboarding.OnboardingScreen
import com.shohan.khatiyan.ui.screens.personal.AddPersonalDebtScreen
import com.shohan.khatiyan.ui.screens.personal.AddPersonalRepaymentScreen
import com.shohan.khatiyan.ui.screens.personal.PersonalDebtDetailScreen
import com.shohan.khatiyan.ui.screens.reports.ReportsScreen
import com.shohan.khatiyan.ui.screens.search.SearchScreen
import com.shohan.khatiyan.ui.screens.settings.SettingsScreen
import com.shohan.khatiyan.ui.screens.shop.AddShopCreditScreen
import com.shohan.khatiyan.ui.screens.shop.AddShopPaymentScreen
import com.shohan.khatiyan.ui.screens.shop.AddShopScreen
import com.shohan.khatiyan.ui.screens.shop.EditShopScreen
import com.shohan.khatiyan.ui.screens.shop.ShopDetailScreen
import com.shohan.khatiyan.ui.screens.transaction.TransactionListScreen
import com.shohan.khatiyan.ui.screens.viewmodels.MainViewModel

data class BottomNavItem(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val route: String
)

@Composable
fun KhatiyanAppUi(
    viewModel: MainViewModel,
    navController: NavHostController = rememberNavController()
) {
    val userPrefs by viewModel.userPreferences.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavItems = listOf(
        BottomNavItem(stringResource(R.string.nav_dashboard), Icons.Default.Dashboard, Screen.Dashboard.route),
        BottomNavItem(stringResource(R.string.nav_accounts), Icons.Default.AccountBalanceWallet, Screen.Accounts.route),
        BottomNavItem(stringResource(R.string.nav_transactions), Icons.Default.ReceiptLong, Screen.Transactions.route),
        BottomNavItem(stringResource(R.string.nav_reports), Icons.Default.PieChart, Screen.Reports.route),
        BottomNavItem(stringResource(R.string.nav_settings), Icons.Default.Settings, Screen.Settings.route)
    )

    val showBottomBar = currentRoute in bottomNavItems.map { it.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar && userPrefs.isOnboardingCompleted) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                            label = { Text(item.title) }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = if (userPrefs.isOnboardingCompleted) Screen.Dashboard.route else Screen.Onboarding.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Onboarding.route) {
                OnboardingScreen(
                    onFinishOnboarding = { userName ->
                        viewModel.completeOnboarding(userName)
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    viewModel = viewModel,
                    onNavigateToSearch = { navController.navigate(Screen.Search.route) },
                    onNavigateToAccountTab = { navController.navigate(Screen.Accounts.route) },
                    onQuickAction = { actionType ->
                        when (actionType) {
                            "ADD_SHOP" -> navController.navigate(Screen.AddShop.route)
                            "ADD_SHOP_CREDIT" -> navController.navigate(Screen.Accounts.route)
                            "ADD_PAYMENT" -> navController.navigate(Screen.Accounts.route)
                            "ADD_LOAN" -> navController.navigate(Screen.AddLoan.route)
                            "ADD_EMI" -> navController.navigate(Screen.AddEmi.route)
                            "ADD_PERSONAL" -> navController.navigate(Screen.AddPersonalDebt.route)
                            "ADD_INCOME" -> navController.navigate(Screen.AddIncome.route)
                            "ADD_EXPENSE" -> navController.navigate(Screen.AddExpense.route)
                        }
                    }
                )
            }

            composable(Screen.Accounts.route) {
                AccountsScreen(
                    viewModel = viewModel,
                    onNavigateToShopDetail = { id -> navController.navigate(Screen.ShopDetail.createRoute(id)) },
                    onNavigateToAddShop = { navController.navigate(Screen.AddShop.route) },
                    onNavigateToLoanDetail = { id -> navController.navigate(Screen.LoanDetail.createRoute(id)) },
                    onNavigateToAddLoan = { navController.navigate(Screen.AddLoan.route) },
                    onNavigateToEmiDetail = { id -> navController.navigate(Screen.EmiDetail.createRoute(id)) },
                    onNavigateToAddEmi = { navController.navigate(Screen.AddEmi.route) },
                    onNavigateToPersonalDetail = { id -> navController.navigate(Screen.PersonalDebtDetail.createRoute(id)) },
                    onNavigateToAddPersonal = { navController.navigate(Screen.AddPersonalDebt.route) },
                    onNavigateToAddIncome = { navController.navigate(Screen.AddIncome.route) },
                    onNavigateToAddExpense = { navController.navigate(Screen.AddExpense.route) }
                )
            }

            composable(Screen.Transactions.route) {
                TransactionListScreen(viewModel = viewModel)
            }

            composable(Screen.Reports.route) {
                ReportsScreen(viewModel = viewModel)
            }

            composable(Screen.Settings.route) {
                SettingsScreen(
                    viewModel = viewModel,
                    onNavigateToBackup = { navController.navigate(Screen.BackupRestore.route) }
                )
            }

            // Sub-routes
            composable(Screen.AddShop.route) {
                AddShopScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
            }
            composable(
                route = Screen.ShopDetail.route,
                arguments = listOf(navArgument("shopId") { type = NavType.LongType })
            ) { backStackEntry ->
                val shopId = backStackEntry.arguments?.getLong("shopId") ?: 0L
                ShopDetailScreen(
                    shopId = shopId,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onEditClick = { id -> navController.navigate(Screen.EditShop.createRoute(id)) },
                    onAddCreditClick = { id -> navController.navigate(Screen.AddShopCredit.createRoute(id)) },
                    onAddPaymentClick = { id -> navController.navigate(Screen.AddShopPayment.createRoute(id)) }
                )
            }
            composable(
                route = Screen.EditShop.route,
                arguments = listOf(navArgument("shopId") { type = NavType.LongType })
            ) { backStackEntry ->
                val shopId = backStackEntry.arguments?.getLong("shopId") ?: 0L
                EditShopScreen(shopId = shopId, viewModel = viewModel, onBack = { navController.popBackStack() })
            }
            composable(
                route = Screen.AddShopCredit.route,
                arguments = listOf(navArgument("shopId") { type = NavType.LongType })
            ) { backStackEntry ->
                val shopId = backStackEntry.arguments?.getLong("shopId") ?: 0L
                AddShopCreditScreen(shopId = shopId, viewModel = viewModel, onBack = { navController.popBackStack() })
            }
            composable(
                route = Screen.AddShopPayment.route,
                arguments = listOf(navArgument("shopId") { type = NavType.LongType })
            ) { backStackEntry ->
                val shopId = backStackEntry.arguments?.getLong("shopId") ?: 0L
                AddShopPaymentScreen(shopId = shopId, viewModel = viewModel, onBack = { navController.popBackStack() })
            }

            composable(Screen.AddLoan.route) {
                AddLoanScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
            }
            composable(
                route = Screen.LoanDetail.route,
                arguments = listOf(navArgument("loanId") { type = NavType.LongType })
            ) { backStackEntry ->
                val loanId = backStackEntry.arguments?.getLong("loanId") ?: 0L
                LoanDetailScreen(
                    loanId = loanId,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onAddPaymentClick = { id -> navController.navigate(Screen.AddLoanPayment.createRoute(id)) }
                )
            }
            composable(
                route = Screen.AddLoanPayment.route,
                arguments = listOf(navArgument("loanId") { type = NavType.LongType })
            ) { backStackEntry ->
                val loanId = backStackEntry.arguments?.getLong("loanId") ?: 0L
                AddLoanPaymentScreen(loanId = loanId, viewModel = viewModel, onBack = { navController.popBackStack() })
            }

            composable(Screen.AddEmi.route) {
                AddEmiScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
            }
            composable(
                route = Screen.EmiDetail.route,
                arguments = listOf(navArgument("emiId") { type = NavType.LongType })
            ) { backStackEntry ->
                val emiId = backStackEntry.arguments?.getLong("emiId") ?: 0L
                EmiDetailScreen(
                    emiId = emiId,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onAddPaymentClick = { id -> navController.navigate(Screen.AddEmiPayment.createRoute(id)) }
                )
            }
            composable(
                route = Screen.AddEmiPayment.route,
                arguments = listOf(navArgument("emiId") { type = NavType.LongType })
            ) { backStackEntry ->
                val emiId = backStackEntry.arguments?.getLong("emiId") ?: 0L
                AddEmiPaymentScreen(emiId = emiId, viewModel = viewModel, onBack = { navController.popBackStack() })
            }

            composable(Screen.AddPersonalDebt.route) {
                AddPersonalDebtScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
            }
            composable(
                route = Screen.PersonalDebtDetail.route,
                arguments = listOf(navArgument("personId") { type = NavType.LongType })
            ) { backStackEntry ->
                val personId = backStackEntry.arguments?.getLong("personId") ?: 0L
                PersonalDebtDetailScreen(
                    personId = personId,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onAddRepaymentClick = { id -> navController.navigate(Screen.AddPersonalRepayment.createRoute(id)) }
                )
            }
            composable(
                route = Screen.AddPersonalRepayment.route,
                arguments = listOf(navArgument("debtId") { type = NavType.LongType })
            ) { backStackEntry ->
                val debtId = backStackEntry.arguments?.getLong("debtId") ?: 0L
                AddPersonalRepaymentScreen(debtId = debtId, viewModel = viewModel, onBack = { navController.popBackStack() })
            }

            composable(Screen.AddIncome.route) {
                AddIncomeScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
            }
            composable(Screen.AddExpense.route) {
                AddExpenseScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
            }

            composable(Screen.Search.route) {
                SearchScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
            }

            composable(Screen.BackupRestore.route) {
                BackupRestoreScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
            }
        }
    }
}
