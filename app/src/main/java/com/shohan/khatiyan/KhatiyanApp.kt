package com.shohan.khatiyan

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.shohan.khatiyan.notification.DueReminderWorker
import com.shohan.khatiyan.notification.NotificationHelper
import java.util.concurrent.TimeUnit

class KhatiyanApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialize Notification Channels
        NotificationHelper.createNotificationChannel(this)

        // Schedule Background Due Payment Reminder WorkManager Job
        scheduleDueReminders()
    }

    private fun scheduleDueReminders() {
        val reminderWork = PeriodicWorkRequestBuilder<DueReminderWorker>(24, TimeUnit.HOURS)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "khatiyan_due_reminder_work",
            ExistingPeriodicWorkPolicy.KEEP,
            reminderWork
        )
    }
}
