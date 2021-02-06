package io.github.kosumorin.journal.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.kosumorin.journal.utils.ui.alert.AlertBottomSheetDialogFragment
import io.github.kosumorin.journal.utils.ui.datetime.DatePickerDialogFragment
import io.github.kosumorin.journal.utils.ui.datetime.TimePickerDialogFragment
import io.github.kosumorin.journal.feature.editor.EditorFragment
import io.github.kosumorin.journal.feature.timeline.TimelineFragment

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