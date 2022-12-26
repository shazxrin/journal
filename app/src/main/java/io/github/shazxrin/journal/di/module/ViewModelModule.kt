package io.github.shazxrin.journal.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import io.github.shazxrin.journal.utils.ui.alert.AlertBottomSheetDialogViewModel
import io.github.shazxrin.journal.utils.ui.datetime.DatePickerDialogViewModel
import io.github.shazxrin.journal.utils.ui.datetime.TimePickerDialogViewModel
import io.github.shazxrin.journal.feature.editor.EditorViewModel
import io.github.shazxrin.journal.feature.timeline.TimelineViewModel
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Singleton
class ViewModelFactory @Inject constructor(
    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = creators[modelClass] ?: creators.entries.firstOrNull {
            modelClass.isAssignableFrom(it.key)
        }?.value ?: throw IllegalArgumentException("unknown model class $modelClass")

        try {
            @Suppress("UNCHECKED_CAST")
            return creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(EditorViewModel::class)
    abstract fun bindEditorViewModel(editorViewModel: EditorViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TimelineViewModel::class)
    abstract fun bindTimelineViewModel(timelineViewModel: TimelineViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DatePickerDialogViewModel::class)
    abstract fun bindDatePickerDialogViewModel(datePickerDialogViewModel: DatePickerDialogViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TimePickerDialogViewModel::class)
    abstract fun bindTimePickerDialogViewModel(timePickerDialogViewModel: TimePickerDialogViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AlertBottomSheetDialogViewModel::class)
    abstract fun bindAlertBottomSheetDialogViewModel(alertBottomSheetDialogViewModel: AlertBottomSheetDialogViewModel): ViewModel
}

@Module
class ViewModelFactoryModule {
    @Provides
    fun providesViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory = viewModelFactory
}