package com.shohan.khatiyan.ui.screens.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shohan.khatiyan.data.local.KhatiyanDatabase
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
import com.shohan.khatiyan.data.model.BackupDataJson
import com.shohan.khatiyan.data.model.DashboardSummary
import com.shohan.khatiyan.data.model.DueItemModel
import com.shohan.khatiyan.data.model.EmiSummary
import com.shohan.khatiyan.data.model.LoanSummary
import com.shohan.khatiyan.data.model.PersonSummary
import com.shohan.khatiyan.data.model.ShopSummary
import com.shohan.khatiyan.data.preferences.UserPreferences
import com.shohan.khatiyan.data.preferences.UserPreferencesRepository
import com.shohan.khatiyan.data.repository.KhatiyanRepository
import com.shohan.khatiyan.data.repository.KhatiyanRepositoryImpl
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = KhatiyanDatabase.getDatabase(application)
    val repository: KhatiyanRepository = KhatiyanRepositoryImpl(db)
    val userPreferencesRepository = UserPreferencesRepository(application)

    val userPreferences: StateFlow<UserPreferences> = userPreferencesRepository.userPreferencesFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserPreferences()
        )

    val dashboardSummary: StateFlow<DashboardSummary> = repository.getDashboardSummaryFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DashboardSummary()
        )

    val upcomingDues: StateFlow<List<DueItemModel>> = repository.getUpcomingDuesFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val shops: StateFlow<List<ShopSummary>> = repository.getAllShopsFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val loans: StateFlow<List<LoanSummary>> = repository.getAllLoansFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val emis: StateFlow<List<EmiSummary>> = repository.getAllEmisFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val persons: StateFlow<List<PersonSummary>> = repository.getAllPersonsFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val incomes: StateFlow<List<IncomeEntity>> = repository.getAllIncomesFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val expenses: StateFlow<List<ExpenseEntity>> = repository.getAllExpensesFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val transactions = repository.getAllTransactionsFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun completeOnboarding(userName: String) {
        viewModelScope.launch {
            userPreferencesRepository.setOnboardingCompleted(true, userName)
        }
    }

    fun addShop(shop: ShopEntity, onComplete: (Long) -> Unit = {}) {
        viewModelScope.launch {
            val id = repository.addShop(shop)
            onComplete(id)
        }
    }

    fun updateShop(shop: ShopEntity, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.updateShop(shop)
            onComplete()
        }
    }

    fun deleteShop(shop: ShopEntity, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.deleteShop(shop)
            onComplete()
        }
    }

    fun addShopCredit(credit: ShopCreditEntity, items: List<ShopCreditItemEntity>, shopName: String, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.addShopCredit(credit, items, shopName)
            onComplete()
        }
    }

    fun addShopPayment(payment: ShopPaymentEntity, shopName: String, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.addShopPayment(payment, shopName)
            onComplete()
        }
    }

    fun addLoan(loan: LoanEntity, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.addLoan(loan)
            onComplete()
        }
    }

    fun addLoanPayment(payment: LoanPaymentEntity, institutionName: String, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.addLoanPayment(payment, institutionName)
            onComplete()
        }
    }

    fun addEmi(emi: EmiEntity, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.addEmi(emi)
            onComplete()
        }
    }

    fun addEmiPayment(payment: EmiPaymentEntity, productName: String, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.addEmiPayment(payment, productName)
            onComplete()
        }
    }

    fun addPerson(person: PersonEntity, onComplete: (Long) -> Unit = {}) {
        viewModelScope.launch {
            val id = repository.addPerson(person)
            onComplete(id)
        }
    }

    fun addPersonalDebt(debt: PersonalDebtEntity, personName: String, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.addPersonalDebt(debt, personName)
            onComplete()
        }
    }

    fun addPersonalRepayment(repayment: PersonalRepaymentEntity, debtTitle: String, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.addPersonalRepayment(repayment, debtTitle)
            onComplete()
        }
    }

    fun addIncome(income: IncomeEntity, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.addIncome(income)
            onComplete()
        }
    }

    fun addExpense(expense: ExpenseEntity, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.addExpense(expense)
            onComplete()
        }
    }

    fun restoreBackup(backupData: BackupDataJson, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.restoreBackupData(backupData)
            if (success) {
                userPreferencesRepository.updateLastBackupTime(System.currentTimeMillis())
            }
            onResult(success)
        }
    }
}
