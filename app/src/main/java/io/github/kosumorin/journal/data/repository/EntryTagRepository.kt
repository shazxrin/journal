package io.github.kosumorin.journal.data.repository

import io.github.kosumorin.journal.data.entity.EntryTag
import io.github.kosumorin.journal.data.local.LocalDatabase
import javax.inject.Inject

interface EntryTagRepository {
    suspend fun insert(entryTag: EntryTag)
}

class LocalEntryTagRepository @Inject constructor (private val localDatabase: LocalDatabase)
    : EntryTagRepository {

    override suspend fun insert(entryTag: EntryTag) = localDatabase.entryTagDAO().insert(entryTag)
}
