package io.github.icedshytea.journal.common.data

class Result private constructor() {
    private var throwable: Throwable? = null
    private var isSuccess: Boolean = false

    companion object {
        fun success(): Result {
            return Result()
                .apply { isSuccess = true }
        }

        fun failure(throwable: Throwable): Result {
            return Result().apply {
                this.isSuccess = false
                this.throwable = throwable
            }
        }
    }

    fun isSuccess() = isSuccess
    fun isFailure() = !isSuccess

    fun getThrowableOrNull() = throwable

    fun onSuccess(action: () -> Unit): Result {
        if (isSuccess()) {
            action.invoke()
        }

        return this
    }

    fun onFailure(action: (Throwable?) -> Unit): Result {
        if (isFailure()) {
            getThrowableOrNull()?.let(action)
        }

        return this
    }
}
