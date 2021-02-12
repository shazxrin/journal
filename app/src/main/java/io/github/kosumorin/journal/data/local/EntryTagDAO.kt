package io.github.kosumorin.journal.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import io.github.kosumorin.journal.data.entity.EntryTag

@Dao
interface EntryTagDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entryTag: EntryTag)
}