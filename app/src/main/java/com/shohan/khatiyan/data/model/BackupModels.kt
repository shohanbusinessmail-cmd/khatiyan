package com.shohan.khatiyan.data.model

import com.shohan.khatiyan.data.local.entities.CategoryEntity
import com.shohan.khatiyan.data.local.entities.EmiEntity
import com.shohan.khatiyan.data.local.entities.EmiPaymentEntity
import com.shohan.khatiyan.data.local.entities.ExpenseEntity
import com.shohan.khatiyan.data.local.entities.IncomeEntity
import com.shohan.khatiyan.data.local.entities.LoanEntity
import com.shohan.khatiyan.data.local.entities.LoanPaymentEntity
import com.shohan.khatiyan.data.local.entities.PersonEntity
import com.shohan.khatiyan.data.local.entities.PersonalDebtEntity
import com.shohan.khatiyan.data.local.entities.PersonalRepaymentEntity
import com.shohan.khatiyan.data.local.entities.ShopCreditEntity
import com.shohan.khatiyan.data.local.entities.ShopCreditItemEntity
import com.shohan.khatiyan.data.local.entities.ShopEntity
import com.shohan.khatiyan.data.local.entities.ShopPaymentEntity
import com.shohan.khatiyan.data.local.entities.TransactionEntity

data class BackupDataJson(
    val version: Int = 1,
    val appName: String = "Khatiyan",
    val exportDate: Long = System.currentTimeMillis(),
    val shops: List<ShopEntity> = emptyList(),
    val shopCredits: List<ShopCreditEntity> = emptyList(),
    val shopCreditItems: List<ShopCreditItemEntity> = emptyList(),
    val shopPayments: List<ShopPaymentEntity> = emptyList(),
    val loans: List<LoanEntity> = emptyList(),
    val loanPayments: List<LoanPaymentEntity> = emptyList(),
    val emis: List<EmiEntity> = emptyList(),
    val emiPayments: List<EmiPaymentEntity> = emptyList(),
    val persons: List<PersonEntity> = emptyList(),
    val personalDebts: List<PersonalDebtEntity> = emptyList(),
    val personalRepayments: List<PersonalRepaymentEntity> = emptyList(),
    val incomes: List<IncomeEntity> = emptyList(),
    val expenses: List<ExpenseEntity> = emptyList(),
    val categories: List<CategoryEntity> = emptyList(),
    val transactions: List<TransactionEntity> = emptyList()
)
