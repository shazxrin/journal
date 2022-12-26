package io.github.shazxrin.journal.data.json

import io.github.shazxrin.journal.BuildConfig
import io.github.shazxrin.journal.data.entity.Entry

data class BackupJSON(
    val version: String = BuildConfig.VERSION_NAME,
    var data: List<Entry>
)
