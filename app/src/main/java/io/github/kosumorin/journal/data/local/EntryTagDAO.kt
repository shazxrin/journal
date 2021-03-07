package io.github.kosumorin.journal.data.local

import androidx.room.*
import io.github.kosumorin.journal.data.entity.EntryTag

@Dao
interface EntryTagDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entryTag: EntryTag)

    @Query("DELETE FROM entrytag WHERE entryId = :entryId")
    suspend fun deleteEntryTags(entryId: String)
}