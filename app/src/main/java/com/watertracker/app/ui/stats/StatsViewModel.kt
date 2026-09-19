package com.watertracker.app.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.watertracker.app.data.WaterRepository
import com.watertracker.app.util.ColorUtils
import com.watertracker.app.util.DateUtils
import com.watertracker.app.util.DayStatus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class DayRecordUi(
    val date: String,
    val displayDate: String,
    val totalMl: Int,
    val goalMl: Int,
    val status: DayStatus
)

class StatsViewModel(repository: WaterRepository) : ViewModel() {

    val records: StateFlow<List<DayRecordUi>> = repository.recentRecordsFlow()
        .map { list ->
            val today = DateUtils.todayKey()
            list.map { r ->
                val status = if (r.date == today) {
                    ColorUtils.statusForToday(r.totalMl, r.goalMl)
                } else {
                    ColorUtils.statusForCompletedDay(r.totalMl, r.goalMl)
                }
                DayRecordUi(
                    date = r.date,
                    displayDate = DateUtils.displayDate(r.date),
                    totalMl = r.totalMl,
                    goalMl = r.goalMl,
                    status = status
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    companion object {
        fun factory(repository: WaterRepository) = viewModelFactory {
            initializer { StatsViewModel(repository) }
        }
    }
}
