package com.shohan.khatiyan.data.repository

import com.shohan.khatiyan.data.local.KhatiyanDatabase
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
import com.shohan.khatiyan.data.model.DueType
import com.shohan.khatiyan.data.model.EmiSummary
import com.shohan.khatiyan.data.model.LoanSummary
import com.shohan.khatiyan.data.model.PersonSummary
import com.shohan.khatiyan.data.model.ShopSummary
import com.shohan.khatiyan.util.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first

class KhatiyanRepositoryImpl(private val db: KhatiyanDatabase) : KhatiyanRepository {

    private val shopDao = db.shopDao()
    private val loanDao = db.loanDao()
    private val emiDao = db.emiDao()
    private val personalDebtDao = db.personalDebtDao()
    private val incomeExpenseDao = db.incomeExpenseDao()
    private val transactionDao = db.transactionDao()
    private val categoryDao = db.categoryDao()

    override fun getDashboardSummaryFlow(): Flow<DashboardSummary> {
        val totalShopCredit = shopDao.getTotalShopCreditAll()
        val totalShopPayment = shopDao.getTotalShopPaymentAll()
        val totalLoanPayable = loanDao.getTotalLoanPayableAll()
        val totalLoanPayments = loanDao.getTotalLoanPaymentsAll()
        val totalEmiPayable = emiDao.getTotalEmiPayableAll()
        val totalEmiPayments = emiDao.getTotalEmiPaymentsAll()
        val totalPersonalBorrowed = personalDebtDao.getTotalPersonalBorrowedAll()
        val totalPersonalRepaid = personalDebtDao.getTotalPersonalRepaidAll()

        val monthStart = DateUtils.getStartOfMonth()
        val monthEnd = DateUtils.getEndOfMonth()
        val monthIncome = incomeExpenseDao.getIncomeSumInRange(monthStart, monthEnd)
        val monthExpense = incomeExpenseDao.getExpenseSumInRange(monthStart, monthEnd)

        return combine(
            totalShopCredit, totalShopPayment,
            totalLoanPayable, totalLoanPayments,
            totalEmiPayable, totalEmiPayments,
            totalPersonalBorrowed, totalPersonalRepaid,
            monthIncome, monthExpense
        ) { values ->
            val shopC = values[0] as Long
            val shopP = values[1] as Long
            val loanPayable = values[2] as Long
            val loanP = values[3] as Long
            val emiPayable = values[4] as Long
            val emiP = values[5] as Long
            val personalB = values[6] as Long
            val personalR = values[7] as Long
            val mIncome = values[8] as Long
            val mExpense = values[9] as Long

            val shopDebt = (shopC - shopP).coerceAtLeast(0L)
            val loanDebt = (loanPayable - loanP).coerceAtLeast(0L)
            val emiDebt = (emiPayable - emiP).coerceAtLeast(0L)
            val personalDebt = (personalB - personalR).coerceAtLeast(0L)

            val totalDebt = shopDebt + loanDebt + emiDebt + personalDebt

            DashboardSummary(
                totalDebtPaisa = totalDebt,
                todayPaidPaisa = 0L, // Aggregated dynamically if needed
                todayIncomePaisa = 0L,
                todayExpensePaisa = 0L,
                shopDebtPaisa = shopDebt,
                loanDebtPaisa = loanDebt,
                emiDebtPaisa = emiDebt,
                personalDebtPaisa = personalDebt,
                thisMonthIncomePaisa = mIncome,
                thisMonthExpensePaisa = mExpense
            )
        }
    }

    override fun getUpcomingDuesFlow(): Flow<List<DueItemModel>> {
        return combine(
            loanDao.getAllLoans(),
            emiDao.getAllEmis(),
            personalDebtDao.getAllPersonalDebts()
        ) { loans, emis, personalDebts ->
            val dueItems = mutableListOf<DueItemModel>()

            // Loans
            for (loan in loans) {
                val totalPaid = db.loanDao().getTotalPaymentForLoan(loan.id).first()
                if (loan.totalPayablePaisa > totalPaid) {
                    val remaining = loan.totalPayablePaisa - totalPaid
                    dueItems.add(
                        DueItemModel(
                            id = loan.id,
                            title = loan.institutionName,
                            subtitle = loan.loanTitle,
                            amountPaisa = loan.installmentAmountPaisa.coerceAtMost(remaining),
                            dueDate = loan.firstPaymentDate,
                            dueType = DueType.LOAN,
                            isOverdue = DateUtils.isOverdue(loan.firstPaymentDate)
                        )
                    )
                }
            }

            // EMIs
            for (emi in emis) {
                val totalPaid = db.emiDao().getTotalPaymentForEmi(emi.id).first()
                if (emi.totalPayablePaisa > totalPaid) {
                    val remaining = emi.totalPayablePaisa - totalPaid
                    dueItems.add(
                        DueItemModel(
                            id = emi.id,
                            title = emi.productName,
                            subtitle = emi.sellerName,
                            amountPaisa = emi.installmentAmountPaisa.coerceAtMost(remaining),
                            dueDate = emi.firstDueDate,
                            dueType = DueType.EMI,
                            isOverdue = DateUtils.isOverdue(emi.firstDueDate)
                        )
                    )
                }
            }

            // Personal
            for (pDebt in personalDebts) {
                if (pDebt.isIoweThem) {
                    val totalRepaid = db.personalDebtDao().getTotalRepaymentsForDebt(pDebt.id).first()
                    if (pDebt.amountPaisa > totalRepaid) {
                        val remaining = pDebt.amountPaisa - totalRepaid
                        val person = db.personalDebtDao().getPersonById(pDebt.personId)
                        dueItems.add(
                            DueItemModel(
                                id = pDebt.id,
                                title = person?.name ?: "ব্যক্তিগত ধার",
                                subtitle = pDebt.note.ifBlank { "ব্যক্তিগত পরিশোধ" },
                                amountPaisa = remaining,
                                dueDate = pDebt.expectedReturnDate,
                                dueType = DueType.PERSONAL,
                                isOverdue = DateUtils.isOverdue(pDebt.expectedReturnDate)
                            )
                        )
                    }
                }
            }

            dueItems.sortedBy { it.dueDate }
        }
    }

    // Shop Module Impl
    override fun getAllShopsFlow(): Flow<List<ShopSummary>> {
        return combine(
            shopDao.getAllShops(),
            shopDao.getAllShopCredits(),
            shopDao.getAllShopPayments()
        ) { shops, credits, payments ->
            shops.map { shop ->
                val totalC = credits.filter { it.shopId == shop.id }.sumOf { it.totalAmountPaisa }
                val totalP = payments.filter { it.shopId == shop.id }.sumOf { it.amountPaisa }
                ShopSummary(
                    shop = shop,
                    totalCreditPaisa = totalC,
                    totalPaymentPaisa = totalP,
                    remainingBalancePaisa = (totalC - totalP).coerceAtLeast(0L)
                )
            }
        }
    }

    override fun getShopByIdFlow(shopId: Long): Flow<ShopEntity?> = shopDao.getShopByIdFlow(shopId)
    override fun getCreditsForShopFlow(shopId: Long): Flow<List<ShopCreditEntity>> = shopDao.getCreditsForShop(shopId)
    override fun getPaymentsForShopFlow(shopId: Long): Flow<List<ShopPaymentEntity>> = shopDao.getPaymentsForShop(shopId)

    override suspend fun addShop(shop: ShopEntity): Long = shopDao.insertShop(shop)

    override suspend fun updateShop(shop: ShopEntity) = shopDao.updateShop(shop)

    override suspend fun addShopCredit(
        credit: ShopCreditEntity,
        items: List<ShopCreditItemEntity>,
        shopName: String
    ): Long {
        val creditId = shopDao.insertShopCredit(credit)
        if (items.isNotEmpty()) {
            val itemsWithId = items.map { it.copy(creditId = creditId) }
            shopDao.insertCreditItems(itemsWithId)
        }

        // Add to central transaction audit log
        transactionDao.insertTransaction(
            TransactionEntity(
                type = "CREDIT_PURCHASE",
                refId = creditId,
                module = "SHOP",
                date = credit.date,
                amountPaisa = credit.totalAmountPaisa,
                title = shopName,
                subtitle = "দোকানের বাকী কেনাকাটা",
                isCredit = false, // Debt increased
                note = credit.note
            )
        )
        return creditId
    }

    override suspend fun addShopPayment(payment: ShopPaymentEntity, shopName: String): Long {
        val paymentId = shopDao.insertShopPayment(payment)
        transactionDao.insertTransaction(
            TransactionEntity(
                type = "DEBT_PAYMENT",
                refId = paymentId,
                module = "SHOP",
                date = payment.date,
                amountPaisa = payment.amountPaisa,
                title = shopName,
                subtitle = "দোকানের বাকী পরিশোধ (${payment.paymentMethod})",
                isCredit = true, // Debt reduced
                note = payment.note
            )
        )
        return paymentId
    }

    override suspend fun deleteShop(shop: ShopEntity) = shopDao.deleteShop(shop)

    // Loan Module Impl
    override fun getAllLoansFlow(): Flow<List<LoanSummary>> {
        return combine(
            loanDao.getAllLoans(),
            loanDao.getAllLoanPayments()
        ) { loans, payments ->
            loans.map { loan ->
                val totalPaid = payments.filter { it.loanId == loan.id }.sumOf { it.amountPaisa }
                val remaining = (loan.totalPayablePaisa - totalPaid).coerceAtLeast(0L)
                LoanSummary(
                    loan = loan,
                    totalPaidPaisa = totalPaid,
                    remainingBalancePaisa = remaining,
                    nextDueDate = loan.firstPaymentDate,
                    isOverdue = remaining > 0 && DateUtils.isOverdue(loan.firstPaymentDate)
                )
            }
        }
    }

    override fun getLoanByIdFlow(loanId: Long): Flow<LoanEntity?> = loanDao.getLoanByIdFlow(loanId)
    override fun getPaymentsForLoanFlow(loanId: Long): Flow<List<LoanPaymentEntity>> = loanDao.getPaymentsForLoan(loanId)

    override suspend fun addLoan(loan: LoanEntity): Long {
        val loanId = loanDao.insertLoan(loan)
        transactionDao.insertTransaction(
            TransactionEntity(
                type = "LOAN_DISBURSEMENT",
                refId = loanId,
                module = "LOAN",
                date = loan.dateTaken,
                amountPaisa = loan.loanAmountPaisa,
                title = loan.institutionName,
                subtitle = loan.loanTitle,
                isCredit = false,
                note = loan.notes
            )
        )
        return loanId
    }

    override suspend fun addLoanPayment(payment: LoanPaymentEntity, institutionName: String): Long {
        val paymentId = loanDao.insertLoanPayment(payment)
        transactionDao.insertTransaction(
            TransactionEntity(
                type = "LOAN_PAYMENT",
                refId = paymentId,
                module = "LOAN",
                date = payment.date,
                amountPaisa = payment.amountPaisa,
                title = institutionName,
                subtitle = "লোনের কিস্তি পরিশোধ (${payment.paymentMethod})",
                isCredit = true,
                note = payment.note
            )
        )
        return paymentId
    }

    override suspend fun deleteLoan(loan: LoanEntity) = loanDao.deleteLoan(loan)

    // EMI Module Impl
    override fun getAllEmisFlow(): Flow<List<EmiSummary>> {
        return combine(
            emiDao.getAllEmis(),
            emiDao.getAllEmiPayments()
        ) { emis, payments ->
            emis.map { emi ->
                val totalPaid = payments.filter { it.emiId == emi.id }.sumOf { it.amountPaisa }
                val remaining = (emi.totalPayablePaisa - totalPaid).coerceAtLeast(0L)
                val installedPaidCount = if (emi.installmentAmountPaisa > 0) (totalPaid / emi.installmentAmountPaisa).toInt() else 0
                val remainingInstallments = (emi.numberOfInstallments - installedPaidCount).coerceAtLeast(0)

                EmiSummary(
                    emi = emi,
                    totalPaidPaisa = totalPaid,
                    remainingBalancePaisa = remaining,
                    installmentsPaid = installedPaidCount,
                    installmentsRemaining = remainingInstallments,
                    nextDueDate = emi.firstDueDate,
                    isOverdue = remaining > 0 && DateUtils.isOverdue(emi.firstDueDate)
                )
            }
        }
    }

    override fun getEmiByIdFlow(emiId: Long): Flow<EmiEntity?> = emiDao.getEmiByIdFlow(emiId)
    override fun getPaymentsForEmiFlow(emiId: Long): Flow<List<EmiPaymentEntity>> = emiDao.getPaymentsForEmi(emiId)

    override suspend fun addEmi(emi: EmiEntity): Long {
        val emiId = emiDao.insertEmi(emi)
        transactionDao.insertTransaction(
            TransactionEntity(
                type = "EMI_PURCHASE",
                refId = emiId,
                module = "EMI",
                date = emi.purchaseDate,
                amountPaisa = emi.financedAmountPaisa,
                title = emi.productName,
                subtitle = emi.sellerName,
                isCredit = false,
                note = emi.notes
            )
        )
        return emiId
    }

    override suspend fun addEmiPayment(payment: EmiPaymentEntity, productName: String): Long {
        val paymentId = emiDao.insertEmiPayment(payment)
        transactionDao.insertTransaction(
            TransactionEntity(
                type = "EMI_PAYMENT",
                refId = paymentId,
                module = "EMI",
                date = payment.date,
                amountPaisa = payment.amountPaisa,
                title = productName,
                subtitle = "ইএমআই কিস্তি পরিশোধ (${payment.paymentMethod})",
                isCredit = true,
                note = payment.note
            )
        )
        return paymentId
    }

    override suspend fun deleteEmi(emi: EmiEntity) = emiDao.deleteEmi(emi)

    // Personal Debt Module Impl
    override fun getAllPersonsFlow(): Flow<List<PersonSummary>> {
        return combine(
            personalDebtDao.getAllPersons(),
            personalDebtDao.getAllPersonalDebts(),
            personalDebtDao.getAllPersonalRepayments()
        ) { persons, debts, repayments ->
            persons.map { person ->
                val personDebts = debts.filter { it.personId == person.id && it.isIoweThem }
                val totalBorrowed = personDebts.sumOf { it.amountPaisa }
                val debtIds = personDebts.map { it.id }.toSet()
                val totalRepaid = repayments.filter { debtIds.contains(it.debtId) }.sumOf { it.amountPaisa }
                val remaining = (totalBorrowed - totalRepaid).coerceAtLeast(0L)

                PersonSummary(
                    person = person,
                    totalBorrowedPaisa = totalBorrowed,
                    totalRepaidPaisa = totalRepaid,
                    remainingBalancePaisa = remaining
                )
            }
        }
    }

    override fun getPersonByIdFlow(personId: Long): Flow<PersonEntity?> = personalDebtDao.getPersonByIdFlow(personId)
    override fun getDebtsForPersonFlow(personId: Long): Flow<List<PersonalDebtEntity>> = personalDebtDao.getDebtsForPerson(personId)
    override fun getRepaymentsForDebtFlow(debtId: Long): Flow<List<PersonalRepaymentEntity>> = personalDebtDao.getRepaymentsForDebt(debtId)

    override suspend fun addPerson(person: PersonEntity): Long = personalDebtDao.insertPerson(person)

    override suspend fun addPersonalDebt(debt: PersonalDebtEntity, personName: String): Long {
        val debtId = personalDebtDao.insertPersonalDebt(debt)
        transactionDao.insertTransaction(
            TransactionEntity(
                type = "PERSONAL_BORROW",
                refId = debtId,
                module = "PERSONAL",
                date = debt.date,
                amountPaisa = debt.amountPaisa,
                title = personName,
                subtitle = if (debt.isIoweThem) "ব্যক্তিগত ধার গ্রহণ" else "ব্যক্তিগত ধার প্রদান",
                isCredit = !debt.isIoweThem,
                note = debt.note
            )
        )
        return debtId
    }

    override suspend fun addPersonalRepayment(repayment: PersonalRepaymentEntity, debtTitle: String): Long {
        val repaymentId = personalDebtDao.insertRepayment(repayment)
        transactionDao.insertTransaction(
            TransactionEntity(
                type = "PERSONAL_REPAYMENT",
                refId = repaymentId,
                module = "PERSONAL",
                date = repayment.date,
                amountPaisa = repayment.amountPaisa,
                title = debtTitle,
                subtitle = "ধার পরিশোধ (${repayment.paymentMethod})",
                isCredit = true,
                note = repayment.note
            )
        )
        return repaymentId
    }

    override suspend fun deletePerson(person: PersonEntity) = personalDebtDao.deletePerson(person)

    // Income & Expense Impl
    override fun getAllIncomesFlow(): Flow<List<IncomeEntity>> = incomeExpenseDao.getAllIncomes()
    override fun getAllExpensesFlow(): Flow<List<ExpenseEntity>> = incomeExpenseDao.getAllExpenses()

    override suspend fun addIncome(income: IncomeEntity): Long {
        val incomeId = incomeExpenseDao.insertIncome(income)
        transactionDao.insertTransaction(
            TransactionEntity(
                type = "INCOME",
                refId = incomeId,
                module = "INCOME",
                date = income.date,
                amountPaisa = income.amountPaisa,
                title = income.source,
                subtitle = "আয় (${income.category})",
                isCredit = true,
                note = income.note
            )
        )
        return incomeId
    }

    override suspend fun addExpense(expense: ExpenseEntity): Long {
        val expenseId = incomeExpenseDao.insertExpense(expense)
        transactionDao.insertTransaction(
            TransactionEntity(
                type = "EXPENSE",
                refId = expenseId,
                module = "EXPENSE",
                date = expense.date,
                amountPaisa = expense.amountPaisa,
                title = expense.category,
                subtitle = if (expense.merchant.isNotBlank()) expense.merchant else "ব্যয়",
                isCredit = false,
                note = expense.note
            )
        )
        return expenseId
    }

    override suspend fun deleteIncome(income: IncomeEntity) = incomeExpenseDao.deleteIncome(income)
    override suspend fun deleteExpense(expense: ExpenseEntity) = incomeExpenseDao.deleteExpense(expense)

    // Central Transaction History
    override fun getAllTransactionsFlow(): Flow<List<TransactionEntity>> = transactionDao.getAllTransactions()
    override fun searchTransactionsFlow(query: String): Flow<List<TransactionEntity>> = transactionDao.searchTransactions(query)

    // Backup & Restore
    override suspend fun generateBackupData(): BackupDataJson {
        return BackupDataJson(
            shops = shopDao.getAllShops().first(),
            shopCredits = shopDao.getAllShopCredits().first(),
            shopPayments = shopDao.getAllShopPayments().first(),
            loans = loanDao.getAllLoans().first(),
            loanPayments = loanDao.getAllLoanPayments().first(),
            emis = emiDao.getAllEmis().first(),
            emiPayments = emiDao.getAllEmiPayments().first(),
            persons = personalDebtDao.getAllPersons().first(),
            personalDebts = personalDebtDao.getAllPersonalDebts().first(),
            personalRepayments = personalDebtDao.getAllPersonalRepayments().first(),
            incomes = incomeExpenseDao.getAllIncomes().first(),
            expenses = incomeExpenseDao.getAllExpenses().first(),
            categories = categoryDao.getAllCategories().first(),
            transactions = transactionDao.getAllTransactions().first()
        )
    }

    override suspend fun restoreBackupData(backupData: BackupDataJson): Boolean {
        return try {
            backupData.shops.forEach { shopDao.insertShop(it) }
            backupData.shopCredits.forEach { shopDao.insertShopCredit(it) }
            backupData.shopCreditItems.forEach { shopDao.insertCreditItems(listOf(it)) }
            backupData.shopPayments.forEach { shopDao.insertShopPayment(it) }
            backupData.loans.forEach { loanDao.insertLoan(it) }
            backupData.loanPayments.forEach { loanDao.insertLoanPayment(it) }
            backupData.emis.forEach { emiDao.insertEmi(it) }
            backupData.emiPayments.forEach { emiDao.insertEmiPayment(it) }
            backupData.persons.forEach { personalDebtDao.insertPerson(it) }
            backupData.personalDebts.forEach { personalDebtDao.insertPersonalDebt(it) }
            backupData.personalRepayments.forEach { personalDebtDao.insertRepayment(it) }
            backupData.incomes.forEach { incomeExpenseDao.insertIncome(it) }
            backupData.expenses.forEach { incomeExpenseDao.insertExpense(it) }
            backupData.categories.forEach { categoryDao.insertCategory(it) }
            backupData.transactions.forEach { transactionDao.insertTransaction(it) }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
