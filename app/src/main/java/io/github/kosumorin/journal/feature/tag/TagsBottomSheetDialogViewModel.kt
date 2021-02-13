package io.github.kosumorin.journal.feature.tag

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.kosumorin.journal.data.entity.Tag
import io.github.kosumorin.journal.data.repository.TagRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class TagsBottomSheetDialogViewModel @Inject constructor(private val tagRepository: TagRepository)
    : ViewModel() {

    val tagsLiveData = MutableLiveData<List<Tag>>()

    fun init() {
        viewModelScope.launch(Dispatchers.IO) {
            tagRepository.getAll().collect { list -> tagsLiveData.postValue(list) }
        }
    }

    fun create(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            tagRepository.insert(Tag(name, "", ""))
        }
    }
}
