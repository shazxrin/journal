package io.github.kosumorin.journal.feature

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.android.support.DaggerFragment
import io.github.kosumorin.journal.R
import io.github.kosumorin.journal.di.module.ViewModelFactory
import javax.inject.Inject

abstract class MainFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    protected var bottomAppBar: BottomAppBar? = null
    protected var fabBottomAppBar: FloatingActionButton? = null

    inline fun <reified VM : ViewModel> getViewModel(): VM {
        return ViewModelProviders.of(this, viewModelFactory).get(VM::class.java)
    }

    inline fun <reified VM : ViewModel> getSharedViewModel(): VM {
        return ViewModelProviders.of(requireActivity(), viewModelFactory).get(VM::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomAppBar = activity?.findViewById(R.id.bottomAppBar)
        fabBottomAppBar = activity?.findViewById(R.id.fab)
    }
}
