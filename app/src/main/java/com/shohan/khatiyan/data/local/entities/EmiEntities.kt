package com.shohan.khatiyan.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "emis")
data class EmiEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productName: String,
    val sellerName: String,
    val purchaseDate: Long,
    val cashPricePaisa: Long = 0,
    val financedAmountPaisa: Long,
    val downPaymentPaisa: Long = 0,
    val totalPayablePaisa: Long,
    val installmentAmountPaisa: Long,
    val numberOfInstallments: Int,
    val frequency: String = "মাসিক",
    val firstDueDate: Long,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "emi_payments",
    foreignKeys = [
        ForeignKey(
            entity = EmiEntity::class,
            parentColumns = ["id"],
            childColumns = ["emiId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["emiId"])]
)
data class EmiPaymentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val emiId: Long,
    val date: Long,
    val amountPaisa: Long,
    val paymentMethod: String = "নগদ",
    val note: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
