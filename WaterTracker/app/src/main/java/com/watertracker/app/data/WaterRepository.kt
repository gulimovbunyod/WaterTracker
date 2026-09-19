package com.watertracker.app.data

import com.watertracker.app.util.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

/** Room (kunlik yozuvlar) va DataStore (meyor) ni birlashtiruvchi repository. */
class WaterRepository(
    private val dao: DayRecordDao,
    private val settings: SettingsRepository
) {
    val goalFlow: Flow<Int> = settings.goalFlow

    fun todayRecordFlow(): Flow<DayRecord?> = dao.observeByDate(DateUtils.todayKey())

    fun recentRecordsFlow(): Flow<List<DayRecord>> = dao.observeRecent()

    /** Bugungi kunga [deltaMl] qo'shadi (manfiy son bo'lsa - ayiradi). 0 dan pastga tushmaydi. */
    suspend fun addWater(deltaMl: Int) {
        val today = DateUtils.todayKey()
        val goal = settings.goalFlow.first()
        val current = dao.observeByDate(today).first()
        val newTotal = ((current?.totalMl ?: 0) + deltaMl).coerceAtLeast(0)
        dao.upsert(DayRecord(date = today, totalMl = newTotal, goalMl = goal))
        dao.trimToLast31()
    }

    /** Meyorni yangilaydi va bugungi kun yozuvidagi meyor snapshot'ini ham darhol yangilaydi. */
    suspend fun setGoal(goalMl: Int) {
        settings.setGoal(goalMl)
        val today = DateUtils.todayKey()
        val current = dao.observeByDate(today).first()
        dao.upsert(DayRecord(date = today, totalMl = current?.totalMl ?: 0, goalMl = goalMl))
    }
}
