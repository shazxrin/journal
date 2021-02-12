package io.github.kosumorin.journal.data.repository

import io.github.kosumorin.journal.data.entity.Tag
import io.github.kosumorin.journal.data.local.LocalDatabase
import javax.inject.Inject
import javax.inject.Singleton

interface TagRepository {
    suspend fun insert(tag: Tag)
    suspend fun delete(tagId: String)
    suspend fun update(tag: Tag)
    suspend fun get(tagId: String): Tag
    suspend fun getAll(): List<Tag>
}

@Singleton
class LocalTagRepository @Inject constructor (private val localDatabase: LocalDatabase)
    : TagRepository {
    override suspend fun insert(tag: Tag) = localDatabase.tagDAO().insert(tag)

    override suspend fun delete(tagId: String) = localDatabase.tagDAO().delete(tagId)

    override suspend fun update(tag: Tag) = localDatabase.tagDAO().update(tag)

    override suspend fun get(tagId: String): Tag = localDatabase.tagDAO().get(tagId)

    override suspend fun getAll(): List<Tag> = localDatabase.tagDAO().getAll()
}
