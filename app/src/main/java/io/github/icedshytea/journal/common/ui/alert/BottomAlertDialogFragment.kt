package io.github.icedshytea.journal.common.ui.alert

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.icedshytea.journal.R

class BottomAlertDialogFragment(
    private val title: String,
    private val positiveButtonText: String,
    private val negativeButtonText: String,
    private val callback: DialogInterface.OnClickListener
) : BottomSheetDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // TODO: Throw if context is null.
        val bottomSheetDialog = BottomSheetDialog(context!!)
        bottomSheetDialog.setContentView(R.layout.sheet_alert_dialog)
        bottomSheetDialog.dismissWithAnimation = true;

        bottomSheetDialog.findViewById<TextView>(R.id.title)?.text = title
        bottomSheetDialog.findViewById<Button>(R.id.positive_button)?.let {
            it.text = positiveButtonText
            it.setOnClickListener { callback.onClick(bottomSheetDialog, R.id.positive_button) }
        }
        bottomSheetDialog.findViewById<Button>(R.id.negative_button)?.let {
            it.text = negativeButtonText
            it.setOnClickListener { callback.onClick(bottomSheetDialog, R.id.negative_button) }
        }

        return bottomSheetDialog
    }
}