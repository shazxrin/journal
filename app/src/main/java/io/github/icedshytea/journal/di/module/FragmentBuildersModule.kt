package io.github.icedshytea.journal.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.icedshytea.journal.feature.editor.EditorFragment
import io.github.icedshytea.journal.feature.timeline.TimelineFragment

@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributesEditorFragment(): EditorFragment

    @ContributesAndroidInjector
    abstract fun contributesTimelineFragment(): TimelineFragment
}