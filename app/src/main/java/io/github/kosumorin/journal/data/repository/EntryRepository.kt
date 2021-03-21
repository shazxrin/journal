package io.github.kosumorin.journal.data.repository

import io.github.kosumorin.journal.data.entity.Entry
import io.github.kosumorin.journal.data.entity.EntryTag
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
    suspend fun insertWithMetadata(entryWithMetadata: EntryWithMetadata)
    suspend fun updateWithMetadata(entryWithMetadata: EntryWithMetadata)
}

@Singleton
class LocalEntryRepository @Inject constructor (private val localDatabase: LocalDatabase)
    : EntryRepository {

    override fun getEntriesSorted(date: LocalDate): Flow<List<Entry>>
            = localDatabase.entryDAO().getEntriesSorted(date.format(DateTimeFormatter.ISO_DATE))

    override suspend fun insert(entry: Entry) = localDatabase.entryDAO().insert(entry)

    override suspend fun delete(entryId: String) {
        localDatabase.entryDAO().delete(entryId)

        localDatabase.entryTagDAO().deleteEntryTagsByEntryId(entryId)
    }

    override suspend fun update(entry: Entry) = localDatabase.entryDAO().update(entry)

    override suspend fun get(entryId: String): Entry = localDatabase.entryDAO().get(entryId)

    override suspend fun getAllEntries(): List<Entry> = localDatabase.entryDAO().getAllEntries()

    override suspend fun getWithMetadata(entryId: String): EntryWithMetadata
        = localDatabase.entryDAO().getWithMetadata(entryId)

    override suspend fun insertWithMetadata(entryWithMetadata: EntryWithMetadata) {
        localDatabase.entryDAO().insert(entryWithMetadata.entry)

        for (tag in entryWithMetadata.tags) {
            localDatabase.tagDAO().insert(tag)

            localDatabase.entryTagDAO().insert(
                EntryTag(
                    entryId = entryWithMetadata.entry.entryId,
                    tagId = tag.tagId
                )
            )
        }
    }

    override suspend fun updateWithMetadata(entryWithMetadata: EntryWithMetadata) {
        localDatabase.entryDAO().update(entryWithMetadata.entry)

        // Update entry tags by deleting and inserting relationships.
        localDatabase.entryTagDAO().deleteEntryTagsByEntryId(entryWithMetadata.entry.entryId)
        for (tag in entryWithMetadata.tags) {
            // Insert will only insert new tags. Existing tags are ignored.
            localDatabase.tagDAO().insert(tag)

            localDatabase.entryTagDAO().insert(
                EntryTag(
                    entryId = entryWithMetadata.entry.entryId,
                    tagId = tag.tagId
                )
            )
        }
    }
}
