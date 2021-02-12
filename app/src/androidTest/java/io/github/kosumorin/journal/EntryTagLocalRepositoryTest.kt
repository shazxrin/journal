package io.github.kosumorin.journal

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.kosumorin.journal.data.entity.Entry
import io.github.kosumorin.journal.data.entity.EntryTag
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
        val tag1 = Tag("name1", "#000000", "❤")
        val tag2 = Tag("name2", "#000001", "💔")

        entryRepository.insertWithMetadata(entry, listOf(tag1, tag2))

        val entryWithMetadata = entryRepository.getWithMetadata(entry.entryId)

        assert(entryWithMetadata.entry.entryId == entry.entryId)
        assert(entryWithMetadata.tags.size == 2)
        assert(entryWithMetadata.tags.contains(tag1))
        assert(entryWithMetadata.tags.contains(tag2))
    }
}