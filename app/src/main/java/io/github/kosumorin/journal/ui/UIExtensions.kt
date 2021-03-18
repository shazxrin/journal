package io.github.kosumorin.journal.ui

import android.text.InputType
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment

val Fragment.actionBar: ActionBar?
    get() {
        val appCompatActivity = this.activity as? AppCompatActivity
        return appCompatActivity?.supportActionBar
    }

fun AppCompatEditText.setReadOnly(isReadOnly: Boolean) {
    isFocusable = !isReadOnly
    isFocusableInTouchMode = !isReadOnly
    inputType = if (isReadOnly) InputType.TYPE_NULL else InputType.TYPE_CLASS_TEXT
}
