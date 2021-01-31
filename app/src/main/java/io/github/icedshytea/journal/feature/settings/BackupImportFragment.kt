package io.github.icedshytea.journal.feature.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import io.github.icedshytea.journal.R
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

class BackupImportFragment : PreferenceFragmentCompat() {
    private val WRITE_REQUEST_CODE = 100
    private val READ_REQUEST_CODE = 101

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.backup_import_preferences, rootKey)

        findPreference<Preference>("backupToJSON")?.setOnPreferenceClickListener {
            val backupFileName = "journal_backup_${LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)}.json"

            val fileSaveIntent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                type = "application/json"
                putExtra(Intent.EXTRA_TITLE, backupFileName)
            }

            startActivityForResult(fileSaveIntent, WRITE_REQUEST_CODE)

            return@setOnPreferenceClickListener true
        }

        findPreference<Preference>("importFromJSON")?.setOnPreferenceClickListener {
            val fileSaveIntent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "application/json"
            }

            startActivityForResult(fileSaveIntent, READ_REQUEST_CODE)

            return@setOnPreferenceClickListener true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            WRITE_REQUEST_CODE -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        if (data != null && data.data != null) {
                            val backupServiceIntent = Intent(requireActivity(), BackupService::class.java).apply {
                                setData(data.data)
                            }

                            requireActivity().startService(backupServiceIntent)
                        }
                    }
                    Activity.RESULT_CANCELED -> return
                }
            }
            READ_REQUEST_CODE -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        if (data != null && data.data != null) {
                            val importServiceIntent = Intent(requireActivity(), ImportService::class.java).apply {
                                setData(data.data)
                            }

                            requireActivity().startService(importServiceIntent)
                        }
                    }
                    Activity.RESULT_CANCELED -> return
                }
            }
        }
    }
}
