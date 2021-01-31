package io.github.icedshytea.journal.utils.ui.datetime

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import org.threeten.bp.*

class DatePickerDialogFragment() : PickerDialogFragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var datePickerDialogViewModel: DatePickerDialogViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        datePickerDialogViewModel = getSharedViewModel()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val showSelectedDate = datePickerDialogViewModel.showSelectedDate

        // Java's Calendar API's months start from 0 so we need to +- accordingly with 310's months.
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            this,
            showSelectedDate.year,
            showSelectedDate.monthValue - 1,
            showSelectedDate.dayOfMonth
        )
        datePickerDialog.datePicker.maxDate = ZonedDateTime.now(ZoneId.systemDefault())
            .toEpochSecond() * 1000L

        return datePickerDialog
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        // Java's Calendar API's months start from 0 so we need to +- accordingly with 310's months.
        datePickerDialogViewModel.userSelectedDateLiveData.postValue(LocalDate.of(year, month + 1, dayOfMonth))
    }
}
