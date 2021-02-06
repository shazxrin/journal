package io.github.kosumorin.journal.data.json

import io.github.kosumorin.journal.BuildConfig
import io.github.kosumorin.journal.data.entity.Entry

data class BackupJSON(
    val version: String = BuildConfig.VERSION_NAME,
    var data: List<Entry>
)
