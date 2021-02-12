package io.github.kosumorin.journal.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.kosumorin.journal.data.entity.Entry
import io.github.kosumorin.journal.data.entity.EntryTag
import io.github.kosumorin.journal.data.entity.Tag

@Database(entities = [
    Entry::class,
    Tag::class,
    EntryTag::class
], exportSchema = false, version = 1)
@TypeConverters(LocalDBTypeConverters::class)
abstract class LocalDatabase: RoomDatabase() {
    abstract fun entryDAO(): EntryDAO

    abstract fun tagDAO(): TagDAO

    abstract fun entryTagDAO(): EntryTagDAO
}