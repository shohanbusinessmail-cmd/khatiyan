package com.shohan.khatiyan

import com.shohan.khatiyan.data.model.DashboardSummary
import com.shohan.khatiyan.util.InsightEngine
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class InsightEngineTest {

    @Test
    fun testInsightGenerationWithDebt() {
        val summary = DashboardSummary(
            totalDebtPaisa = 1000000L,
            thisMonthIncomePaisa = 5000000L,
            thisMonthExpensePaisa = 2000000L
        )
        val insights = InsightEngine.generateInsights(summary, emptyList())
        assertFalse(insights.isEmpty())
        assertTrue(insights.any { it.title.contains("মোট দায়") })
    }

    @Test
    fun testInsightGenerationZeroDebt() {
        val summary = DashboardSummary(
            totalDebtPaisa = 0L,
            thisMonthIncomePaisa = 5000000L,
            thisMonthExpensePaisa = 2000000L
        )
        val insights = InsightEngine.generateInsights(summary, emptyList())
        assertTrue(insights.any { it.title.contains("কোনো বকেয়া নেই") })
    }
}
