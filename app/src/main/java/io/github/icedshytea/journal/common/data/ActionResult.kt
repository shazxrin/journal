package io.github.icedshytea.journal.common.data

class ActionResult private constructor() {
    private var throwable: Throwable? = null
    private var isSuccess: Boolean = false

    companion object {
        fun success(): ActionResult {
            return ActionResult()
                .apply { isSuccess = true }
        }

        fun failure(throwable: Throwable): ActionResult {
            return ActionResult().apply {
                this.isSuccess = false
                this.throwable = throwable
            }
        }
    }

    fun isSuccess() = isSuccess
    fun isFailure() = !isSuccess

    fun getThrowableOrNull() = throwable

    fun onSuccess(action: () -> Unit): ActionResult {
        if (isSuccess()) {
            action.invoke()
        }

        return this
    }

    fun onFailure(action: (Throwable?) -> Unit): ActionResult {
        if (isFailure()) {
            getThrowableOrNull()?.let(action)
        }

        return this
    }
}
