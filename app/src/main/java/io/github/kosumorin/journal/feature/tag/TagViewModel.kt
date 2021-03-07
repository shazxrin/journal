package io.github.kosumorin.journal.feature.tag

import androidx.lifecycle.MediatorLiveData
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

typealias TagWithSelectedState = Pair<Tag, Boolean>

class TagViewModel @Inject constructor(private val tagRepository: TagRepository)
    : ViewModel() {
    var hasInit = false

    val selectedTagsLiveData = MutableLiveData<List<Tag>>(listOf())
    val tagsLiveData = MutableLiveData<List<Tag>>()
    val tagsWithSelectedStateLiveData = MediatorLiveData<List<TagWithSelectedState>>()

    val createResultLiveData = ConsumableLiveData<Result>()

    private fun mergeTagAndSelectedState(): List<TagWithSelectedState> {
        val selectedTags = selectedTagsLiveData.value ?: listOf()
        val tags = tagsLiveData.value ?: listOf()

        return tags.map { tag ->
            TagWithSelectedState(tag, selectedTags.contains(tag))
        }.toList()
    }

    fun init() {
        tagsWithSelectedStateLiveData.apply {
            addSource(tagsLiveData) {
                tagsWithSelectedStateLiveData.postValue(mergeTagAndSelectedState())
            }

            addSource(selectedTagsLiveData) {
                tagsWithSelectedStateLiveData.postValue(mergeTagAndSelectedState())
            }
        }

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
        val selectedTagsSnapshot = selectedTagsLiveData.value

        selectedTagsLiveData.postValue(selectedTagsSnapshot?.plus(tag))
    }

    fun deselectTag(tag: Tag) {
        val selectedTagsSnapshot = selectedTagsLiveData.value

        selectedTagsLiveData.postValue(selectedTagsSnapshot?.filter { it != tag })
    }

    fun clearSelectedTags() {
        selectedTagsLiveData.postValue(listOf())
    }
}
