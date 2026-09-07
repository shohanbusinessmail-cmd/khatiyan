package com.shohan.khatiyan.data.repository

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
import com.shohan.khatiyan.data.model.BackupDataJson
import com.shohan.khatiyan.data.model.DashboardSummary
import com.shohan.khatiyan.data.model.DueItemModel
import com.shohan.khatiyan.data.model.EmiSummary
import com.shohan.khatiyan.data.model.LoanSummary
import com.shohan.khatiyan.data.model.PersonSummary
import com.shohan.khatiyan.data.model.ShopSummary
import kotlinx.coroutines.flow.Flow

interface KhatiyanRepository {
    // Dashboard & Summaries
    fun getDashboardSummaryFlow(): Flow<DashboardSummary>
    fun getUpcomingDuesFlow(): Flow<List<DueItemModel>>

    // Shop Module
    fun getAllShopsFlow(): Flow<List<ShopSummary>>
    fun getShopByIdFlow(shopId: Long): Flow<ShopEntity?>
    fun getCreditsForShopFlow(shopId: Long): Flow<List<ShopCreditEntity>>
    fun getPaymentsForShopFlow(shopId: Long): Flow<List<ShopPaymentEntity>>
    suspend fun addShop(shop: ShopEntity): Long
    suspend fun addShopCredit(credit: ShopCreditEntity, items: List<ShopCreditItemEntity>, shopName: String): Long
    suspend fun addShopPayment(payment: ShopPaymentEntity, shopName: String): Long
    suspend fun deleteShop(shop: ShopEntity)

    // Loan Module
    fun getAllLoansFlow(): Flow<List<LoanSummary>>
    fun getLoanByIdFlow(loanId: Long): Flow<LoanEntity?>
    fun getPaymentsForLoanFlow(loanId: Long): Flow<List<LoanPaymentEntity>>
    suspend fun addLoan(loan: LoanEntity): Long
    suspend fun addLoanPayment(payment: LoanPaymentEntity, institutionName: String): Long
    suspend fun deleteLoan(loan: LoanEntity)

    // EMI Module
    fun getAllEmisFlow(): Flow<List<EmiSummary>>
    fun getEmiByIdFlow(emiId: Long): Flow<EmiEntity?>
    fun getPaymentsForEmiFlow(emiId: Long): Flow<List<EmiPaymentEntity>>
    suspend fun addEmi(emi: EmiEntity): Long
    suspend fun addEmiPayment(payment: EmiPaymentEntity, productName: String): Long
    suspend fun deleteEmi(emi: EmiEntity)

    // Personal Debt Module
    fun getAllPersonsFlow(): Flow<List<PersonSummary>>
    fun getPersonByIdFlow(personId: Long): Flow<PersonEntity?>
    fun getDebtsForPersonFlow(personId: Long): Flow<List<PersonalDebtEntity>>
    fun getRepaymentsForDebtFlow(debtId: Long): Flow<List<PersonalRepaymentEntity>>
    suspend fun addPerson(person: PersonEntity): Long
    suspend fun addPersonalDebt(debt: PersonalDebtEntity, personName: String): Long
    suspend fun addPersonalRepayment(repayment: PersonalRepaymentEntity, debtTitle: String): Long
    suspend fun deletePerson(person: PersonEntity)

    // Income & Expense Module
    fun getAllIncomesFlow(): Flow<List<IncomeEntity>>
    fun getAllExpensesFlow(): Flow<List<ExpenseEntity>>
    suspend fun addIncome(income: IncomeEntity): Long
    suspend fun addExpense(expense: ExpenseEntity): Long
    suspend fun deleteIncome(income: IncomeEntity)
    suspend fun deleteExpense(expense: ExpenseEntity)

    // Central Transaction History
    fun getAllTransactionsFlow(): Flow<List<TransactionEntity>>
    fun searchTransactionsFlow(query: String): Flow<List<TransactionEntity>>

    // Backup & Restore
    suspend fun generateBackupData(): BackupDataJson
    suspend fun restoreBackupData(backupData: BackupDataJson): Boolean
}
