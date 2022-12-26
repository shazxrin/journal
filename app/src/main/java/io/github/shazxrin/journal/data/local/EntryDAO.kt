package io.github.shazxrin.journal.data.local

import androidx.room.*
import io.github.shazxrin.journal.data.entity.Entry
import kotlinx.coroutines.flow.Flow

@Dao
interface EntryDAO {
    @Query("SELECT * FROM entry WHERE DATE(dateTime) = DATE(:isoDateFormat) ORDER BY DATETIME(dateTime) DESC")
    fun getEntriesSorted(isoDateFormat: String): Flow<List<Entry>>

    @Query("SELECT * FROM entry WHERE id = :id")
    suspend fun get(id: Int): Entry

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: Entry)

    @Update
    suspend fun update(entry: Entry)

    @Query("DELETE FROM entry WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM entry")
    suspend fun getAllEntries(): List<Entry>
}