package io.github.kosumorin.journal.utils.datetime

import android.text.format.DateFormat
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

object DateTimeHelper {
    @JvmStatic
    fun format(dateTime: LocalDateTime): String {
        val dateTimeFormatString = DateFormat.getBestDateTimePattern(Locale.getDefault(), "dd MMM yyyy, HH:mm")
        val dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormatString)

        return dateTime.format(dateTimeFormatter)
    }

    @JvmStatic
    fun formatDate(dateTime: LocalDateTime): String {
        val dateTimeFormatString = DateFormat.getBestDateTimePattern(Locale.getDefault(), "dd MMM yyyy")
        val dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormatString)

        return dateTime.format(dateTimeFormatter)
    }

    @JvmStatic
    fun formatTime(dateTime: LocalDateTime): String {
        val dateTimeFormatString = DateFormat.getBestDateTimePattern(Locale.getDefault(), "HH:mm")
        val dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormatString)

        return dateTime.format(dateTimeFormatter)
    }
}
