package com.example.aplikasipencatatankeuanganpribadi.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.example.aplikasipencatatankeuanganpribadi.model.DataBulananChart
import com.example.aplikasipencatatankeuanganpribadi.util.FormatUtils
import kotlin.math.max

class MonthlyBarChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var dataBulanan: List<DataBulananChart> = emptyList()

    private val density = resources.displayMetrics.density

    private val paintGrid = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#ECEEF5")
        style = Paint.Style.STROKE
        strokeWidth = 1.2f * density
        pathEffect = DashPathEffect(floatArrayOf(4f * density, 4f * density), 0f)
    }

    private val paintAxisText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#A0A0B8")
        textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            11f,
            resources.displayMetrics
        )
    }

    private val paintIncomeBar = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#8B5CF6") // Purple Income bar
        style = Paint.Style.FILL
    }

    private val paintExpenseBar = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#6EE7B7") // Mint Expense bar
        style = Paint.Style.FILL
    }

    private val barRect = RectF()

    fun setData(list: List<DataBulananChart>) {
        dataBulanan = list
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (width == 0 || height == 0) return

        val leftPadding = 52f * density
        val rightPadding = 12f * density
        val topPadding = 14f * density
        val bottomPadding = 28f * density

        val chartWidth = width - leftPadding - rightPadding
        val chartHeight = height - topPadding - bottomPadding
        if (chartWidth <= 0 || chartHeight <= 0) return

        var maxVal = 1_000_000.0
        for (d in dataBulanan) {
            maxVal = max(maxVal, max(d.pemasukan, d.pengeluaran))
        }
        // Bulatkan ke atas agar rapi
        maxVal *= 1.15

        // Gambar 4 garis horizontal putus-putus & label sumbu Y
        val steps = 3
        for (i in 0..steps) {
            val fraction = i.toFloat() / steps
            val y = topPadding + chartHeight * (1f - fraction)
            val nilai = maxVal * fraction

            canvas.drawLine(leftPadding, y, width - rightPadding, y, paintGrid)
            val label = FormatUtils.formatRupiahSingkat(nilai)
            canvas.drawText(label, 4f * density, y + 4f * density, paintAxisText)
        }

        if (dataBulanan.isEmpty()) return

        val count = dataBulanan.size
        val slotWidth = chartWidth / count
        val barWidth = (slotWidth * 0.22f).coerceAtMost(12f * density)
        val gapBetweenBars = 4f * density
        val cornerRadius = barWidth / 2f
        val baseLineY = topPadding + chartHeight

        paintAxisText.textAlign = Paint.Align.CENTER

        for ((index, item) in dataBulanan.withIndex()) {
            val centerX = leftPadding + slotWidth * index + slotWidth / 2f

            // Bar Pengeluaran (Mint Green - Kiri)
            val expenseRatio = (item.pengeluaran / maxVal).toFloat().coerceIn(0f, 1f)
            val expenseHeight = max(expenseRatio * chartHeight, 6f * density)
            val expLeft = centerX - gapBetweenBars / 2f - barWidth
            val expRight = centerX - gapBetweenBars / 2f
            barRect.set(expLeft, baseLineY - expenseHeight, expRight, baseLineY)
            canvas.drawRoundRect(barRect, cornerRadius, cornerRadius, paintExpenseBar)

            // Bar Pemasukan (Purple - Kanan)
            val incomeRatio = (item.pemasukan / maxVal).toFloat().coerceIn(0f, 1f)
            val incomeHeight = max(incomeRatio * chartHeight, 6f * density)
            val incLeft = centerX + gapBetweenBars / 2f
            val incRight = centerX + gapBetweenBars / 2f + barWidth
            barRect.set(incLeft, baseLineY - incomeHeight, incRight, baseLineY)
            canvas.drawRoundRect(barRect, cornerRadius, cornerRadius, paintIncomeBar)

            // Label Bulan di bawah
            canvas.drawText(
                item.labelBulanSingkat,
                centerX,
                baseLineY + 18f * density,
                paintAxisText
            )
        }

        paintAxisText.textAlign = Paint.Align.LEFT
    }
}
