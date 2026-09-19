package com.watertracker.app.util

enum class DayStatus { GREEN, RED }

object ColorUtils {

    /**
     * BUGUNGI (hali tugamagan) kun uchun: meyor 24 soatga mutanosib taqsimlanadi.
     * Masalan ertalab soat 6 da (kunning 1/4 qismi) meyorning kamida 1/4 qismini
     * ichgan bo'lsa - bu yashil (jadal ketyapti), aks holda qizil (orqada qolyapti).
     */
    fun statusForToday(totalMl: Int, goalMl: Int): DayStatus {
        if (goalMl <= 0) return DayStatus.GREEN
        val hourFraction = DateUtils.currentHourFraction()
        val expected = goalMl * (hourFraction / 24.0)
        return if (totalMl >= expected) DayStatus.GREEN else DayStatus.RED
    }

    /**
     * TUGAGAN kun uchun: yakuniy natija meyordan ko'pi bilan ±1 litr (±1000 ml)
     * farq qilsa - normal (yashil), aks holda - qizil.
     */
    fun statusForCompletedDay(totalMl: Int, goalMl: Int): DayStatus {
        val diff = kotlin.math.abs(totalMl - goalMl)
        return if (diff <= 1000) DayStatus.GREEN else DayStatus.RED
    }
}
