package io.github.shazxrin.journal.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.shazxrin.journal.settings.BackupService
import io.github.shazxrin.journal.settings.ImportService

@Module
abstract class ServiceBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributesBackupService(): BackupService

    @ContributesAndroidInjector
    abstract fun contributesImportService(): ImportService
}