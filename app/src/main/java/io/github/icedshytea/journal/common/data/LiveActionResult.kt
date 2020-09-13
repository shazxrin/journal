package io.github.icedshytea.journal.common.data

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class LiveActionResult : MutableLiveData<ActionResult>() {
    private val hasValueConsumed = AtomicBoolean(false)

    fun consume(owner: LifecycleOwner, observer: Observer<ActionResult>) {
        super.observe(owner, Observer {
            if (hasValueConsumed.compareAndSet(false, true)) {
                observer.onChanged(it)
            }
        })
    }

    override fun postValue(value: ActionResult?) {
        hasValueConsumed.set(false)
        super.postValue(value)
    }

    override fun setValue(value: ActionResult?) {
        hasValueConsumed.set(false)
        super.setValue(value)
    }
}