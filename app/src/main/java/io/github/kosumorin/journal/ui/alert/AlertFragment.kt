package io.github.kosumorin.journal.ui.alert

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.github.kosumorin.journal.R
import io.github.kosumorin.journal.ui.DialogFragment

class AlertFragment() : DialogFragment() {
    private lateinit var alertViewModel: AlertViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        alertViewModel = getSharedViewModel()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(R.layout.sheet_alert_dialog)
        bottomSheetDialog.dismissWithAnimation = true;

        bottomSheetDialog.findViewById<TextView>(R.id.title)?.text = alertViewModel.title
        bottomSheetDialog.findViewById<Button>(R.id.positive_button)?.let {
            it.text = alertViewModel.positiveButtonText
            it.setOnClickListener { alertViewModel.userSelectionLiveData.postValue(AlertResponse.POSITIVE) }
        }
        bottomSheetDialog.findViewById<Button>(R.id.negative_button)?.let {
            it.text = alertViewModel.negativeButtonText
            it.setOnClickListener {
                if (alertViewModel.dismissOnNegative) {
                    dismiss()
                } else {
                    alertViewModel.userSelectionLiveData.postValue(AlertResponse.NEGATIVE)
                }
            }
        }

        return bottomSheetDialog
    }
}