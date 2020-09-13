package io.github.icedshytea.journal.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.icedshytea.journal.feature.MainActivity

@Module
abstract class ActivityBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributesMainActivity(): MainActivity
}