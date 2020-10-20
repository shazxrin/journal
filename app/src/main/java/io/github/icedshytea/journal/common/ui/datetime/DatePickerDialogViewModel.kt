package io.github.icedshytea.journal.common.ui.datetime

import androidx.lifecycle.ViewModel
import io.github.icedshytea.journal.common.data.ConsumableLiveData
import org.threeten.bp.LocalDate
import javax.inject.Inject

class DatePickerDialogViewModel @Inject constructor() : ViewModel() {
    // Settings.
    var showSelectedDate: LocalDate = LocalDate.now()

    // Live Data.
    val userSelectedDate = ConsumableLiveData<LocalDate>()
}