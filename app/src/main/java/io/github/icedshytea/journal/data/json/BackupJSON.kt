package io.github.icedshytea.journal.data.json

import io.github.icedshytea.journal.BuildConfig
import io.github.icedshytea.journal.data.entity.Entry

data class BackupJSON(
    val version: String = BuildConfig.VERSION_NAME,
    var data: List<Entry>
)
