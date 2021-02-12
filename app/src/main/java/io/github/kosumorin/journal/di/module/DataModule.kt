package io.github.kosumorin.journal.di.module

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import io.github.kosumorin.journal.data.local.LocalDatabase
import io.github.kosumorin.journal.data.repository.*
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

    @Singleton
    @Provides
    fun provideTagRepository(localDatabase: LocalDatabase): TagRepository {
        return LocalTagRepository(
            localDatabase
        )
    }

    @Singleton
    @Provides
    fun provideEntryTagRepository(localDatabase: LocalDatabase): EntryTagRepository {
        return LocalEntryTagRepository(
            localDatabase
        )
    }
}
