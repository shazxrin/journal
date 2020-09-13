package io.github.icedshytea.journal.feature

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.github.icedshytea.journal.R
import io.github.icedshytea.journal.common.BaseFragment

abstract class MainFragment : BaseFragment() {
    protected var bottomAppBar: BottomAppBar? = null
    protected var fabBottomAppBar: FloatingActionButton? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomAppBar = activity?.findViewById(R.id.bottomAppBar)
        fabBottomAppBar = activity?.findViewById(R.id.fab)
    }
}
