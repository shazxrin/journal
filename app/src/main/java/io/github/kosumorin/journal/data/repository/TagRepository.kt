package io.github.kosumorin.journal.data.repository

import io.github.kosumorin.journal.data.entity.Tag
import io.github.kosumorin.journal.data.local.LocalDatabase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface TagRepository {
    suspend fun insert(tag: Tag)
    suspend fun delete(tag: Tag)
    suspend fun update(tag: Tag)
    suspend fun getById(tagId: String): Tag
    fun getAll(): Flow<List<Tag>>
}

@Singleton
class LocalTagRepository @Inject constructor (private val localDatabase: LocalDatabase)
    : TagRepository {
    override suspend fun insert(tag: Tag) = localDatabase.tagDAO().insert(tag)

    override suspend fun delete(tag: Tag) {
        localDatabase.tagDAO().delete(tag.tagId)

        localDatabase.entryTagDAO().deleteEntryTagsByTagId(tag.tagId)
    }

    override suspend fun update(tag: Tag) = localDatabase.tagDAO().update(tag)

    override suspend fun getById(tagId: String): Tag = localDatabase.tagDAO().get(tagId)

    override fun getAll(): Flow<List<Tag>> = localDatabase.tagDAO().getAll()
}
