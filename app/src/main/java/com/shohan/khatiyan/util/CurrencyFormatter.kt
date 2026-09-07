package com.shohan.khatiyan.util

import java.text.NumberFormat
import java.util.Locale

object CurrencyFormatter {

    private val bnDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')

    fun formatPaisa(paisa: Long, includeSymbol: Boolean = true): String {
        val amount = paisa / 100.0
        return formatAmount(amount, includeSymbol)
    }

    fun formatAmount(amount: Double, includeSymbol: Boolean = true): String {
        val formatter = NumberFormat.getNumberInstance(Locale("bn", "BD"))
        formatter.minimumFractionDigits = 0
        formatter.maximumFractionDigits = 2
        val formattedNumber = formatter.format(amount)
        return if (includeSymbol) "$formattedNumber ৳" else formattedNumber
    }

    fun toBengaliDigits(number: Any): String {
        val str = number.toString()
        val sb = StringBuilder()
        for (ch in str) {
            if (ch in '0'..'9') {
                sb.append(bnDigits[ch - '0'])
            } else {
                sb.append(ch)
            }
        }
        return sb.toString()
    }
}
