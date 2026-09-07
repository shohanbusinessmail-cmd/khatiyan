package com.shohan.khatiyan.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.shohan.khatiyan.data.local.entities.EmiEntity
import com.shohan.khatiyan.data.local.entities.EmiPaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EmiDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmi(emi: EmiEntity): Long

    @Update
    suspend fun updateEmi(emi: EmiEntity)

    @Delete
    suspend fun deleteEmi(emi: EmiEntity)

    @Query("SELECT * FROM emis ORDER BY createdAt DESC")
    fun getAllEmis(): Flow<List<EmiEntity>>

    @Query("SELECT * FROM emis WHERE id = :emiId")
    suspend fun getEmiById(emiId: Long): EmiEntity?

    @Query("SELECT * FROM emis WHERE id = :emiId")
    fun getEmiByIdFlow(emiId: Long): Flow<EmiEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmiPayment(payment: EmiPaymentEntity): Long

    @Query("SELECT * FROM emi_payments WHERE emiId = :emiId ORDER BY date DESC")
    fun getPaymentsForEmi(emiId: Long): Flow<List<EmiPaymentEntity>>

    @Query("SELECT * FROM emi_payments ORDER BY date DESC")
    fun getAllEmiPayments(): Flow<List<EmiPaymentEntity>>

    @Query("SELECT COALESCE(SUM(totalPayablePaisa), 0) FROM emis")
    fun getTotalEmiPayableAll(): Flow<Long>

    @Query("SELECT COALESCE(SUM(amountPaisa), 0) FROM emi_payments")
    fun getTotalEmiPaymentsAll(): Flow<Long>

    @Query("SELECT COALESCE(SUM(amountPaisa), 0) FROM emi_payments WHERE emiId = :emiId")
    fun getTotalPaymentForEmi(emiId: Long): Flow<Long>
}
