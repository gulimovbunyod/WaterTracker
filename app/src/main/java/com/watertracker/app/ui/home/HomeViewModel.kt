package com.watertracker.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.watertracker.app.data.SettingsRepository
import com.watertracker.app.data.WaterRepository
import com.watertracker.app.util.ColorUtils
import com.watertracker.app.util.DayStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val totalMl: Int = 0,
    val goalMl: Int = SettingsRepository.DEFAULT_GOAL_ML,
    val status: DayStatus = DayStatus.GREEN
)

class HomeViewModel(private val repository: WaterRepository) : ViewModel() {

    // Status/rang hisob-kitobi uchun soatlik "tick" - vaqt o'tishi bilan
    // (yangi suv qo'shilmasa ham) holat yangilanib tursin. Bu ekrandagi
    // "o'tgan/qolgan vaqt" hisoblagichidan MUSTAQIL, alohida ishlaydi.
    private val statusTicker = flow {
        while (true) {
            emit(Unit)
            delay(3_600_000) // 1 soat
        }
    }

    val uiState: StateFlow<HomeUiState> = combine(
        repository.todayRecordFlow(),
        repository.goalFlow,
        statusTicker
    ) { record, goal, _ ->
        val total = record?.totalMl ?: 0
        HomeUiState(
            totalMl = total,
            goalMl = goal,
            status = ColorUtils.statusForToday(total, goal)
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())

    fun addWater(deltaMl: Int) {
        viewModelScope.launch { repository.addWater(deltaMl) }
    }

    fun setGoal(goalMl: Int) {
        viewModelScope.launch { repository.setGoal(goalMl) }
    }

    companion object {
        fun factory(repository: WaterRepository) = viewModelFactory {
            initializer { HomeViewModel(repository) }
        }
    }
}
