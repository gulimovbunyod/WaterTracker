package com.watertracker.app.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.watertracker.app.data.WaterRepository
import com.watertracker.app.util.ColorUtils
import com.watertracker.app.util.DateUtils
import com.watertracker.app.util.DayStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DayRecordUi(
    val date: String,
    val displayDate: String,
    val totalMl: Int,
    val goalMl: Int,
    val status: DayStatus,
    val editable: Boolean
)

class StatsViewModel(private val repository: WaterRepository) : ViewModel() {

    // Ro'yxat chegarasi (bugun/kecha) vaqt o'tishi bilan ham to'g'ri qolishi uchun soatlik "tick".
    private val ticker = flow {
        while (true) {
            emit(Unit)
            delay(3_600_000)
        }
    }

    val records: StateFlow<List<DayRecordUi>> = combine(
        repository.recentRecordsFlow(),
        ticker
    ) { list, _ ->
        val today = DateUtils.todayKey()
        val yesterday = DateUtils.dateKeyDaysAgo(1)
        val dayBeforeYesterday = DateUtils.dateKeyDaysAgo(2)

        list
            .filter { it.date != today } // joriy kun tugamaguncha statistikada ko'rinmaydi
            .take(31)
            .map { r ->
                DayRecordUi(
                    date = r.date,
                    displayDate = DateUtils.displayDate(r.date),
                    totalMl = r.totalMl,
                    goalMl = r.goalMl,
                    status = ColorUtils.statusForCompletedDay(r.totalMl, r.goalMl),
                    editable = r.date == yesterday || r.date == dayBeforeYesterday
                )
            }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun adjustDay(date: String, deltaMl: Int) {
        viewModelScope.launch { repository.adjustDay(date, deltaMl) }
    }

    companion object {
        fun factory(repository: WaterRepository) = viewModelFactory {
            initializer { StatsViewModel(repository) }
        }
    }
}
