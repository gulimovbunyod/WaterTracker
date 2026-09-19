package com.watertracker.app.ui.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.watertracker.app.ui.components.CustomAmountDialog
import com.watertracker.app.ui.components.DayBarChart
import com.watertracker.app.ui.theme.GreenStatus
import com.watertracker.app.ui.theme.RedStatus
import com.watertracker.app.util.DayStatus

@Composable
fun StatsScreen(viewModel: StatsViewModel) {
    val records by viewModel.records.collectAsState()
    var editingRecord by remember { mutableStateOf<DayRecordUi?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Statistika (31 kun)") }) }
    ) { padding ->
        if (records.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Hali ma'lumot yo'q. Bugungi kun tugagach shu yerda ko'rinadi.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        DayBarChart(
                            records = records,
                            modifier = Modifier.fillMaxWidth().height(160.dp).padding(12.dp)
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                }
                items(records) { record ->
                    DayCard(record, onEditClick = { editingRecord = record })
                }
            }
        }
    }

    editingRecord?.let { record ->
        CustomAmountDialog(
            title = "${record.displayDate} uchun miqdor",
            onDismiss = { editingRecord = null },
            onConfirm = { amount, subtract ->
                viewModel.adjustDay(record.date, if (subtract) -amount else amount)
                editingRecord = null
            }
        )
    }
}

@Composable
fun DayCard(record: DayRecordUi, onEditClick: () -> Unit) {
    val color = if (record.status == DayStatus.GREEN) GreenStatus else RedStatus
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.10f))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(record.displayDate, style = MaterialTheme.typography.titleMedium)
                Text("Meyor: ${record.goalMl} ml", style = MaterialTheme.typography.bodySmall)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(color = color, shape = CircleShape)
                )
                Spacer(Modifier.width(8.dp))
                Text("${record.totalMl} ml", fontWeight = FontWeight.Bold)
                if (record.editable) {
                    Spacer(Modifier.width(4.dp))
                    IconButton(onClick = onEditClick, modifier = Modifier.size(28.dp)) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Tahrirlash",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
