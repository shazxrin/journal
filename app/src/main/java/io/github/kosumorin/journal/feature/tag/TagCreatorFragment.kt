package io.github.kosumorin.journal.feature.tag

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputLayout
import io.github.kosumorin.journal.R
import io.github.kosumorin.journal.ui.BottomSheetDialogFragment

class TagCreatorFragment : BottomSheetDialogFragment() {
    private lateinit var tagViewModel: TagViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tagViewModel = getSharedViewModel()
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
        return inflater.inflate(R.layout.sheet_tag_creator_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<Button>(R.id.tag_creator_pos_button)?.setOnClickListener {
            val newTagName = view
                .findViewById<TextInputLayout>(R.id.tag_creator_tag_name_input)
                ?.editText
                ?.text
                .toString()

            tagViewModel.create(newTagName)
        }

        view.findViewById<Button>(R.id.tag_creator_neg_button)?.setOnClickListener {
            dismiss()
        }

        tagViewModel.createResultLiveData.consume(viewLifecycleOwner) {
            if (it.isSuccess()) {
                dismiss()
            }
        }
    }
}