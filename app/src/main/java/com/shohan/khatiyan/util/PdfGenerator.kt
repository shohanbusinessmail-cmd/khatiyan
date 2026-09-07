package com.shohan.khatiyan.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import com.shohan.khatiyan.data.model.DashboardSummary
import java.io.OutputStream

object PdfGenerator {

    fun generatePdfReport(
        context: Context,
        uri: Uri,
        userName: String,
        summary: DashboardSummary
    ): Boolean {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 Size (595 x 842 pt)
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        val titlePaint = Paint().apply {
            color = Color.parseColor("#006C4C")
            textSize = 24f
            isFakeBoldText = true
        }

        val subtitlePaint = Paint().apply {
            color = Color.parseColor("#4C635B")
            textSize = 12f
        }

        val headerPaint = Paint().apply {
            color = Color.parseColor("#006C4C")
            textSize = 14f
            isFakeBoldText = true
        }

        val textPaint = Paint().apply {
            color = Color.parseColor("#191C1A")
            textSize = 12f
        }

        val bgPaint = Paint().apply {
            color = Color.parseColor("#F6FBF6")
        }

        val cardBgPaint = Paint().apply {
            color = Color.parseColor("#E8F5E9")
        }

        var y = 50f

        // Draw Background
        canvas.drawRect(0f, 0f, 595f, 842f, bgPaint)

        // Title Header
        canvas.drawText("খতিয়ান (Khatiyan)", 40f, y, titlePaint)
        y += 20f
        canvas.drawText("সব হিসাব, এক জায়গায়", 40f, y, subtitlePaint)
        y += 30f

        // Divider Line
        val linePaint = Paint().apply {
            color = Color.parseColor("#006C4C")
            strokeWidth = 2f
        }
        canvas.drawLine(40f, y, 555f, y, linePaint)
        y += 30f

        // User & Date Details
        canvas.drawText("প্রস্তুতকারী: $userName", 40f, y, textPaint)
        canvas.drawText("তারিখ: ${DateUtils.formatDate(System.currentTimeMillis())}", 350f, y, textPaint)
        y += 40f

        // Financial Summary Box
        canvas.drawRect(40f, y, 555f, y + 140f, cardBgPaint)
        var boxY = y + 30f
        canvas.drawText("আর্থিক সারসংক্ষেপ (Financial Overview)", 60f, boxY, headerPaint)
        boxY += 30f
        canvas.drawText("সর্বমোট দেনা / বকেয়া: ${CurrencyFormatter.formatPaisa(summary.totalDebtPaisa)}", 60f, boxY, textPaint)
        boxY += 25f
        canvas.drawText("এই মাসের মোট আয়: ${CurrencyFormatter.formatPaisa(summary.thisMonthIncomePaisa)}", 60f, boxY, textPaint)
        boxY += 25f
        canvas.drawText("এই মাসের মোট ব্যয়: ${CurrencyFormatter.formatPaisa(summary.thisMonthExpensePaisa)}", 60f, boxY, textPaint)

        y += 180f

        // Outstanding Debt Breakdown Table
        canvas.drawText("বকেয়ার খাতভিত্তিক বিবরণ", 40f, y, headerPaint)
        y += 25f

        val itemLeft = 40f
        val itemRight = 300f

        canvas.drawText("• দোকানের বাকী:", itemLeft, y, textPaint)
        canvas.drawText(CurrencyFormatter.formatPaisa(summary.shopDebtPaisa), itemRight, y, textPaint)
        y += 25f

        canvas.drawText("• ব্যাংক / এনজিও লোন:", itemLeft, y, textPaint)
        canvas.drawText(CurrencyFormatter.formatPaisa(summary.loanDebtPaisa), itemRight, y, textPaint)
        y += 25f

        canvas.drawText("• ইএমআই / কিস্তি:", itemLeft, y, textPaint)
        canvas.drawText(CurrencyFormatter.formatPaisa(summary.emiDebtPaisa), itemRight, y, textPaint)
        y += 25f

        canvas.drawText("• ব্যক্তিগত ধার:", itemLeft, y, textPaint)
        canvas.drawText(CurrencyFormatter.formatPaisa(summary.personalDebtPaisa), itemRight, y, textPaint)
        y += 40f

        // Footer
        canvas.drawLine(40f, 780f, 555f, 780f, linePaint)
        val footerPaint = Paint().apply {
            color = Color.GRAY
            textSize = 10f
        }
        canvas.drawText("তৈরি করেছেন: Shohan Khan | ইমেইল: helloiamshohan@gmail.com", 40f, 800f, footerPaint)

        pdfDocument.finishPage(page)

        return try {
            val outputStream: OutputStream? = context.contentResolver.openOutputStream(uri)
            if (outputStream != null) {
                pdfDocument.writeTo(outputStream)
                outputStream.close()
                pdfDocument.close()
                true
            } else {
                pdfDocument.close()
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            pdfDocument.close()
            false
        }
    }
}
