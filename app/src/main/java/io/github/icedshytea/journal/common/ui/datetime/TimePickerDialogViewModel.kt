package io.github.icedshytea.journal.common.ui.datetime

import androidx.lifecycle.ViewModel
import io.github.icedshytea.journal.common.data.ConsumableLiveData
import org.threeten.bp.LocalTime
import javax.inject.Inject

class TimePickerDialogViewModel @Inject constructor() : ViewModel() {
    // Settings.
    var showSelectedTime: LocalTime = LocalTime.now()

    // Live Data.
    val userSelectedTime = ConsumableLiveData<LocalTime>()
}