package io.github.kosumorin.journal.data.json

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import io.github.kosumorin.journal.data.entity.Entry
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

class EntryJSONAdapter {
    @ToJson
    public fun entryToJSON(entry: Entry): EntryJSON {
        return EntryJSON(
            entry.entryId,
            entry.title,
            entry.content,
            entry.dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )
    }

    @FromJson
    public fun entryFromJSON(entryJSON: EntryJSON): Entry {
        return Entry(
            entryJSON.entryId,
            entryJSON.title,
            entryJSON.content,
            LocalDateTime.parse(entryJSON.dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )
    }
}
