package io.github.icedshytea.journal.common

import android.content.Context
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import io.github.icedshytea.journal.di.module.ViewModelFactory
import javax.inject.Inject

abstract class BaseDialogFragment() : DialogFragment(), HasSupportFragmentInjector {
    @Inject
    lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment?>

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment?>? {
        return childFragmentInjector
    }

    inline fun <reified VM : ViewModel> initViewModel(): VM {
        return ViewModelProviders.of(this, viewModelFactory).get(VM::class.java)
    }

    inline fun <reified VM : ViewModel> initSharedViewModel(): VM {
        return ViewModelProviders.of(requireActivity(), viewModelFactory).get(VM::class.java)
    }
}