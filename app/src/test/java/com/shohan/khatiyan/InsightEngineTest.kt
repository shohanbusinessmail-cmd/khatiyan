package com.shohan.khatiyan

import com.shohan.khatiyan.data.model.DashboardSummary
import com.shohan.khatiyan.util.InsightEngine
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.text.Normalizer

class InsightEngineTest {

    /**
     * Bengali text can encode the same grapheme two ways — e.g. য় as the precomposed
     * U+09DF, or as য (U+09AF) followed by the nukta U+09BC. Both render identically but
     * are different Strings, and this source tree genuinely contains a mix of the two.
     * Every Bengali comparison here is therefore normalised first.
     */
    private fun String.normalised(): String = Normalizer.normalize(this, Normalizer.Form.NFC)

    private fun List<com.shohan.khatiyan.data.model.InsightModel>.hasTitleContaining(
        fragment: String
    ): Boolean = any { it.title.normalised().contains(fragment.normalised()) }

    @Test
    fun testInsightGenerationWithDebt() {
        val summary = DashboardSummary(
            totalDebtPaisa = 1000000L,
            thisMonthIncomePaisa = 5000000L,
            thisMonthExpensePaisa = 2000000L
        )
        val insights = InsightEngine.generateInsights(summary, emptyList())
        assertFalse(insights.isEmpty())
        assertTrue(insights.hasTitleContaining("মোট দায়"))
    }

    @Test
    fun testInsightGenerationZeroDebt() {
        val summary = DashboardSummary(
            totalDebtPaisa = 0L,
            thisMonthIncomePaisa = 5000000L,
            thisMonthExpensePaisa = 2000000L
        )
        val insights = InsightEngine.generateInsights(summary, emptyList())
        assertTrue(insights.hasTitleContaining("কোনো বকেয়া নেই"))
    }

    @Test
    fun testSurplusInsightWhenIncomeExceedsExpense() {
        val summary = DashboardSummary(
            totalDebtPaisa = 0L,
            thisMonthIncomePaisa = 5000000L,
            thisMonthExpensePaisa = 2000000L
        )
        val insights = InsightEngine.generateInsights(summary, emptyList())
        assertTrue(insights.hasTitleContaining("সঞ্চয়"))
    }

    @Test
    fun testOverspendInsightWhenExpenseExceedsIncome() {
        val summary = DashboardSummary(
            totalDebtPaisa = 0L,
            thisMonthIncomePaisa = 1000000L,
            thisMonthExpensePaisa = 4000000L
        )
        val insights = InsightEngine.generateInsights(summary, emptyList())
        assertTrue(insights.hasTitleContaining("অতিরিক্ত ব্যয়"))
    }
}
