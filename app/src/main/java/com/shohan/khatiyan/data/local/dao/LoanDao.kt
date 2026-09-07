package com.shohan.khatiyan.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.shohan.khatiyan.data.local.entities.LoanEntity
import com.shohan.khatiyan.data.local.entities.LoanPaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LoanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLoan(loan: LoanEntity): Long

    @Update
    suspend fun updateLoan(loan: LoanEntity)

    @Delete
    suspend fun deleteLoan(loan: LoanEntity)

    @Query("SELECT * FROM loans ORDER BY createdAt DESC")
    fun getAllLoans(): Flow<List<LoanEntity>>

    @Query("SELECT * FROM loans WHERE id = :loanId")
    suspend fun getLoanById(loanId: Long): LoanEntity?

    @Query("SELECT * FROM loans WHERE id = :loanId")
    fun getLoanByIdFlow(loanId: Long): Flow<LoanEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLoanPayment(payment: LoanPaymentEntity): Long

    @Query("SELECT * FROM loan_payments WHERE loanId = :loanId ORDER BY date DESC")
    fun getPaymentsForLoan(loanId: Long): Flow<List<LoanPaymentEntity>>

    @Query("SELECT * FROM loan_payments ORDER BY date DESC")
    fun getAllLoanPayments(): Flow<List<LoanPaymentEntity>>

    @Query("SELECT COALESCE(SUM(totalPayablePaisa), 0) FROM loans")
    fun getTotalLoanPayableAll(): Flow<Long>

    @Query("SELECT COALESCE(SUM(amountPaisa), 0) FROM loan_payments")
    fun getTotalLoanPaymentsAll(): Flow<Long>

    @Query("SELECT COALESCE(SUM(amountPaisa), 0) FROM loan_payments WHERE loanId = :loanId")
    fun getTotalPaymentForLoan(loanId: Long): Flow<Long>
}
