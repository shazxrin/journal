package io.github.icedshytea.journal.feature

import android.os.Bundle
import io.github.icedshytea.journal.R
import io.github.icedshytea.journal.common.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(bottomAppBar)
    }
}