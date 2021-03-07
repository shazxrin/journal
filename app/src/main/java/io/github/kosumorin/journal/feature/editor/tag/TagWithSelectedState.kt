package io.github.kosumorin.journal.feature.editor.tag

import io.github.kosumorin.journal.data.entity.Tag

data class TagWithSelectedState(val tag: Tag, val isSelected: Boolean)
