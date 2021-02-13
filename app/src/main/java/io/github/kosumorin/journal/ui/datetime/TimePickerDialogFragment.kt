package io.github.kosumorin.journal.ui.datetime

import android.app.TimePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.TimePicker
import org.threeten.bp.LocalTime

class TimePickerDialogFragment() : PickerDialogFragment(), TimePickerDialog.OnTimeSetListener {
    private lateinit var timePickerDialogViewModel: TimePickerDialogViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        timePickerDialogViewModel = getSharedViewModel()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val showSelectedTime = timePickerDialogViewModel.showSelectedTime

        return TimePickerDialog(
            requireContext(),
            this,
            showSelectedTime.hour,
            showSelectedTime.minute,
            true
        )
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        timePickerDialogViewModel.userSelectedTimeLiveData.postValue(LocalTime.of(hourOfDay, minute))
    }
}
