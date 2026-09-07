package com.shohan.khatiyan.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shohan.khatiyan.data.local.dao.CategoryDao
import com.shohan.khatiyan.data.local.dao.EmiDao
import com.shohan.khatiyan.data.local.dao.IncomeExpenseDao
import com.shohan.khatiyan.data.local.dao.LoanDao
import com.shohan.khatiyan.data.local.dao.PersonalDebtDao
import com.shohan.khatiyan.data.local.dao.ShopDao
import com.shohan.khatiyan.data.local.dao.TransactionDao
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

@Database(
    entities = [
        ShopEntity::class,
        ShopCreditEntity::class,
        ShopCreditItemEntity::class,
        ShopPaymentEntity::class,
        LoanEntity::class,
        LoanPaymentEntity::class,
        EmiEntity::class,
        EmiPaymentEntity::class,
        PersonEntity::class,
        PersonalDebtEntity::class,
        PersonalRepaymentEntity::class,
        IncomeEntity::class,
        ExpenseEntity::class,
        CategoryEntity::class,
        TransactionEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class KhatiyanDatabase : RoomDatabase() {

    abstract fun shopDao(): ShopDao
    abstract fun loanDao(): LoanDao
    abstract fun emiDao(): EmiDao
    abstract fun personalDebtDao(): PersonalDebtDao
    abstract fun incomeExpenseDao(): IncomeExpenseDao
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        private var INSTANCE: KhatiyanDatabase? = null

        fun getDatabase(context: Context): KhatiyanDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KhatiyanDatabase::class.java,
                    "khatiyan_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
