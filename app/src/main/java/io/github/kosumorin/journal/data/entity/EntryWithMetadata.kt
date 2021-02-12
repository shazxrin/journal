package io.github.kosumorin.journal.data.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class EntryWithMetadata(
    @Embedded val entry: Entry,
    @Relation(
        parentColumn = "entryId",
        entityColumn = "tagId",
        associateBy = Junction(EntryTag::class)
    ) val tags: List<Tag>
)