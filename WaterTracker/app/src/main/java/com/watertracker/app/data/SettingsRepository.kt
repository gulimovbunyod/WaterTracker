package com.watertracker.app.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

/** Foydalanuvchi belgilagan kunlik suv meyorini saqlaydi (DataStore, lokal). */
class SettingsRepository(private val context: Context) {

    companion object {
        val GOAL_KEY = intPreferencesKey("goal_ml")
        const val DEFAULT_GOAL_ML = 2000
    }

    val goalFlow: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[GOAL_KEY] ?: DEFAULT_GOAL_ML
    }

    suspend fun setGoal(goalMl: Int) {
        context.dataStore.edit { prefs -> prefs[GOAL_KEY] = goalMl }
    }
}
