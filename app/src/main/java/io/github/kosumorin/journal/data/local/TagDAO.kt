package io.github.kosumorin.journal.data.local

import androidx.room.*
import io.github.kosumorin.journal.data.entity.Tag

@Dao
interface TagDAO {
    @Query("SELECT * FROM tag WHERE tagId = :id")
    suspend fun get(id: String): Tag

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tag: Tag)

    @Update
    suspend fun update(tag: Tag)

    @Query("DELETE FROM tag WHERE tagId  = :id")
    suspend fun delete(id: String)
}
