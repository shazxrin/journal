package io.github.icedshytea.journal.di.module

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import io.github.icedshytea.journal.data.repository.EntryRepository
import io.github.icedshytea.journal.data.repository.LocalEntryRepository
import io.github.icedshytea.journal.data.local.LocalDatabase
import javax.inject.Singleton

@Module
class DataModule {
    @Provides
    fun provideLocalDatabase(application: Application): LocalDatabase {
        return Room.databaseBuilder(application, LocalDatabase::class.java, "thoughts.db").build()
    }

    @Singleton
    @Provides
    fun provideEntryRepository(localDatabase: LocalDatabase): EntryRepository {
        return LocalEntryRepository(
            localDatabase
        )
    }
}
