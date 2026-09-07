package com.shohan.khatiyan.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "incomes")
data class IncomeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: Long,
    val amountPaisa: Long,
    val source: String, // e.g., "Salary", "Business", "Freelance", "Gift", "Bonus", "Refund", "Other"
    val category: String = "সাধারণ আয়",
    val note: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: Long,
    val amountPaisa: Long,
    val category: String, // e.g., "খাবার", "বাজার", "যাতায়াত", "বাসা", "চিকিৎসা", "শিক্ষা", "বিল", "কেনাকাটা", "বিনোদনের", "অন্যান্য"
    val merchant: String = "",
    val note: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: String, // "INCOME", "EXPENSE", "SHOP"
    val iconName: String = "category",
    val isSystem: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
