package io.github.icedshytea.journal.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.icedshytea.journal.feature.settings.BackupService

@Module
abstract class ServiceBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributesBackupService(): BackupService
}