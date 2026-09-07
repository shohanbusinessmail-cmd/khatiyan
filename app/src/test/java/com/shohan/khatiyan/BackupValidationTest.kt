package com.shohan.khatiyan

import com.google.gson.Gson
import com.shohan.khatiyan.data.local.entities.ShopEntity
import com.shohan.khatiyan.data.model.BackupDataJson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class BackupValidationTest {

    @Test
    fun testJsonBackupSerialization() {
        val backup = BackupDataJson(
            version = 1,
            shops = listOf(ShopEntity(id = 1, name = "রহিম স্টোর", ownerName = "রহিম সাহেব"))
        )
        val gson = Gson()
        val json = gson.toJson(backup)
        assertNotNull(json)

        val restored = gson.fromJson(json, BackupDataJson::class.java)
        assertEquals(1, restored.shops.size)
        assertEquals("রহিম স্টোর", restored.shops[0].name)
    }
}
