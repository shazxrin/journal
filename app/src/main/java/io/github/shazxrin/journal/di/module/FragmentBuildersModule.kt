package io.github.shazxrin.journal.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.shazxrin.journal.utils.ui.alert.AlertBottomSheetDialogFragment
import io.github.shazxrin.journal.utils.ui.datetime.DatePickerDialogFragment
import io.github.shazxrin.journal.utils.ui.datetime.TimePickerDialogFragment
import io.github.shazxrin.journal.feature.editor.EditorFragment
import io.github.shazxrin.journal.feature.timeline.TimelineFragment

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