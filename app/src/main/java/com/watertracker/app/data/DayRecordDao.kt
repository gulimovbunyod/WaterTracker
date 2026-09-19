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

    // 32 ta oxirgi yozuv olinadi: ulardan biri "bugungi kun" bo'lishi mumkin,
    // uni UI qatlamida chiqarib tashlab, aniq 31 ta tugagan kunni ko'rsatamiz.
    @Query("SELECT * FROM day_records ORDER BY date DESC LIMIT 32")
    fun observeRecent(): Flow<List<DayRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(record: DayRecord)

    @Query(
        """
        DELETE FROM day_records
        WHERE date NOT IN (SELECT date FROM day_records ORDER BY date DESC LIMIT 32)
        """
    )
    suspend fun trimToLast32()
}
