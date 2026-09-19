package com.watertracker.app.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.watertracker.app.ui.components.WaterProgressRing
import com.watertracker.app.ui.theme.GreenStatus
import com.watertracker.app.ui.theme.RedStatus
import com.watertracker.app.util.DayStatus

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val state by viewModel.uiState.collectAsState()
    var showGoalDialog by remember { mutableStateOf(false) }
    var showCustomDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Suv Tracker") },
                actions = {
                    IconButton(onClick = { showGoalDialog = true }) {
                        Icon(Icons.Default.Settings, contentDescription = "Meyorni sozlash")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(8.dp))
            WaterProgressRing(
                totalMl = state.totalMl,
                goalMl = state.goalMl,
                status = state.status,
                modifier = Modifier.height(220.dp).fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = if (state.status == DayStatus.GREEN)
                    "Siz jadal ketyapsiz \uD83D\uDC4D"
                else
                    "Orqada qolyapsiz, ko'proq suv iching \uD83D\uDCA7",
                color = if (state.status == DayStatus.GREEN) GreenStatus else RedStatus,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(28.dp))
            Text("Tezkor qo'shish", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(100, 200, 300, 500).forEach { amount ->
                    FilledTonalButton(onClick = { viewModel.addWater(amount) }) {
                        Text("+$amount")
                    }
                }
            }
            Spacer(Modifier.height(10.dp))
            Text("Xato bosdingizmi? Ayirish:", style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(100, 200, 300, 500).forEach { amount ->
                    OutlinedButton(onClick = { viewModel.addWater(-amount) }) {
                        Text("-$amount")
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            TextButton(onClick = { showCustomDialog = true }) {
                Text("Boshqa miqdor kiritish")
            }
            Spacer(Modifier.height(24.dp))
        }
    }

    if (showGoalDialog) {
        GoalDialog(
            currentGoal = state.goalMl,
            onDismiss = { showGoalDialog = false },
            onConfirm = { newGoal ->
                viewModel.setGoal(newGoal)
                showGoalDialog = false
            }
        )
    }

    if (showCustomDialog) {
        CustomAmountDialog(
            onDismiss = { showCustomDialog = false },
            onConfirm = { amount, subtract ->
                viewModel.addWater(if (subtract) -amount else amount)
                showCustomDialog = false
            }
        )
    }
}

@Composable
fun GoalDialog(currentGoal: Int, onDismiss: () -> Unit, onConfirm: (Int) -> Unit) {
    var text by remember { mutableStateOf(currentGoal.toString()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Kunlik meyorni belgilang") },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { input -> text = input.filter { it.isDigit() } },
                label = { Text("Meyor (ml)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(onClick = {
                val value = text.toIntOrNull()
                if (value != null && value > 0) onConfirm(value)
            }) { Text("Saqlash") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Bekor qilish") } }
    )
}

@Composable
fun CustomAmountDialog(onDismiss: () -> Unit, onConfirm: (Int, Boolean) -> Unit) {
    var text by remember { mutableStateOf("") }
    var isSubtract by remember { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Miqdor kiriting") },
        text = {
            Column {
                OutlinedTextField(
                    value = text,
                    onValueChange = { input -> text = input.filter { it.isDigit() } },
                    label = { Text("Miqdor (ml)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                Spacer(Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Ayirish sifatida qo'shish")
                    Spacer(Modifier.width(8.dp))
                    Switch(checked = isSubtract, onCheckedChange = { isSubtract = it })
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val amount = text.toIntOrNull()
                if (amount != null && amount > 0) onConfirm(amount, isSubtract)
            }) { Text("Tasdiqlash") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Bekor qilish") } }
    )
}
