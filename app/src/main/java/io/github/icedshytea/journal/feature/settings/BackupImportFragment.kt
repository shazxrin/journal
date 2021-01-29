package io.github.icedshytea.journal.feature.settings

import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import io.github.icedshytea.journal.R

class BackupImportFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.backup_import_preferences, rootKey)

        findPreference<Preference>("backupToJSON")?.setOnPreferenceClickListener {
            Toast.makeText(requireActivity(), "Clicked!", Toast.LENGTH_LONG).show()
            return@setOnPreferenceClickListener true
        }
    }
}
