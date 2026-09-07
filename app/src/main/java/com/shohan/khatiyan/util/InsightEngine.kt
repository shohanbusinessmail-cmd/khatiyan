package com.shohan.khatiyan.util

import com.shohan.khatiyan.data.model.DashboardSummary
import com.shohan.khatiyan.data.model.DueItemModel
import com.shohan.khatiyan.data.model.InsightModel

object InsightEngine {

    fun generateInsights(
        summary: DashboardSummary,
        upcomingDues: List<DueItemModel>
    ): List<InsightModel> {
        val insights = mutableListOf<InsightModel>()

        // Rule 1: Total Debt Check
        if (summary.totalDebtPaisa > 0) {
            val formattedDebt = CurrencyFormatter.formatPaisa(summary.totalDebtPaisa)
            insights.add(
                InsightModel(
                    title = "বর্তমান মোট দায়",
                    description = "আপনার সর্বমোট বকেয়া বা পরিশোধযোগ্য ঋণের পরিমাণ $formattedDebt।",
                    iconType = "warning"
                )
            )
        } else {
            insights.add(
                InsightModel(
                    title = "কোনো বকেয়া নেই!",
                    description = "অভিনন্দন! আপনার কোনো দোকানের বাকী বা লোন বকেয়া নেই।",
                    iconType = "success"
                )
            )
        }

        // Rule 2: Overdue items
        val overdueCount = upcomingDues.count { it.isOverdue }
        if (overdueCount > 0) {
            insights.add(
                InsightModel(
                    title = "সময় পেরিয়ে যাওয়া কিস্তি",
                    description = "আপনার $overdueCount টি পেমেন্টের পরিশোধের সময় অতিবাহিত হয়ে গেছে। দ্রুত পরিশোধ করুন।",
                    iconType = "danger"
                )
            )
        }

        // Rule 3: Monthly Net Cash Flow
        if (summary.thisMonthIncomePaisa > 0 || summary.thisMonthExpensePaisa > 0) {
            val netPaisa = summary.thisMonthIncomePaisa - summary.thisMonthExpensePaisa
            if (netPaisa >= 0) {
                val formattedNet = CurrencyFormatter.formatPaisa(netPaisa)
                insights.add(
                    InsightModel(
                        title = "এই মাসের সঞ্চয়/উদ্বৃত্ত",
                        description = "এই মাসে আয় ব্যয়ের তুলনায় $formattedNet বেশি রয়েছে।",
                        iconType = "success"
                    )
                )
            } else {
                val formattedNet = CurrencyFormatter.formatPaisa(-netPaisa)
                insights.add(
                    InsightModel(
                        title = "এই মাসের অতিরিক্ত ব্যয়",
                        description = "এই মাসে আপনার ব্যয় আয়ের তুলনায় $formattedNet বেশি হয়েছে।",
                        iconType = "warning"
                    )
                )
            }
        }

        // Rule 4: Upcoming 7 days due
        val dueSoonCount = upcomingDues.count { !it.isOverdue }
        if (dueSoonCount > 0) {
            insights.add(
                InsightModel(
                    title = "আসন্ন পরিশোধ",
                    description = "সামনে আপনার মোট $dueSoonCount টি নির্ধারিত পেমেন্ট রয়েছে।",
                    iconType = "info"
                )
            )
        }

        return insights
    }
}
