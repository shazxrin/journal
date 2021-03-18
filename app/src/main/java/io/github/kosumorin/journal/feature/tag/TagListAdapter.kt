package io.github.kosumorin.journal.feature.tag

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.kosumorin.journal.R
import io.github.kosumorin.journal.data.entity.Tag
import io.github.kosumorin.journal.ui.setReadOnly

typealias TagListItemSaveClickedHandler = ((newTag: Tag) -> Unit)
typealias TagListItemDeleteClickedHandler = ((tag: Tag) -> Unit)

class TagListAdapter : ListAdapter<Tag, RecyclerView.ViewHolder>(TagDiffCallback()) {
    var onTagItemSaveClickedHandler: TagListItemSaveClickedHandler? = null
    var onTagItemDeleteClickedHandler: TagListItemDeleteClickedHandler? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TagViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_tag, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        val holder = holder as TagViewHolder
        holder.clear()
        holder.bind(item)
    }


    class TagDiffCallback : DiffUtil.ItemCallback<Tag>() {
        override fun areItemsTheSame(oldItem: Tag, newItem: Tag): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Tag, newItem: Tag): Boolean {
            return oldItem == newItem
        }
    }

    inner class TagViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var isEditMode = false

        private val tagNameEditText = view.findViewById<AppCompatEditText>(R.id.tag_item_name)
        private val tagEditButton = view.findViewById<ImageButton>(R.id.tag_item_edit)
        private val tagSaveButton = view.findViewById<ImageButton>(R.id.tag_item_save)
        private val tagDeleteButton = view.findViewById<ImageButton>(R.id.tag_item_delete)

        init {
            tagNameEditText.setReadOnly(true)

            tagEditButton.setOnClickListener {
                isEditMode = !isEditMode

                setupTagListItem()
            }
        }

        fun bind(tag: Tag) {
            tagNameEditText.setText(tag.name)

            tagSaveButton.setOnClickListener {
                onTagItemSaveClickedHandler?.invoke(tag.copy(name = tagNameEditText.text.toString()))
            }
            tagDeleteButton.setOnClickListener { onTagItemDeleteClickedHandler?.invoke(tag) }
        }

        fun clear() {
            tagNameEditText.setText("")

            tagSaveButton.setOnClickListener(null)
            tagDeleteButton.setOnClickListener(null)
        }

        private fun setupTagListItem() {
            tagNameEditText.setReadOnly(!isEditMode)

            if (isEditMode) {
                tagEditButton.visibility = View.GONE

                tagSaveButton.visibility = View.VISIBLE
                tagDeleteButton.visibility = View.VISIBLE

                tagNameEditText.requestFocus()
                tagNameEditText.setSelection(tagNameEditText.text?.length ?: 0)
            } else {
                tagEditButton.visibility = View.VISIBLE

                tagSaveButton.visibility = View.GONE
                tagDeleteButton.visibility = View.GONE
            }
        }
    }
}
