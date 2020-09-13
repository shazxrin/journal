package io.github.icedshytea.journal.feature.timeline

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.icedshytea.journal.data.entity.Entry
import io.github.icedshytea.journal.data.repository.EntryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import javax.inject.Inject

typealias DatedEntryList = Pair<LocalDate, List<Entry>>

class TimelineViewModel @Inject constructor(private val entryRepository: EntryRepository) : ViewModel() {
    val datedEntryList = MutableLiveData<DatedEntryList>()

    var currentDate: LocalDate = LocalDate.now()
        set(value) {
            field = value
            getEntries(value)
        }

    private var getEntriesJob: Job? = null
    private fun getEntries(date: LocalDate) {
        getEntriesJob?.cancel()

        getEntriesJob = viewModelScope.launch(Dispatchers.IO) {
            entryRepository.getEntriesSorted(date).collect { value -> datedEntryList.postValue(DatedEntryList(date, value)) }
        }
    }
}