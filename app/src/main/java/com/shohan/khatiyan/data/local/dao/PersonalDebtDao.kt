package com.shohan.khatiyan.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.shohan.khatiyan.data.local.entities.PersonEntity
import com.shohan.khatiyan.data.local.entities.PersonalDebtEntity
import com.shohan.khatiyan.data.local.entities.PersonalRepaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonalDebtDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerson(person: PersonEntity): Long

    @Update
    suspend fun updatePerson(person: PersonEntity)

    @Delete
    suspend fun deletePerson(person: PersonEntity)

    @Query("SELECT * FROM persons ORDER BY createdAt DESC")
    fun getAllPersons(): Flow<List<PersonEntity>>

    @Query("SELECT * FROM persons WHERE id = :personId")
    suspend fun getPersonById(personId: Long): PersonEntity?

    @Query("SELECT * FROM persons WHERE id = :personId")
    fun getPersonByIdFlow(personId: Long): Flow<PersonEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPersonalDebt(debt: PersonalDebtEntity): Long

    @Query("SELECT * FROM personal_debts WHERE personId = :personId ORDER BY date DESC")
    fun getDebtsForPerson(personId: Long): Flow<List<PersonalDebtEntity>>

    @Query("SELECT * FROM personal_debts ORDER BY date DESC")
    fun getAllPersonalDebts(): Flow<List<PersonalDebtEntity>>

    @Query("SELECT * FROM personal_debts WHERE id = :debtId")
    suspend fun getPersonalDebtById(debtId: Long): PersonalDebtEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepayment(repayment: PersonalRepaymentEntity): Long

    @Query("SELECT * FROM personal_repayments WHERE debtId = :debtId ORDER BY date DESC")
    fun getRepaymentsForDebt(debtId: Long): Flow<List<PersonalRepaymentEntity>>

    @Query("SELECT * FROM personal_repayments ORDER BY date DESC")
    fun getAllPersonalRepayments(): Flow<List<PersonalRepaymentEntity>>

    @Query("SELECT COALESCE(SUM(amountPaisa), 0) FROM personal_debts WHERE isIoweThem = 1")
    fun getTotalPersonalBorrowedAll(): Flow<Long>

    @Query("SELECT COALESCE(SUM(r.amountPaisa), 0) FROM personal_repayments r INNER JOIN personal_debts d ON r.debtId = d.id WHERE d.isIoweThem = 1")
    fun getTotalPersonalRepaidAll(): Flow<Long>

    @Query("SELECT COALESCE(SUM(r.amountPaisa), 0) FROM personal_repayments r WHERE r.debtId = :debtId")
    fun getTotalRepaymentsForDebt(debtId: Long): Flow<Long>
}
