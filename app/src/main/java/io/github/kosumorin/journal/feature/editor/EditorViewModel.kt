package io.github.kosumorin.journal.feature.editor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.kosumorin.journal.utils.data.Result
import io.github.kosumorin.journal.utils.data.ConsumableLiveData
import io.github.kosumorin.journal.data.entity.Entry
import io.github.kosumorin.journal.data.repository.EntryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import java.lang.Exception
import javax.inject.Inject

class EditorViewModel @Inject constructor(private val entryRepository: EntryRepository) : ViewModel() {
    var currentEntryId: String? = null
        private set

    // Flags
    var hasInit = false
    var isViewingMode = false
    var isDirty = false
    var dialogState: EditorDialogState = EditorDialogState.NONE

    // Live Data.
    val titleLiveData = MutableLiveData<String>("")
    val contentLiveData = MutableLiveData<String>("")
    val dateTimeLiveData = MutableLiveData<LocalDateTime>(LocalDateTime.now())

    val saveResultLiveData = ConsumableLiveData<Result>()
    val loadResultLiveData = ConsumableLiveData<Result>()
    val deleteResultLiveData = ConsumableLiveData<Result>()

    // Actions.
    fun save() {
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

        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (entryId == null) {
                    entryRepository.insert(entry)
                }
                else {
                    entryRepository.update(entry)
                }

                saveResultLiveData.postValue(Result.success())
            }
            catch (e: Exception) {
                saveResultLiveData.postValue(Result.failure(e))
            }
        }
    }

    fun load(entryId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val entry = entryRepository.get(entryId)

                currentEntryId = entry.id
                titleLiveData.postValue(entry.title)
                contentLiveData.postValue(entry.content)
                dateTimeLiveData.postValue(entry.dateTime)

                loadResultLiveData.postValue(Result.success())
            }
            catch (e: Exception) {
                loadResultLiveData.postValue(Result.failure(e))
            }
        }
    }

    fun delete() {
        val entryToDeleteId = currentEntryId ?: return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                entryRepository.delete(entryToDeleteId)

                deleteResultLiveData.postValue(Result.success())
            }
            catch (e: Exception) {
                deleteResultLiveData.postValue(Result.failure(e))
            }
        }
    }
}