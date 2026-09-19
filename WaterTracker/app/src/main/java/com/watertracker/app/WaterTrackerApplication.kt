package com.watertracker.app

import android.app.Application
import com.watertracker.app.data.AppDatabase
import com.watertracker.app.data.SettingsRepository
import com.watertracker.app.data.WaterRepository

class WaterTrackerApplication : Application() {

    private val database by lazy { AppDatabase.getInstance(this) }
    private val settingsRepository by lazy { SettingsRepository(this) }
    val repository by lazy { WaterRepository(database.dayRecordDao(), settingsRepository) }
}
