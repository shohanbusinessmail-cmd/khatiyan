package com.shohan.khatiyan

import com.shohan.khatiyan.util.CurrencyFormatter
import com.shohan.khatiyan.util.DateUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FinancialCalculationTest {

    @Test
    fun testPaisaFormatting() {
        val paisa = 1250000L // 12,500.00 tk
        val formatted = CurrencyFormatter.formatPaisa(paisa, includeSymbol = false)
        assertTrue(formatted.contains("১২,৫০০") || formatted.contains("12,500"))
    }

    @Test
    fun testBengaliDigitsConversion() {
        val number = "1234567890"
        val converted = CurrencyFormatter.toBengaliDigits(number)
        assertEquals("১২৩৪৫৬৭৮৯০", converted)
    }

    @Test
    fun testNetDebtCalculation() {
        val totalCreditPaisa = 500000L // 5000 tk
        val totalPaymentPaisa = 200000L // 2000 tk
        val remaining = (totalCreditPaisa - totalPaymentPaisa).coerceAtLeast(0L)
        assertEquals(300000L, remaining)
    }

    @Test
    fun testOverdueLogic() {
        val pastDate = System.currentTimeMillis() - 86400000L * 5 // 5 days ago
        assertTrue(DateUtils.isOverdue(pastDate))
    }
}
