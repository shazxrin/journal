package io.github.icedshytea.journal.di.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import io.github.icedshytea.journal.App
import io.github.icedshytea.journal.di.module.*
import javax.inject.Singleton

@Singleton
@Component(modules = [
    DataModule::class,
    ViewModelModule::class,
    ViewModelFactoryModule::class,
    ActivityBuildersModule::class,
    FragmentBuildersModule::class,
    AndroidInjectionModule::class
])
interface AppComponent : AndroidInjector<App> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}