package io.github.icedshytea.journal.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.icedshytea.journal.data.entity.Entry

@Database(entities = [Entry::class], exportSchema = false, version = 1)
@TypeConverters(LocalDBTypeConverters::class)
abstract class LocalDatabase: RoomDatabase() {
    abstract fun entryDAO(): EntryDAO
}