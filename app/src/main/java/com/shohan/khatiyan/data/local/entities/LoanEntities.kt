package com.shohan.khatiyan.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "loans")
data class LoanEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val institutionName: String,
    val loanTitle: String,
    val loanAmountPaisa: Long,
    val dateTaken: Long,
    val interestRate: Double = 0.0,
    val processingFeePaisa: Long = 0,
    val totalPayablePaisa: Long,
    val installmentAmountPaisa: Long,
    val frequency: String = "মাসিক", // "মাসিক", "সাপ্তাহিক", "দ্বি-সাপ্তাহিক"
    val totalInstallments: Int = 1,
    val firstPaymentDate: Long,
    val maturityDate: Long = 0,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "loan_payments",
    foreignKeys = [
        ForeignKey(
            entity = LoanEntity::class,
            parentColumns = ["id"],
            childColumns = ["loanId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["loanId"])]
)
data class LoanPaymentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val loanId: Long,
    val date: Long,
    val amountPaisa: Long,
    val paymentMethod: String = "নগদ",
    val note: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
