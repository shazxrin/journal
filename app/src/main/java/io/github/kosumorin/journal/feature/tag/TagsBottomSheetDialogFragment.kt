package io.github.kosumorin.journal.feature.tag

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.github.kosumorin.journal.R
import io.github.kosumorin.journal.ui.BottomSheetDialogFragment

class TagsBottomSheetDialogFragment() : BottomSheetDialogFragment() {
    private enum class ViewState { INIT, LIST, CREATOR }
    private var viewState: ViewState = ViewState.INIT

    private lateinit var tagsBottomSheetDialogViewModel: TagsBottomSheetDialogViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tagsBottomSheetDialogViewModel = getSharedViewModel()
    }

    private fun clearTagList() {
        val bottomSheetDialog = dialog as BottomSheetDialog

        bottomSheetDialog.findViewById<TextView>(R.id.tag_list_pos_button)?.setOnLongClickListener(null)
        bottomSheetDialog.findViewById<TextView>(R.id.tag_list_neg_button)?.setOnLongClickListener(null)
        bottomSheetDialog.findViewById<TextView>(R.id.tag_list_create_button)?.setOnLongClickListener(null)
    }

    private fun setupTagList(bottomSheetDialog: BottomSheetDialog) {
        if (viewState == ViewState.CREATOR) {
            clearTagCreator()
        }

        bottomSheetDialog.setContentView(R.layout.sheet_tag_list_dialog)

        bottomSheetDialog.findViewById<Button>(R.id.tag_list_pos_button)?.setOnClickListener {
            // Dismiss for now
            dismiss()
        }
        bottomSheetDialog.findViewById<Button>(R.id.tag_list_neg_button)?.setOnClickListener {
            dismiss()
        }
        bottomSheetDialog.findViewById<TextView>(R.id.tag_list_create_button)?.setOnClickListener {
            showTagCreator()
        }

        viewState = ViewState.LIST
    }

    private fun clearTagCreator() {
        val bottomSheetDialog = dialog as BottomSheetDialog

        bottomSheetDialog.findViewById<Button>(R.id.tag_creator_pos_button)?.setOnClickListener(null)
        bottomSheetDialog.findViewById<Button>(R.id.tag_creator_neg_button)?.setOnClickListener(null)
    }

    private fun setupTagCreator(bottomSheetDialog: BottomSheetDialog) {
        if (viewState == ViewState.LIST) {
            clearTagList()
        }

        bottomSheetDialog.setContentView(R.layout.sheet_tag_creator_dialog)

        bottomSheetDialog.findViewById<Button>(R.id.tag_creator_pos_button)?.setOnClickListener {
            // Dismiss for now
            dismiss()
        }
        bottomSheetDialog.findViewById<Button>(R.id.tag_creator_neg_button)?.setOnClickListener {
           showTagList()
        }

        viewState = ViewState.CREATOR
    }

    private fun showTagList() {
        val bottomSheetDialog = dialog as BottomSheetDialog

        setupTagList(bottomSheetDialog)
    }

    private fun showTagCreator() {
        val bottomSheetDialog = dialog as BottomSheetDialog

        setupTagCreator(bottomSheetDialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.dismissWithAnimation = true;

        setupTagList(bottomSheetDialog)

        return bottomSheetDialog
    }
}