package io.github.icedshytea.journal.common.ui.datetime

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import org.threeten.bp.*

class DatePickerDialogFragment(
    private val onDateSetListener: DatePickerDialog.OnDateSetListener?,
    private val selectedDate: LocalDate
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Java's Calendar API's months start from 0 so we need to +- accordingly with 310's months.
        val datePickerDialog = DatePickerDialog(context, onDateSetListener, selectedDate.year, selectedDate.monthValue, selectedDate.dayOfMonth)
        datePickerDialog.datePicker.maxDate = ZonedDateTime.now(ZoneId.systemDefault())
            .toEpochSecond() * 1000L
        datePickerDialog.datePicker

        return datePickerDialog
    }
}