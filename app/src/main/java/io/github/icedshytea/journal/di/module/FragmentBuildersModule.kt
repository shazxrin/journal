package io.github.icedshytea.journal.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.icedshytea.journal.common.ui.alert.AlertBottomSheetDialogFragment
import io.github.icedshytea.journal.common.ui.datetime.DatePickerDialogFragment
import io.github.icedshytea.journal.common.ui.datetime.TimePickerDialogFragment
import io.github.icedshytea.journal.feature.editor.EditorFragment
import io.github.icedshytea.journal.feature.timeline.TimelineFragment

@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributesEditorFragment(): EditorFragment

    @ContributesAndroidInjector
    abstract fun contributesTimelineFragment(): TimelineFragment

    @ContributesAndroidInjector
    abstract fun contributesDatePickerDialogFragment(): DatePickerDialogFragment

    @ContributesAndroidInjector
    abstract fun contributesTimePickerDialogFragment(): TimePickerDialogFragment

    @ContributesAndroidInjector
    abstract fun contributesAlertBottomSheetDialogFragment(): AlertBottomSheetDialogFragment
}