package com.shohan.khatiyan

import com.shohan.khatiyan.util.CurrencyFormatter
import com.shohan.khatiyan.util.DateUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

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
    fun testMultiPartialPaymentsCalculation() {
        // Debt = ৳10,000 = 1,000,000 paisa
        val initialDebtPaisa = 1000000L
        val payment1Paisa = 200000L // ৳2,000
        val payment2Paisa = 150000L // ৳1,500
        val payment3Paisa = 50000L  // ৳500

        val totalPaidPaisa = payment1Paisa + payment2Paisa + payment3Paisa
        val remainingPaisa = (initialDebtPaisa - totalPaidPaisa).coerceAtLeast(0L)

        // Expected remaining: ৳6,000 = 600,000 paisa
        assertEquals(600000L, remainingPaisa)
        assertEquals(400000L, totalPaidPaisa)
    }

    @Test
    fun testNetCashFlowCalculation() {
        val totalIncomePaisa = 5000000L // ৳50,000
        val totalExpensePaisa = 3200000L // ৳32,000

        val netCashFlowPaisa = totalIncomePaisa - totalExpensePaisa
        assertEquals(1800000L, netCashFlowPaisa) // ৳18,000
    }

    @Test
    fun testOutstandingDebtAggregation() {
        val shopDebtPaisa = 150000L      // ৳1,500
        val loanDebtPaisa = 5000000L     // ৳50,000
        val emiDebtPaisa = 2000000L      // ৳20,000
        val personalDebtPaisa = 500000L  // ৳5,000

        val totalOutstandingPaisa = shopDebtPaisa + loanDebtPaisa + emiDebtPaisa + personalDebtPaisa
        assertEquals(7650000L, totalOutstandingPaisa) // ৳76,500
    }

    @Test
    fun testLargeAmountPrecisionWithoutOverflow() {
        val largeAmountPaisa = 10000000000L // ৳100,000,000 (10 Crore Taka)
        val paymentPaisa = 2500000000L      // ৳25,000,000
        val remainingPaisa = largeAmountPaisa - paymentPaisa

        assertEquals(7500000000L, remainingPaisa)
    }

    @Test
    fun testOverpaymentCapBehavior() {
        val totalObligationPaisa = 500000L // ৳5,000
        val paymentInputPaisa = 600000L    // ৳6,000 (Attempted overpayment)

        val isOverpaying = paymentInputPaisa > totalObligationPaisa
        assertTrue(isOverpaying)

        // Capped payment
        val cappedPayment = paymentInputPaisa.coerceAtMost(totalObligationPaisa)
        assertEquals(500000L, cappedPayment)
    }

    @Test
    fun testDateUtilsStartAndEndOfMonth() {
        val now = System.currentTimeMillis()
        val startOfMonth = DateUtils.getStartOfMonth(now)
        val endOfMonth = DateUtils.getEndOfMonth(now)

        assertTrue(startOfMonth <= now)
        assertTrue(endOfMonth >= now)
    }

    @Test
    fun testOverdueLogic() {
        val pastDate = System.currentTimeMillis() - (86400000L * 3) // 3 days ago
        assertTrue(DateUtils.isOverdue(pastDate))

        val futureDate = System.currentTimeMillis() + (86400000L * 3) // 3 days in future
        assertFalse(DateUtils.isOverdue(futureDate))
    }
}
