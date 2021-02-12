package io.github.kosumorin.journal.data.local

import androidx.room.*
import io.github.kosumorin.journal.data.entity.Entry
import io.github.kosumorin.journal.data.entity.EntryWithMetadata
import kotlinx.coroutines.flow.Flow

@Dao
interface EntryDAO {
    @Query("SELECT * FROM entry WHERE DATE(dateTime) = DATE(:isoDateFormat) ORDER BY DATETIME(dateTime) DESC")
    fun getEntriesSorted(isoDateFormat: String): Flow<List<Entry>>

    @Query("SELECT * FROM entry WHERE entryId = :id")
    suspend fun get(id: String): Entry

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: Entry)

    @Update
    suspend fun update(entry: Entry)

    @Query("DELETE FROM entry WHERE entryId = :id")
    suspend fun delete(id: String)

    @Query("SELECT * FROM entry")
    suspend fun getAllEntries(): List<Entry>

    @Transaction
    @Query("SELECT * FROM entry WHERE entryId = :id")
    suspend fun getWithMetadata(id: String): EntryWithMetadata
}