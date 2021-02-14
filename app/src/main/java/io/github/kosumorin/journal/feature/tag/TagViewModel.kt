package io.github.kosumorin.journal.feature.tag

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.kosumorin.journal.data.entity.Tag
import io.github.kosumorin.journal.data.repository.TagRepository
import io.github.kosumorin.journal.utils.data.ConsumableLiveData
import io.github.kosumorin.journal.utils.data.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class TagViewModel @Inject constructor(private val tagRepository: TagRepository)
    : ViewModel() {
    var hasInit = false

    val selectedTagsLiveData = MutableLiveData<List<Tag>>()
    val tagsLiveData = MutableLiveData<List<Tag>>()

    val createResultLiveData = ConsumableLiveData<Result>()

    fun init() {
        viewModelScope.launch(Dispatchers.IO) {
            tagRepository.getAll().collect { list -> tagsLiveData.postValue(list) }
        }
    }

    fun create(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            tagRepository.insert(Tag(name, "", ""))

            createResultLiveData.postValue(Result.success())
        }
    }

    fun selectTag(tag: Tag) {

    }

    fun deselectTag(tag: Tag) {

    }
}
