package com.shohan.khatiyan.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String, // CREDIT_PURCHASE, DEBT_PAYMENT, LOAN_DISBURSEMENT, LOAN_PAYMENT, EMI_PURCHASE, EMI_PAYMENT, PERSONAL_BORROW, PERSONAL_REPAYMENT, INCOME, EXPENSE
    val refId: Long, // ID of the referenced entity
    val module: String, // "SHOP", "LOAN", "EMI", "PERSONAL", "INCOME", "EXPENSE"
    val date: Long,
    val amountPaisa: Long,
    val title: String,
    val subtitle: String = "",
    val isCredit: Boolean, // true if money coming in or debt reduced, false if debt increased or money going out
    val note: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
