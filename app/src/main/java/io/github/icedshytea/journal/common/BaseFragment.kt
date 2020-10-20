package io.github.icedshytea.journal.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import dagger.android.support.DaggerFragment
import io.github.icedshytea.journal.di.module.ViewModelFactory
import javax.inject.Inject

abstract class BaseFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    inline fun <reified VM : ViewModel> initViewModel(): VM {
        return ViewModelProviders.of(this, viewModelFactory).get(VM::class.java)
    }

    inline fun <reified VM : ViewModel> initSharedViewModel(): VM {
        return ViewModelProviders.of(requireActivity(), viewModelFactory).get(VM::class.java)
    }
}