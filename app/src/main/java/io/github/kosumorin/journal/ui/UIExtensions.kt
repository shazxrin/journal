package io.github.kosumorin.journal.ui

import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

val Fragment.actionBar: ActionBar?
    get() {
        val appCompatActivity = this.activity as? AppCompatActivity
        return appCompatActivity?.supportActionBar
    }
