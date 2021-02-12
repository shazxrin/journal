package io.github.kosumorin.journal.data.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDateTime
import java.util.*

@Entity
data class Entry(
    @PrimaryKey val entryId: String,
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
        UUID.randomUUID().toString(),
        title,
        content,
        dateTime
    )
}
