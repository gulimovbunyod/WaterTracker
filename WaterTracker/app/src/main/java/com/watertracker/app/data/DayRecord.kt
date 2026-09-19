package com.watertracker.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Bitta kun uchun yozuv: sana, shu kunda ichilgan jami suv (ml)
 * va o'sha kunda amal qilgan meyor (ml) - tarixiy hisob to'g'ri bo'lishi uchun
 * meyor keyinchalik o'zgartirilsa ham, eski kunlar o'z vaqtidagi meyorni saqlaydi.
 */
@Entity(tableName = "day_records")
data class DayRecord(
    @PrimaryKey val date: String, // "yyyy-MM-dd"
    val totalMl: Int,
    val goalMl: Int
)
