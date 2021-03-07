package io.github.kosumorin.journal.feature.editor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.kosumorin.journal.utils.data.Result
import io.github.kosumorin.journal.utils.data.ConsumableLiveData
import io.github.kosumorin.journal.data.entity.Entry
import io.github.kosumorin.journal.data.entity.EntryWithMetadata
import io.github.kosumorin.journal.data.repository.EntryRepository
import io.github.kosumorin.journal.data.repository.TagRepository
import io.github.kosumorin.journal.feature.editor.tag.EditorTagStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import java.lang.Exception
import javax.inject.Inject

class EditorViewModel @Inject constructor(
    private val entryRepository: EntryRepository,
    private val tagRepository: TagRepository
) : ViewModel() {
    var currentEntryId: String? = null
        private set

    // Flags
    var hasInit = false
    var isViewingMode = false
    var isDirty = false
    var alertState: EditorAlertState = EditorAlertState.NONE

    // Live Data.
    val titleLiveData = MutableLiveData<String>("")
    val contentLiveData = MutableLiveData<String>("")
    val dateTimeLiveData = MutableLiveData<LocalDateTime>(LocalDateTime.now())

    val saveEntryResultLiveData = ConsumableLiveData<Result>()
    val loadEntryResultLiveData = ConsumableLiveData<Result>()
    val deleteEntryResultLiveData = ConsumableLiveData<Result>()

    val tagStore = EditorTagStore(viewModelScope, tagRepository)

    fun init() {
        tagStore.init()

        hasInit = true
    }

    // Actions.
    fun saveEntry() {
        val entryId = currentEntryId

        val entry = if (entryId != null) Entry(
            entryId,
            titleLiveData.value ?: "",
            contentLiveData.value ?: "",
            dateTimeLiveData.value ?: LocalDateTime.now()
        ) else Entry(
            titleLiveData.value ?: "",
            contentLiveData.value ?: "",
            dateTimeLiveData.value ?: LocalDateTime.now()
        )

        val tags = tagStore.selectedTagsLiveData.value ?: listOf()

        val entryWithMetadata = EntryWithMetadata(entry, tags)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (entryId == null) {
                    entryRepository.insertWithMetadata(entryWithMetadata)
                }
                else {
                    entryRepository.updateWithMetadata(entryWithMetadata)
                }

                saveEntryResultLiveData.postValue(Result.success())
            }
            catch (e: Exception) {
                saveEntryResultLiveData.postValue(Result.failure(e))
            }
        }
    }

    fun loadEntry(entryId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val entryWithMetadata = entryRepository.getWithMetadata(entryId)

                currentEntryId = entryWithMetadata.entry.entryId

                titleLiveData.postValue(entryWithMetadata.entry.title)
                contentLiveData.postValue(entryWithMetadata.entry.content)
                dateTimeLiveData.postValue(entryWithMetadata.entry.dateTime)

                tagStore.selectedTagsLiveData.postValue(entryWithMetadata.tags)

                loadEntryResultLiveData.postValue(Result.success())
            }
            catch (e: Exception) {
                loadEntryResultLiveData.postValue(Result.failure(e))
            }
        }
    }

    fun deleteEntry() {
        val entryToDeleteId = currentEntryId ?: return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                entryRepository.delete(entryToDeleteId)

                deleteEntryResultLiveData.postValue(Result.success())
            }
            catch (e: Exception) {
                deleteEntryResultLiveData.postValue(Result.failure(e))
            }
        }
    }
}