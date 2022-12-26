package io.github.shazxrin.journal.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.shazxrin.journal.feature.MainActivity

@Module
abstract class ActivityBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributesMainActivity(): MainActivity
}