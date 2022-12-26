package io.github.shazxrin.journal.utils.data

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class ConsumableLiveData<T> : MutableLiveData<T>() {
    private val hasValueConsumed = AtomicBoolean(false)

    fun consume(owner: LifecycleOwner, observer: Observer<T>) {
        super.observe(owner, Observer {
            if (hasValueConsumed.compareAndSet(false, true)) {
                observer.onChanged(it)
            }
        })
    }

    override fun postValue(value: T?) {
        hasValueConsumed.set(false)
        super.postValue(value)
    }

    override fun setValue(value: T?) {
        hasValueConsumed.set(false)
        super.setValue(value)
    }
}