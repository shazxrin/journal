package io.github.icedshytea.journal.utils.datetime

import android.app.TimePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import org.threeten.bp.LocalTime

class TimePickerDialogFragment(
    private val onTimeSetListener: TimePickerDialog.OnTimeSetListener,
    private val selectedTime: LocalTime
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return TimePickerDialog(
            context,
            onTimeSetListener,
            selectedTime.hour,
            selectedTime.minute,
            true
        )
    }
}