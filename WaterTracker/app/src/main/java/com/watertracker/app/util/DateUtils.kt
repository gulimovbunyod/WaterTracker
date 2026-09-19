package com.watertracker.app.util

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object DateUtils {
    private val keyFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val uzMonths = listOf(
        "Yan", "Fev", "Mar", "Apr", "May", "Iyun",
        "Iyul", "Avg", "Sen", "Okt", "Noy", "Dek"
    )

    fun todayKey(): String = LocalDate.now().format(keyFormatter)

    /** "2026-09-18" -> "18-Sen" ko'rinishida ko'rsatish uchun. */
    fun displayDate(key: String): String {
        val date = LocalDate.parse(key, keyFormatter)
        return "${date.dayOfMonth}-${uzMonths[date.monthValue - 1]}"
    }

    /** Joriy vaqtni soat sifatida qaytaradi, masalan 14:30 -> 14.5 */
    fun currentHourFraction(): Double {
        val now = LocalTime.now()
        return now.hour + now.minute / 60.0 + now.second / 3600.0
    }
}
