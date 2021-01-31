package io.github.icedshytea.journal.feature

import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import io.github.icedshytea.journal.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : DaggerAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(bottomAppBar)
    }
}