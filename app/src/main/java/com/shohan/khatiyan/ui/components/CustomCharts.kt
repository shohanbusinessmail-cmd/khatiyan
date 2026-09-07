package com.shohan.khatiyan.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.shohan.khatiyan.util.CurrencyFormatter

data class ChartSlice(
    val label: String,
    val value: Long,
    val color: Color
)

@Composable
fun DonutChart(
    slices: List<ChartSlice>,
    modifier: Modifier = Modifier,
    chartSize: Dp = 160.dp,
    strokeWidth: Dp = 24.dp
) {
    val totalValue = slices.sumOf { it.value }.toFloat().coerceAtLeast(1f)
    var animationPlayed by remember { mutableStateOf(false) }
    val animateProgress by animateFloatAsState(
        targetValue = if (animationPlayed) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "donut_anim"
    )

    LaunchedEffect(Unit) {
        animationPlayed = true
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(chartSize),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(chartSize)) {
                var startAngle = -90f
                val strokeWidthPx = strokeWidth.toPx()

                slices.forEach { slice ->
                    val sweepAngle = (slice.value / totalValue) * 360f * animateProgress
                    if (sweepAngle > 0) {
                        drawArc(
                            color = slice.color,
                            startAngle = startAngle,
                            sweepAngle = sweepAngle,
                            useCenter = false,
                            style = Stroke(width = strokeWidthPx, cap = StrokeCap.Butt),
                            size = Size(size.width - strokeWidthPx, size.height - strokeWidthPx),
                            topLeft = Offset(strokeWidthPx / 2, strokeWidthPx / 2)
                        )
                        startAngle += sweepAngle
                    }
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "মোট দেনা",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = CurrencyFormatter.formatPaisa(slices.sumOf { it.value }),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Legend
        Column(modifier = Modifier.fillMaxWidth()) {
            slices.forEach { slice ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .padding(2.dp)
                    ) {
                        Canvas(modifier = Modifier.fillMaxWidth()) {
                            drawCircle(color = slice.color)
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = slice.label,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = CurrencyFormatter.formatPaisa(slice.value),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun SimpleBarChart(
    incomePaisa: Long,
    expensePaisa: Long,
    modifier: Modifier = Modifier
) {
    val maxVal = maxOf(incomePaisa, expensePaisa, 1L).toFloat()
    val incomeRatio = (incomePaisa / maxVal).coerceIn(0.05f, 1f)
    val expenseRatio = (expensePaisa / maxVal).coerceIn(0.05f, 1f)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp)
            .padding(16.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        // Income Bar
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = CurrencyFormatter.formatPaisa(incomePaisa),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height((100 * incomeRatio).dp)
            ) {
                Canvas(modifier = Modifier.fillMaxWidth()) {
                    drawRect(color = Color(0xFF006E1C))
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "আয়", style = MaterialTheme.typography.bodySmall)
        }

        // Expense Bar
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = CurrencyFormatter.formatPaisa(expensePaisa),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height((100 * expenseRatio).dp)
            ) {
                Canvas(modifier = Modifier.fillMaxWidth()) {
                    drawRect(color = Color(0xFFBA1A1A))
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "ব্যয়", style = MaterialTheme.typography.bodySmall)
        }
    }
}
