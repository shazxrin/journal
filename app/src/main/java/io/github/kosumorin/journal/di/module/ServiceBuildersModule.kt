package io.github.kosumorin.journal.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.kosumorin.journal.settings.BackupService
import io.github.kosumorin.journal.settings.ImportService

@Module
abstract class ServiceBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributesBackupService(): BackupService

    @ContributesAndroidInjector
    abstract fun contributesImportService(): ImportService
}