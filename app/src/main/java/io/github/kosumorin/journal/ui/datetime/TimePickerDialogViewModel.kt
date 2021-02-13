package io.github.kosumorin.journal.ui.datetime

import androidx.lifecycle.ViewModel
import io.github.kosumorin.journal.utils.data.ConsumableLiveData
import org.threeten.bp.LocalTime
import javax.inject.Inject

class TimePickerDialogViewModel @Inject constructor() : ViewModel() {
    // Settings.
    var showSelectedTime: LocalTime = LocalTime.now()

    // Live Data.
    val userSelectedTimeLiveData = ConsumableLiveData<LocalTime>()
}