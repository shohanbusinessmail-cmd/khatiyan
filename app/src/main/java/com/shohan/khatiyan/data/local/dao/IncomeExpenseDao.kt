package com.shohan.khatiyan.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.shohan.khatiyan.data.local.entities.ExpenseEntity
import com.shohan.khatiyan.data.local.entities.IncomeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomeExpenseDao {
    // Income
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncome(income: IncomeEntity): Long

    @Update
    suspend fun updateIncome(income: IncomeEntity)

    @Delete
    suspend fun deleteIncome(income: IncomeEntity)

    @Query("SELECT * FROM incomes ORDER BY date DESC")
    fun getAllIncomes(): Flow<List<IncomeEntity>>

    @Query("SELECT COALESCE(SUM(amountPaisa), 0) FROM incomes WHERE date >= :startDate AND date <= :endDate")
    fun getIncomeSumInRange(startDate: Long, endDate: Long): Flow<Long>

    @Query("SELECT COALESCE(SUM(amountPaisa), 0) FROM incomes")
    fun getTotalIncomeAll(): Flow<Long>

    // Expense
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity): Long

    @Update
    suspend fun updateExpense(expense: ExpenseEntity)

    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Query("SELECT COALESCE(SUM(amountPaisa), 0) FROM expenses WHERE date >= :startDate AND date <= :endDate")
    fun getExpenseSumInRange(startDate: Long, endDate: Long): Flow<Long>

    @Query("SELECT COALESCE(SUM(amountPaisa), 0) FROM expenses")
    fun getTotalExpenseAll(): Flow<Long>
}
