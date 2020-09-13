package io.github.icedshytea.journal.data.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDateTime

@Entity
data class Entry(
    @PrimaryKey(autoGenerate = true) val id: Int,
    var title: String,
    var content: String,
    val dateTime: LocalDateTime
) {
    @Ignore
    constructor(
        title: String,
        content: String,
        dateTime: LocalDateTime
    ) : this(
        0,
        title,
        content,
        dateTime
    )
}
