package com.shohan.khatiyan.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.shohan.khatiyan.data.local.entities.ShopCreditEntity
import com.shohan.khatiyan.data.local.entities.ShopCreditItemEntity
import com.shohan.khatiyan.data.local.entities.ShopEntity
import com.shohan.khatiyan.data.local.entities.ShopPaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShop(shop: ShopEntity): Long

    @Update
    suspend fun updateShop(shop: ShopEntity)

    @Delete
    suspend fun deleteShop(shop: ShopEntity)

    @Query("SELECT * FROM shops ORDER BY createdAt DESC")
    fun getAllShops(): Flow<List<ShopEntity>>

    @Query("SELECT * FROM shops WHERE id = :shopId")
    suspend fun getShopById(shopId: Long): ShopEntity?

    @Query("SELECT * FROM shops WHERE id = :shopId")
    fun getShopByIdFlow(shopId: Long): Flow<ShopEntity?>

    // Shop Credits
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShopCredit(credit: ShopCreditEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCreditItems(items: List<ShopCreditItemEntity>)

    @Query("SELECT * FROM shop_credits WHERE shopId = :shopId ORDER BY date DESC")
    fun getCreditsForShop(shopId: Long): Flow<List<ShopCreditEntity>>

    @Query("SELECT * FROM shop_credits ORDER BY date DESC")
    fun getAllShopCredits(): Flow<List<ShopCreditEntity>>

    @Query("SELECT * FROM shop_credit_items WHERE creditId = :creditId")
    suspend fun getItemsForCredit(creditId: Long): List<ShopCreditItemEntity>

    // Shop Payments
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShopPayment(payment: ShopPaymentEntity): Long

    @Query("SELECT * FROM shop_payments WHERE shopId = :shopId ORDER BY date DESC")
    fun getPaymentsForShop(shopId: Long): Flow<List<ShopPaymentEntity>>

    @Query("SELECT * FROM shop_payments ORDER BY date DESC")
    fun getAllShopPayments(): Flow<List<ShopPaymentEntity>>

    @Query("SELECT COALESCE(SUM(totalAmountPaisa), 0) FROM shop_credits WHERE shopId = :shopId")
    fun getTotalCreditForShop(shopId: Long): Flow<Long>

    @Query("SELECT COALESCE(SUM(amountPaisa), 0) FROM shop_payments WHERE shopId = :shopId")
    fun getTotalPaymentForShop(shopId: Long): Flow<Long>

    @Query("SELECT COALESCE(SUM(totalAmountPaisa), 0) FROM shop_credits")
    fun getTotalShopCreditAll(): Flow<Long>

    @Query("SELECT COALESCE(SUM(amountPaisa), 0) FROM shop_payments")
    fun getTotalShopPaymentAll(): Flow<Long>
}
