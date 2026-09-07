package com.shohan.khatiyan.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "shops")
data class ShopEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val ownerName: String = "",
    val phone: String = "",
    val address: String = "",
    val note: String = "",
    val category: String = "মুদি দোকান",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "shop_credits",
    foreignKeys = [
        ForeignKey(
            entity = ShopEntity::class,
            parentColumns = ["id"],
            childColumns = ["shopId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["shopId"])]
)
data class ShopCreditEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val shopId: Long,
    val date: Long,
    val totalAmountPaisa: Long, // Money stored in paisa (e.g., 100 tk = 10000 paisa)
    val note: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "shop_credit_items",
    foreignKeys = [
        ForeignKey(
            entity = ShopCreditEntity::class,
            parentColumns = ["id"],
            childColumns = ["creditId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["creditId"])]
)
data class ShopCreditItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val creditId: Long,
    val itemName: String,
    val quantity: Double = 1.0,
    val unit: String = "কেজি",
    val unitPricePaisa: Long = 0,
    val lineTotalPaisa: Long = 0
)

@Entity(
    tableName = "shop_payments",
    foreignKeys = [
        ForeignKey(
            entity = ShopEntity::class,
            parentColumns = ["id"],
            childColumns = ["shopId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["shopId"])]
)
data class ShopPaymentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val shopId: Long,
    val date: Long,
    val amountPaisa: Long,
    val paymentMethod: String = "নগদ",
    val note: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
