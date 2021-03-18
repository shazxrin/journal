package io.github.kosumorin.journal.data.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Tag(
    @PrimaryKey val tagId: String,
    val name: String,
    val color: String
) {
  @Ignore
  constructor(
      name: String,
      color: String
  ) : this(UUID.randomUUID().toString(), name, color)
}
