package io.github.kosumorin.journal.feature.tag

import androidx.lifecycle.LiveData
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

class TagViewModel @Inject constructor(
    private val tagRepository: TagRepository
) : ViewModel() {
    // Flags
    var hasInit = false

    // Live data
    private val _tags = MutableLiveData<List<Tag>>()
    val tags: LiveData<List<Tag>> = _tags

    // Results
    val deleteTagResult = ConsumableLiveData<Result>()
    val updateTagResult = ConsumableLiveData<Result>()
    val createTagResult = ConsumableLiveData<Result>()

    fun init() {
        viewModelScope.launch(Dispatchers.IO) {
            tagRepository.getAll().collect {
                _tags.postValue(it)
            }
        }

        hasInit = true
    }

    fun deleteTag(tag: Tag) {
        viewModelScope.launch(Dispatchers.IO) {
            tagRepository.delete(tag)

            deleteTagResult.postValue(Result.success())
        }
    }

    fun createTag(tag: Tag) {
        viewModelScope.launch(Dispatchers.IO) {
            tagRepository.insert(tag)

            createTagResult.postValue(Result.success())
        }
    }

    fun updateTag(newTag: Tag) {
        viewModelScope.launch(Dispatchers.IO) {
            tagRepository.update(newTag)

            updateTagResult.postValue(Result.success())
        }
    }
}