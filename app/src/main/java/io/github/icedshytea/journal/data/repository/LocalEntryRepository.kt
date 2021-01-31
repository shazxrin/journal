package io.github.icedshytea.journal.data.repository

import io.github.icedshytea.journal.data.entity.Entry
import io.github.icedshytea.journal.data.local.LocalDatabase
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalEntryRepository @Inject constructor (private val localDatabase: LocalDatabase)
    : EntryRepository {

    override fun getEntriesSorted(date: LocalDate): Flow<List<Entry>>
            = localDatabase.entryDAO().getEntriesSorted(date.format(DateTimeFormatter.ISO_DATE))

    override suspend fun insert(entry: Entry) = localDatabase.entryDAO().insert(entry)

    override suspend fun delete(entryId: Int) = localDatabase.entryDAO().delete(entryId)

    override suspend fun update(entry: Entry) = localDatabase.entryDAO().update(entry)

    override suspend fun get(entryId: Int): Entry = localDatabase.entryDAO().get(entryId)

    override suspend fun getAllEntries(): List<Entry> = localDatabase.entryDAO().getAllEntries()
}