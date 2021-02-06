package io.github.kosumorin.journal.utils.ui.datetime

import androidx.lifecycle.ViewModel
import io.github.kosumorin.journal.utils.data.ConsumableLiveData
import org.threeten.bp.LocalDate
import javax.inject.Inject

class DatePickerDialogViewModel @Inject constructor() : ViewModel() {
    // Settings.
    var showSelectedDate: LocalDate = LocalDate.now()

    // Live Data.
    val userSelectedDateLiveData = ConsumableLiveData<LocalDate>()
}