package io.github.kosumorin.journal.di.module

import android.app.Application
import dagger.Module
import dagger.Provides
import io.noties.markwon.Markwon
import javax.inject.Singleton

@Module
class MarkwonModule {
    @Singleton
    @Provides
    fun provideMarkwon(application: Application): Markwon {
        return Markwon.create(application)
    }
}