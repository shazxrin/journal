package io.github.kosumorin.journal.feature.editor.tag

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.github.kosumorin.journal.R
import io.github.kosumorin.journal.feature.editor.EditorViewModel
import io.github.kosumorin.journal.ui.DialogFragment

class EditorTagListFragment() : DialogFragment() {
    private val tagListAdapter = EditorTagListAdapter()

    private val editorViewModel: EditorViewModel by navGraphViewModels(R.id.editor_nav_graph) {
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
        return inflater.inflate(R.layout.sheet_editor_tag_list_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.tag_list_recycler_view)
        recyclerView?.adapter = tagListAdapter
        tagListAdapter.onTagItemClickedHandler = { tag, isChecked ->
            if (isChecked) {
                editorViewModel.tagStore.selectTag(tag)
            } else {
                editorViewModel.tagStore.deselectTag(tag)
            }
        }
        editorViewModel.tagStore.tagsWithSelectedStateLiveData.observe(viewLifecycleOwner) {
            tagListAdapter.submitList(it)
        }

        view.findViewById<Button>(R.id.tag_list_pos_button)?.setOnClickListener {
            dismiss()
        }
        view.findViewById<Button>(R.id.tag_list_neg_button)?.setOnClickListener {
            dismiss()
        }
        view.findViewById<TextView>(R.id.tag_list_create_button)?.setOnClickListener {
            findNavController().navigate(
                EditorTagListFragmentDirections.actionEditorTagListFragmentToEditorTagCreatorFragment()
            )
        }
    }
}