package io.github.icedshytea.journal.common.ui.datetime

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import io.github.icedshytea.journal.common.BaseDialogFragment
import org.threeten.bp.*

class DatePickerDialogFragment() : BaseDialogFragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var datePickerDialogViewModel: DatePickerDialogViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        datePickerDialogViewModel = initSharedViewModel()
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
        datePickerDialogViewModel.userSelectedDate.postValue(LocalDate.of(year, month + 1, dayOfMonth))
    }
}