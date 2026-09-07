package com.shohan.khatiyan.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {

    private val bengaliMonths = arrayOf(
        "জানুয়ারি", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল", "মে", "জুন",
        "জুলাই", "আগস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর"
    )

    fun formatDate(timestamp: Long): String {
        if (timestamp <= 0) return "-"
        val cal = Calendar.getInstance().apply { timeInMillis = timestamp }
        val day = CurrencyFormatter.toBengaliDigits(cal.get(Calendar.DAY_OF_MONTH))
        val month = bengaliMonths[cal.get(Calendar.MONTH)]
        val year = CurrencyFormatter.toBengaliDigits(cal.get(Calendar.YEAR))
        return "$day $month, $year"
    }

    fun formatDateShort(timestamp: Long): String {
        if (timestamp <= 0) return "-"
        val cal = Calendar.getInstance().apply { timeInMillis = timestamp }
        val day = CurrencyFormatter.toBengaliDigits(cal.get(Calendar.DAY_OF_MONTH))
        val month = bengaliMonths[cal.get(Calendar.MONTH)]
        return "$day $month"
    }

    fun getStartOfDay(timestamp: Long = System.currentTimeMillis()): Long {
        val cal = Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return cal.timeInMillis
    }

    fun getEndOfDay(timestamp: Long = System.currentTimeMillis()): Long {
        val cal = Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }
        return cal.timeInMillis
    }

    fun getStartOfMonth(timestamp: Long = System.currentTimeMillis()): Long {
        val cal = Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return cal.timeInMillis
    }

    fun getEndOfMonth(timestamp: Long = System.currentTimeMillis()): Long {
        val cal = Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }
        return cal.timeInMillis
    }

    fun isToday(timestamp: Long): Boolean {
        val todayStart = getStartOfDay()
        val todayEnd = getEndOfDay()
        return timestamp in todayStart..todayEnd
    }

    fun isOverdue(dueDate: Long): Boolean {
        if (dueDate <= 0) return false
        return getStartOfDay(dueDate) < getStartOfDay()
    }
}
