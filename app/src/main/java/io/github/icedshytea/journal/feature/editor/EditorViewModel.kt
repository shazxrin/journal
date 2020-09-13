package io.github.icedshytea.journal.feature.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.icedshytea.journal.common.data.ActionResult
import io.github.icedshytea.journal.common.data.LiveActionResult
import io.github.icedshytea.journal.common.data.LiveField
import io.github.icedshytea.journal.data.entity.Entry
import io.github.icedshytea.journal.data.repository.EntryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import java.lang.Exception
import javax.inject.Inject

class EditorViewModel @Inject constructor(private val entryRepository: EntryRepository) : ViewModel() {
    private var currentEntryId: Int? = null

    // Fields.
    val titleField = LiveField("")
    val contentField = LiveField("")
    val dateTimeField = LiveField(LocalDateTime.now())

    // Actions & results.
    val saveActionResult = LiveActionResult()
    fun save() {
        val entry = Entry(
            currentEntryId ?: 0,
            titleField.value ?: "",
            contentField.value ?: "",
            dateTimeField.value ?: LocalDateTime.now()
        )

        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (currentEntryId == null) {
                    entryRepository.insert(entry)
                }
                else {
                    entryRepository.update(entry)
                }

                saveActionResult.postValue(ActionResult.success())
            }
            catch (e: Exception) {
                saveActionResult.postValue(ActionResult.failure(e))
            }
        }
    }

    val loadActionResult = LiveActionResult()
    fun load(entryId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val entry = entryRepository.get(entryId)

                currentEntryId = entry.id
                titleField.postValue(entry.title)
                contentField.postValue(entry.content)
                dateTimeField.postValue(entry.dateTime)

                loadActionResult.postValue(ActionResult.success())
            }
            catch (e: Exception) {
                loadActionResult.postValue(ActionResult.failure(e))
            }
        }
    }

    val deleteActionResult = LiveActionResult()
    fun delete() {
        val entryToDeleteId = currentEntryId ?: return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                entryRepository.delete(entryToDeleteId)

                deleteActionResult.postValue(ActionResult.success())
            }
            catch (e: Exception) {
                deleteActionResult.postValue(ActionResult.failure(e))
            }
        }
    }
}