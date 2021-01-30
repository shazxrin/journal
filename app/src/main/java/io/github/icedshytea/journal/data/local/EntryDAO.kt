package io.github.icedshytea.journal.data.local

import androidx.room.*
import io.github.icedshytea.journal.data.entity.Entry
import kotlinx.coroutines.flow.Flow

@Dao
interface EntryDAO {
    @Query("SELECT * FROM entry WHERE DATE(dateTime) = DATE(:isoDateFormat) ORDER BY DATETIME(dateTime) DESC")
    fun getEntriesSorted(isoDateFormat: String): Flow<List<Entry>>

    @Query("SELECT * FROM entry WHERE id = :id")
    suspend fun get(id: Int): Entry

    @Insert
    suspend fun insert(entry: Entry)

    @Update
    suspend fun update(entry: Entry)

    @Query("DELETE FROM entry WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM entry LIMIT :limit OFFSET :offset")
    suspend fun getAllEntries(limit: Int, offset: Int): List<Entry>

    @Query("SELECT COUNT(*) FROM entry")
    suspend fun getEntriesCount(): Int
}