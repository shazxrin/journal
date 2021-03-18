package io.github.kosumorin.journal.feature.tag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import io.github.kosumorin.journal.R
import io.github.kosumorin.journal.data.entity.Tag
import io.github.kosumorin.journal.feature.MainFragment
import io.github.kosumorin.journal.ui.actionBar

class TagFragment() : MainFragment() {
    private lateinit var tagViewModel: TagViewModel

    private val tagListAdapter = TagListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tagViewModel = getViewModel()
        if (!tagViewModel.hasInit) {
            tagViewModel.init()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tag, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBottomAppBar()

        val recyclerView = view.findViewById<RecyclerView>(R.id.tag_list_recycler_view)
        tagListAdapter.onTagItemSaveClickedHandler = { newTag: Tag -> tagViewModel.updateTag(newTag) }
        tagListAdapter.onTagItemDeleteClickedHandler = { tag: Tag -> tagViewModel.deleteTag(tag) }
        recyclerView.adapter = tagListAdapter
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        tagViewModel.tags.observe(viewLifecycleOwner) {
            tagListAdapter.submitList(it)
        }
    }

    private fun setupBottomAppBar() {
        fabBottomAppBar?.hide()

        bottomAppBar?.performShow()
        bottomAppBar?.hideOnScroll = false

        actionBar?.setDisplayHomeAsUpEnabled(false)
    }
}