package io.github.kosumorin.journal.feature.editor.tag

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import io.github.kosumorin.journal.data.entity.Tag
import io.github.kosumorin.journal.data.repository.TagRepository
import io.github.kosumorin.journal.utils.data.ConsumableLiveData
import io.github.kosumorin.journal.utils.data.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class EditorTagStore(private val scope: CoroutineScope, private val tagRepository: TagRepository) {
    val selectedTagsLiveData = MutableLiveData<List<Tag>>(listOf())
    val tagsLiveData = MutableLiveData<List<Tag>>()
    val tagsWithSelectedStateLiveData = MediatorLiveData<List<TagWithSelectedState>>()

    val createTagResultLiveData = ConsumableLiveData<Result>()

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

        scope.launch(Dispatchers.IO) {
            tagRepository.getAll().collect { list -> tagsLiveData.postValue(list) }
        }
    }

    fun createTag(name: String) {
        scope.launch(Dispatchers.IO) {
            tagRepository.insert(Tag(name, "", ""))

            createTagResultLiveData.postValue(Result.success())
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

    fun setSelectedTags(tags: List<Tag>) {
        val selectedTagsSnapshot = selectedTagsLiveData.value

        selectedTagsLiveData.postValue(tags)
    }
}