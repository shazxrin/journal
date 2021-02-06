package io.github.kosumorin.journal.data.repository

import io.github.kosumorin.journal.data.entity.Entry
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDate

interface EntryRepository {
    fun getEntriesSorted(date: LocalDate): Flow<List<Entry>>
    suspend fun insert(entry: Entry)
    suspend fun delete(entryId: Int)
    suspend fun update(entry: Entry)
    suspend fun get(entryId: Int): Entry
    suspend fun getAllEntries(): List<Entry>
}