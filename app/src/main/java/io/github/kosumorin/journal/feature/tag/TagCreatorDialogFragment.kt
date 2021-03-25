package io.github.kosumorin.journal.feature.tag

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.navGraphViewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputLayout
import io.github.kosumorin.journal.R
import io.github.kosumorin.journal.data.entity.Tag
import io.github.kosumorin.journal.ui.DialogFragment

class TagCreatorDialogFragment : DialogFragment() {
    private val tagViewModel: TagViewModel by navGraphViewModels(R.id.tag_nav_graph) {
        viewModelFactory
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.dismissWithAnimation = true;

        return bottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sheet_editor_tag_creator_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<Button>(R.id.tag_creator_pos_button)?.setOnClickListener {
            val textInputLayout = view.findViewById<TextInputLayout>(R.id.tag_creator_tag_name_input)

            val newTagName = textInputLayout?.editText?.text.toString()

            if (newTagName.isBlank()) {
                textInputLayout.error = "Tag name cannot be blank!"

                return@setOnClickListener
            }

            tagViewModel.createTag(Tag(newTagName, ""))
        }

        view.findViewById<Button>(R.id.tag_creator_neg_button)?.setOnClickListener {
            dismiss()
        }

        tagViewModel.createTagResult.consume(viewLifecycleOwner) {
            if (it.isSuccess()) {
                dismiss()
            }
        }
    }
}
