package io.github.kosumorin.journal

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.kosumorin.journal.data.entity.Entry
import io.github.kosumorin.journal.data.entity.EntryWithMetadata
import io.github.kosumorin.journal.data.entity.Tag
import io.github.kosumorin.journal.data.local.LocalDatabase
import io.github.kosumorin.journal.data.repository.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.threeten.bp.LocalDateTime
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class EntryTagLocalRepositoryTest {
    private lateinit var db: LocalDatabase
    private lateinit var tagRepository: TagRepository
    private lateinit var entryRepository: EntryRepository
    private lateinit var entryTagRepository: EntryTagRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, LocalDatabase::class.java).build()

        tagRepository = LocalTagRepository(db)
        entryRepository = LocalEntryRepository(db)
        entryTagRepository = LocalEntryTagRepository(db)
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }

    @Test
    fun testGetEntryWithMetadata() = runBlocking {
        val entry = Entry("title", "content", LocalDateTime.now())
        val tag1 = Tag("name1", "#000000")
        val tag2 = Tag("name2", "#000001")

        val insertEntryWithMetadata = EntryWithMetadata(entry, listOf(tag1, tag2))

        entryRepository.insertWithMetadata(insertEntryWithMetadata)

        val gotEntryWithMetadata = entryRepository.getWithMetadata(entry.entryId)

        assert(gotEntryWithMetadata.entry.entryId == entry.entryId)
        assert(gotEntryWithMetadata.tags.size == 2)
        assert(gotEntryWithMetadata.tags.contains(tag1))
        assert(gotEntryWithMetadata.tags.contains(tag2))
    }

    @Test
    fun testUpdateEntryWithMetadata() = runBlocking {
        val entry = Entry("title", "content", LocalDateTime.now())
        val tag1 = Tag("name1", "#000000")
        val tag2 = Tag("name2", "#000001")

        val insertEntryWithMetadata = EntryWithMetadata(entry, listOf(tag1, tag2))

        entryRepository.insertWithMetadata(insertEntryWithMetadata)

        val tag3 = Tag("name3", "#000001")
        val updateEntryWithMetadata = insertEntryWithMetadata.copy(tags = listOf(tag3))

        entryRepository.updateWithMetadata(updateEntryWithMetadata)

        val gotEntryWithMetadata = entryRepository.getWithMetadata(entry.entryId)

        assert(gotEntryWithMetadata.entry.entryId == entry.entryId)
        assert(gotEntryWithMetadata.tags.size == 1)
        assert(!gotEntryWithMetadata.tags.contains(tag1))
        assert(!gotEntryWithMetadata.tags.contains(tag2))
        assert(gotEntryWithMetadata.tags.contains(tag3))
    }
}