package com.watertracker.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DayRecordDao {

    @Query("SELECT * FROM day_records WHERE date = :date LIMIT 1")
    fun observeByDate(date: String): Flow<DayRecord?>

    @Query("SELECT * FROM day_records ORDER BY date DESC LIMIT 31")
    fun observeRecent(): Flow<List<DayRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(record: DayRecord)

    // Faqat oxirgi 31 kunni saqlab qolish, qolganini o'chirish
    @Query(
        """
        DELETE FROM day_records
        WHERE date NOT IN (SELECT date FROM day_records ORDER BY date DESC LIMIT 31)
        """
    )
    suspend fun trimToLast31()
}
