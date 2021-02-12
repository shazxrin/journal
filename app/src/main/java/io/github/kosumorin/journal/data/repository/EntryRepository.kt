package io.github.kosumorin.journal.data.repository

import io.github.kosumorin.journal.data.entity.Entry
import io.github.kosumorin.journal.data.entity.EntryWithMetadata
import io.github.kosumorin.journal.data.local.LocalDatabase
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

interface EntryRepository {
    fun getEntriesSorted(date: LocalDate): Flow<List<Entry>>
    suspend fun insert(entry: Entry)
    suspend fun delete(entryId: String)
    suspend fun update(entry: Entry)
    suspend fun get(entryId: String): Entry
    suspend fun getAllEntries(): List<Entry>
    suspend fun getWithMetadata(entryId: String): EntryWithMetadata
}

@Singleton
class LocalEntryRepository @Inject constructor (private val localDatabase: LocalDatabase)
    : EntryRepository {

    override fun getEntriesSorted(date: LocalDate): Flow<List<Entry>>
            = localDatabase.entryDAO().getEntriesSorted(date.format(DateTimeFormatter.ISO_DATE))

    override suspend fun insert(entry: Entry) = localDatabase.entryDAO().insert(entry)

    override suspend fun delete(entryId: String) = localDatabase.entryDAO().delete(entryId)

    override suspend fun update(entry: Entry) = localDatabase.entryDAO().update(entry)

    override suspend fun get(entryId: String): Entry = localDatabase.entryDAO().get(entryId)

    override suspend fun getAllEntries(): List<Entry> = localDatabase.entryDAO().getAllEntries()

    override suspend fun getWithMetadata(entryId: String): EntryWithMetadata
        = localDatabase.entryDAO().getWithMetadata(entryId)
}
