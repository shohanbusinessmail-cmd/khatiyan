package com.shohan.khatiyan.data.model

import com.shohan.khatiyan.data.local.entities.EmiEntity
import com.shohan.khatiyan.data.local.entities.LoanEntity
import com.shohan.khatiyan.data.local.entities.PersonEntity
import com.shohan.khatiyan.data.local.entities.ShopEntity

data class DashboardSummary(
    val totalDebtPaisa: Long = 0,
    val todayPaidPaisa: Long = 0,
    val todayIncomePaisa: Long = 0,
    val todayExpensePaisa: Long = 0,
    val shopDebtPaisa: Long = 0,
    val loanDebtPaisa: Long = 0,
    val emiDebtPaisa: Long = 0,
    val personalDebtPaisa: Long = 0,
    val thisMonthIncomePaisa: Long = 0,
    val thisMonthExpensePaisa: Long = 0
)

data class ShopSummary(
    val shop: ShopEntity,
    val totalCreditPaisa: Long,
    val totalPaymentPaisa: Long,
    val remainingBalancePaisa: Long
)

data class LoanSummary(
    val loan: LoanEntity,
    val totalPaidPaisa: Long,
    val remainingBalancePaisa: Long,
    val nextDueDate: Long,
    val isOverdue: Boolean
)

data class EmiSummary(
    val emi: EmiEntity,
    val totalPaidPaisa: Long,
    val remainingBalancePaisa: Long,
    val installmentsPaid: Int,
    val installmentsRemaining: Int,
    val nextDueDate: Long,
    val isOverdue: Boolean
)

data class PersonSummary(
    val person: PersonEntity,
    val totalBorrowedPaisa: Long,
    val totalRepaidPaisa: Long,
    val remainingBalancePaisa: Long
)

enum class DueType {
    SHOP, LOAN, EMI, PERSONAL
}

data class DueItemModel(
    val id: Long,
    val title: String,
    val subtitle: String,
    val amountPaisa: Long,
    val dueDate: Long,
    val dueType: DueType,
    val isOverdue: Boolean
)

data class InsightModel(
    val title: String,
    val description: String,
    val iconType: String = "info"
)
