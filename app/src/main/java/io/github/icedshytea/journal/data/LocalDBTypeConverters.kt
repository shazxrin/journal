package io.github.icedshytea.journal.data

import androidx.room.TypeConverter
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

class LocalDBTypeConverters {
    @TypeConverter
    fun dateTimeToISOFormat(dateTime: LocalDateTime): String {
        return dateTime.format(DateTimeFormatter.ISO_DATE_TIME)
    }

    @TypeConverter
    fun isoFormatToDateTime(isoFormat: String): LocalDateTime {
        return LocalDateTime.parse(isoFormat, DateTimeFormatter.ISO_DATE_TIME)
    }
}