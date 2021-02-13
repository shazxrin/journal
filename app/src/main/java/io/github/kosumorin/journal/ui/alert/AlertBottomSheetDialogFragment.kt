package io.github.kosumorin.journal.ui.alert

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.github.kosumorin.journal.R

class AlertBottomSheetDialogFragment() : BottomSheetDialogFragment() {
    private lateinit var alertBottomSheetDialogViewModel: AlertBottomSheetDialogViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        alertBottomSheetDialogViewModel = getSharedViewModel()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(R.layout.sheet_alert_dialog)
        bottomSheetDialog.dismissWithAnimation = true;

        bottomSheetDialog.findViewById<TextView>(R.id.title)?.text = alertBottomSheetDialogViewModel.title
        bottomSheetDialog.findViewById<Button>(R.id.positive_button)?.let {
            it.text = alertBottomSheetDialogViewModel.positiveButtonText
            it.setOnClickListener { alertBottomSheetDialogViewModel.userSelectionLiveData.postValue(AlertBottomSheetResponse.POSITIVE) }
        }
        bottomSheetDialog.findViewById<Button>(R.id.negative_button)?.let {
            it.text = alertBottomSheetDialogViewModel.negativeButtonText
            it.setOnClickListener {
                if (alertBottomSheetDialogViewModel.dismissOnNegative) {
                    dismiss()
                } else {
                    alertBottomSheetDialogViewModel.userSelectionLiveData.postValue(AlertBottomSheetResponse.NEGATIVE)
                }
            }
        }

        return bottomSheetDialog
    }
}