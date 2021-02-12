package io.github.kosumorin.journal.data.entity

import androidx.room.Entity

@Entity(primaryKeys = ["entryId", "tagId"])
data class EntryTag(
   val entryId: String,
   val tagId: String
)
