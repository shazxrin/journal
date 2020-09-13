package io.github.icedshytea.journal.common.data

import androidx.lifecycle.MutableLiveData

class LiveField<T>(initVal: T) : MutableLiveData<T>() {
    init {
        value = initVal
    }
}
