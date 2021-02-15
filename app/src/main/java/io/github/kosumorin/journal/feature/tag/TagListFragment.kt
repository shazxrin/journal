package io.github.kosumorin.journal.feature.tag

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.github.kosumorin.journal.R
import io.github.kosumorin.journal.ui.DialogFragment

class TagListFragment() : DialogFragment() {
    private val tagListAdapter = TagListAdapter()

    private lateinit var tagViewModel: TagViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tagViewModel = getSharedViewModel()

        if (!tagViewModel.hasInit) {
            tagViewModel.init()

            tagViewModel.hasInit = true
        }
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
        return inflater.inflate(R.layout.sheet_tag_list_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.tag_list_recycler_view)
        recyclerView?.adapter = tagListAdapter
        tagListAdapter.onTagItemClickedHandler = { tag, isChecked ->
            if (isChecked) {
                tagViewModel.selectTag(tag)
            } else {
                tagViewModel.deselectTag(tag)
            }
        }
        tagViewModel.tagsLiveData.observe(viewLifecycleOwner) {
            tagListAdapter.submitList(it)
        }

        view.findViewById<Button>(R.id.tag_list_pos_button)?.setOnClickListener {
            dismiss()
        }
        view.findViewById<Button>(R.id.tag_list_neg_button)?.setOnClickListener {
            dismiss()
        }
        view.findViewById<TextView>(R.id.tag_list_create_button)?.setOnClickListener {
            TagCreatorFragment().show(
                childFragmentManager,
                "TagsCreatorFragment"
            )
        }
    }
}