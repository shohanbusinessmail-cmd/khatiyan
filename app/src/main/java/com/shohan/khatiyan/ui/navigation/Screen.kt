package com.shohan.khatiyan.ui.navigation

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Dashboard : Screen("dashboard")
    object Accounts : Screen("accounts")
    object Transactions : Screen("transactions")
    object Reports : Screen("reports")
    object Settings : Screen("settings")

    // Sub-screens
    object ShopList : Screen("shop_list")
    object ShopDetail : Screen("shop_detail/{shopId}") {
        fun createRoute(shopId: Long) = "shop_detail/$shopId"
    }
    object AddShop : Screen("add_shop")
    object AddShopCredit : Screen("add_shop_credit/{shopId}") {
        fun createRoute(shopId: Long) = "add_shop_credit/$shopId"
    }
    object AddShopPayment : Screen("add_shop_payment/{shopId}") {
        fun createRoute(shopId: Long) = "add_shop_payment/$shopId"
    }

    object LoanList : Screen("loan_list")
    object LoanDetail : Screen("loan_detail/{loanId}") {
        fun createRoute(loanId: Long) = "loan_detail/$loanId"
    }
    object AddLoan : Screen("add_loan")
    object AddLoanPayment : Screen("add_loan_payment/{loanId}") {
        fun createRoute(loanId: Long) = "add_loan_payment/$loanId"
    }

    object EmiList : Screen("emi_list")
    object EmiDetail : Screen("emi_detail/{emiId}") {
        fun createRoute(emiId: Long) = "emi_detail/$emiId"
    }
    object AddEmi : Screen("add_emi")
    object AddEmiPayment : Screen("add_emi_payment/{emiId}") {
        fun createRoute(emiId: Long) = "add_emi_payment/$emiId"
    }

    object PersonalDebtList : Screen("personal_list")
    object PersonalDebtDetail : Screen("personal_detail/{personId}") {
        fun createRoute(personId: Long) = "personal_detail/$personId"
    }
    object AddPersonalDebt : Screen("add_personal")
    object AddPersonalRepayment : Screen("add_personal_repayment/{debtId}") {
        fun createRoute(debtId: Long) = "add_personal_repayment/$debtId"
    }

    object IncomeExpense : Screen("income_expense")
    object AddIncome : Screen("add_income")
    object AddExpense : Screen("add_expense")

    object Search : Screen("search")
    object BackupRestore : Screen("backup_restore")
}
