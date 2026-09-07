package com.shohan.khatiyan.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.shohan.khatiyan.data.local.KhatiyanDatabase
import com.shohan.khatiyan.util.CurrencyFormatter
import com.shohan.khatiyan.util.DateUtils
import kotlinx.coroutines.flow.first

class DueReminderWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend doWork(): Result {
        val db = KhatiyanDatabase.getDatabase(context)
        val today = System.currentTimeMillis()

        // Check Loans
        val loans = db.loanDao().getAllLoans().first()
        var dueTodayCount = 0
        for (loan in loans) {
            val totalPaidPaisa = db.loanDao().getTotalPaymentForLoan(loan.id).first()
            if (loan.totalPayablePaisa > totalPaidPaisa) {
                if (DateUtils.isToday(loan.firstPaymentDate) || DateUtils.isOverdue(loan.firstPaymentDate)) {
                    dueTodayCount++
                }
            }
        }

        // Check EMIs
        val emis = db.emiDao().getAllEmis().first()
        for (emi in emis) {
            val totalPaidPaisa = db.emiDao().getTotalPaymentForEmi(emi.id).first()
            if (emi.totalPayablePaisa > totalPaidPaisa) {
                if (DateUtils.isToday(emi.firstDueDate) || DateUtils.isOverdue(emi.firstDueDate)) {
                    dueTodayCount++
                }
            }
        }

        if (dueTodayCount > 0) {
            NotificationHelper.sendDueNotification(
                context = context,
                title = "পেমেন্ট রিমাইন্ডার - খতিয়ান",
                message = "আজ আপনার $dueTodayCount টি কিস্তি/লোন পরিশোধের তারিখ রয়েছে। অ্যাপে হিসাব চেক করুন।",
                notificationId = 1001
            )
        }

        return Result.success()
    }
}
