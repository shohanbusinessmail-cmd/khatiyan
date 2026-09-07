package com.shohan.khatiyan.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shohan.khatiyan.data.local.entities.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions ORDER BY date DESC, id DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE date >= :startDate AND date <= :endDate ORDER BY date DESC, id DESC")
    fun getTransactionsInRange(startDate: Long, endDate: Long): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE module = :module ORDER BY date DESC, id DESC")
    fun getTransactionsByModule(module: String): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE title LIKE '%' || :query || '%' OR subtitle LIKE '%' || :query || '%' OR note LIKE '%' || :query || '%' ORDER BY date DESC")
    fun searchTransactions(query: String): Flow<List<TransactionEntity>>

    @Query("DELETE FROM transactions")
    suspend fun deleteAll()
}
