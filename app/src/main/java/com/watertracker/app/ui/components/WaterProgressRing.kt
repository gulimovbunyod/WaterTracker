package com.watertracker.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.watertracker.app.ui.theme.GreenStatus
import com.watertracker.app.ui.theme.RedStatus
import com.watertracker.app.ui.theme.TrackGray
import com.watertracker.app.util.DayStatus

@Composable
fun WaterProgressRing(
    totalMl: Int,
    goalMl: Int,
    status: DayStatus,
    modifier: Modifier = Modifier
) {
    val progress = if (goalMl > 0) (totalMl.toFloat() / goalMl.toFloat()).coerceIn(0f, 1f) else 0f
    val color = if (status == DayStatus.GREEN) GreenStatus else RedStatus
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "progress")

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidthPx = 22.dp.toPx()
            val inset = strokeWidthPx / 2
            val arcSize = Size(size.width - strokeWidthPx, size.height - strokeWidthPx)
            drawArc(
                color = TrackGray,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = androidx.compose.ui.geometry.Offset(inset, inset),
                size = arcSize,
                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
            )
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                topLeft = androidx.compose.ui.geometry.Offset(inset, inset),
                size = arcSize,
                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$totalMl ml",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Meyor: $goalMl ml",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
