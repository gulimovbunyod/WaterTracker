package com.watertracker.app.data

import com.watertracker.app.util.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class WaterRepository(
    private val dao: DayRecordDao,
    private val settings: SettingsRepository
) {
    val goalFlow: Flow<Int> = settings.goalFlow

    fun todayRecordFlow(): Flow<DayRecord?> = dao.observeByDate(DateUtils.todayKey())

    fun recentRecordsFlow(): Flow<List<DayRecord>> = dao.observeRecent()

    suspend fun addWater(deltaMl: Int) {
        val today = DateUtils.todayKey()
        val goal = settings.goalFlow.first()
        val current = dao.observeByDate(today).first()
        val newTotal = ((current?.totalMl ?: 0) + deltaMl).coerceAtLeast(0)
        dao.upsert(DayRecord(date = today, totalMl = newTotal, goalMl = goal))
        dao.trimToLast32()
    }

    suspend fun setGoal(goalMl: Int) {
        settings.setGoal(goalMl)
        val today = DateUtils.todayKey()
        val current = dao.observeByDate(today).first()
        dao.upsert(DayRecord(date = today, totalMl = current?.totalMl ?: 0, goalMl = goalMl))
    }

    /** Statistikada faqat "kecha" va "kechadan oldingi kun" uchun tahrirlash imkoniyati. */
    suspend fun adjustDay(date: String, deltaMl: Int) {
        val current = dao.observeByDate(date).first()
        val goal = current?.goalMl ?: settings.goalFlow.first()
        val newTotal = ((current?.totalMl ?: 0) + deltaMl).coerceAtLeast(0)
        dao.upsert(DayRecord(date = date, totalMl = newTotal, goalMl = goal))
    }
}
