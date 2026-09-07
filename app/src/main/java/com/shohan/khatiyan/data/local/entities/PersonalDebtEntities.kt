package com.shohan.khatiyan.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "persons")
data class PersonEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val relationship: String = "বন্ধু", // "বন্ধু", "আত্মীয়", "পরিবার", "সহকর্মী", "পরিচিত", "অন্যান্য"
    val phone: String = "",
    val note: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "personal_debts",
    foreignKeys = [
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = ["id"],
            childColumns = ["personId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["personId"])]
)
data class PersonalDebtEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val personId: Long,
    val amountPaisa: Long,
    val date: Long,
    val expectedReturnDate: Long = 0,
    val note: String = "",
    val isIoweThem: Boolean = true, // true = I borrowed from them (my obligation), false = They borrowed from me
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "personal_repayments",
    foreignKeys = [
        ForeignKey(
            entity = PersonalDebtEntity::class,
            parentColumns = ["id"],
            childColumns = ["debtId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["debtId"])]
)
data class PersonalRepaymentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val debtId: Long,
    val date: Long,
    val amountPaisa: Long,
    val paymentMethod: String = "নগদ",
    val note: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
