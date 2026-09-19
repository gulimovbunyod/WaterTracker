package com.watertracker.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import com.watertracker.app.ui.stats.DayRecordUi
import com.watertracker.app.ui.theme.GreenStatus
import com.watertracker.app.ui.theme.RedStatus
import com.watertracker.app.util.DayStatus

@Composable
fun DayBarChart(records: List<DayRecordUi>, modifier: Modifier = Modifier) {
    val chronological = records.sortedBy { it.date }
    if (chronological.isEmpty()) return
    val maxVal = (chronological.maxOfOrNull { it.totalMl } ?: 0).coerceAtLeast(2000)

    Canvas(modifier = modifier) {
        val count = chronological.size
        val slot = size.width / count
        val barWidth = slot * 0.6f
        chronological.forEachIndexed { index, record ->
            val barHeight = (record.totalMl.toFloat() / maxVal) * size.height
            val color = if (record.status == DayStatus.GREEN) GreenStatus else RedStatus
            val x = index * slot + (slot - barWidth) / 2
            drawRoundRect(
                color = color,
                topLeft = Offset(x, size.height - barHeight),
                size = Size(barWidth, barHeight.coerceAtLeast(4f)),
                cornerRadius = CornerRadius(4.dp.toPx())
            )
        }
    }
}
