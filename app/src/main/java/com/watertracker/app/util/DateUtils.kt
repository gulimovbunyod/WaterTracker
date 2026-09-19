package com.watertracker.app.util

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object DateUtils {
    private val keyFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun todayKey(): String = LocalDate.now().format(keyFormatter)

    /** Bugundan necha kun oldingi sanani "yyyy-MM-dd" ko'rinishida qaytaradi. */
    fun dateKeyDaysAgo(days: Long): String = LocalDate.now().minusDays(days).format(keyFormatter)

    /**
     * Statistikada ko'rsatish uchun: kecha bo'lsa "Kecha", aks holda "13/01/2026" ko'rinishida.
     */
    fun displayDate(key: String): String {
        if (key == dateKeyDaysAgo(1)) return "Kecha"
        val date = LocalDate.parse(key, keyFormatter)
        return "%02d/%02d/%04d".format(date.dayOfMonth, date.monthValue, date.year)
    }

    /** Joriy vaqtni soat sifatida qaytaradi, masalan 14:30 -> 14.5 */
    fun currentHourFraction(): Double {
        val now = LocalTime.now()
        return now.hour + now.minute / 60.0 + now.second / 3600.0
    }
}
