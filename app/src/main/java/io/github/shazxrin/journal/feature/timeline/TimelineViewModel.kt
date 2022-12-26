package io.github.shazxrin.journal.feature.timeline

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.shazxrin.journal.data.entity.Entry
import io.github.shazxrin.journal.data.repository.EntryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import javax.inject.Inject

typealias DatedEntryList = Pair<LocalDate, List<Entry>>

class TimelineViewModel @Inject constructor(private val entryRepository: EntryRepository) : ViewModel() {
    // State.
    var hasInit: Boolean = false

    val datedEntryListLiveData = MutableLiveData<DatedEntryList>()

    var currentDate: LocalDate = LocalDate.now()
        set(value) {
            field = value
            getEntries(value)
        }

    private var getEntriesJob: Job? = null
    private fun getEntries(date: LocalDate) {
        getEntriesJob?.cancel()

        getEntriesJob = viewModelScope.launch(Dispatchers.IO) {
            entryRepository.getEntriesSorted(date).collect { value -> datedEntryListLiveData.postValue(DatedEntryList(date, value)) }
        }
    }
}