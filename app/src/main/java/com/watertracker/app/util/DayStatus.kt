package com.watertracker.app.util

enum class DayStatus { GREEN, RED }

object ColorUtils {

    // Kun davomida: soatlik kutilgan miqdorning shu foizigacha yetsa - yashil.
    private const val INTRADAY_TOLERANCE = 0.8

    // Kun yakunlangach: meyordan shuncha kam yoki ko'p bo'lsa ham - hali normal (yashil).
    private const val FINAL_LOWER_TOLERANCE_ML = 500
    private const val FINAL_UPPER_TOLERANCE_ML = 1000

    /**
     * BUGUNGI (hali tugamagan) kun uchun: meyor 24 soatga mutanosib taqsimlanadi.
     * Soatlik kutilgan miqdorning kamida 80%i ichilgan bo'lsa - yashil, aks holda - qizil.
     */
    fun statusForToday(totalMl: Int, goalMl: Int): DayStatus {
        if (goalMl <= 0) return DayStatus.GREEN
        val hourFraction = DateUtils.currentHourFraction()
        val expectedSoFar = goalMl * (hourFraction / 24.0)
        val threshold = expectedSoFar * INTRADAY_TOLERANCE
        return if (totalMl >= threshold) DayStatus.GREEN else DayStatus.RED
    }

    /**
     * TUGAGAN kun uchun: meyordan ko'pi bilan 500 ml kam yoki 1000 ml ko'p bo'lsa - normal (yashil).
     */
    fun statusForCompletedDay(totalMl: Int, goalMl: Int): DayStatus {
        val lowerBound = goalMl - FINAL_LOWER_TOLERANCE_ML
        val upperBound = goalMl + FINAL_UPPER_TOLERANCE_ML
        return if (totalMl in lowerBound..upperBound) DayStatus.GREEN else DayStatus.RED
    }
}
