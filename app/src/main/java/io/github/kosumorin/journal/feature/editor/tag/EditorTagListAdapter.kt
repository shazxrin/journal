package io.github.kosumorin.journal.feature.editor.tag

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.kosumorin.journal.R
import io.github.kosumorin.journal.data.entity.Tag

class EditorTagListAdapter : ListAdapter<TagWithSelectedState, RecyclerView.ViewHolder>(TagDiffCallback()) {
    var onTagItemClickedHandler: ((tag: Tag, isChecked: Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TagViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_editor_tag, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        val holder = holder as TagViewHolder
        holder.clear()
        holder.bind(item)
    }


    class TagDiffCallback : DiffUtil.ItemCallback<TagWithSelectedState>() {
        override fun areItemsTheSame(oldItem: TagWithSelectedState, newItem: TagWithSelectedState): Boolean {
            return oldItem.tag === newItem.tag
        }

        override fun areContentsTheSame(oldItem: TagWithSelectedState, newItem: TagWithSelectedState): Boolean {
            return oldItem.tag == newItem.tag
        }
    }

    inner class TagViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tagNameTextView = view.findViewById<AppCompatTextView>(R.id.tag_item_name)
        private val tagItemContainer = view.findViewById<View>(R.id.tag_item_container)
        private val tagItemCheckbox = view.findViewById<CheckBox>(R.id.tag_item_checkbox)

        fun bind(tagWithSelectedState: TagWithSelectedState) {
            tagNameTextView.text = tagWithSelectedState.tag.name
            tagItemCheckbox.isChecked = tagWithSelectedState.isSelected

            tagItemContainer.setOnClickListener {
                tagItemCheckbox.isChecked = !tagItemCheckbox.isChecked

                onTagItemClickedHandler?.invoke(tagWithSelectedState.tag, tagItemCheckbox.isChecked)
            }
            tagItemCheckbox.setOnClickListener {
                onTagItemClickedHandler?.invoke(tagWithSelectedState.tag, tagItemCheckbox.isChecked)
            }
        }

        fun clear() {
            tagNameTextView.text = ""

            tagItemContainer.setOnClickListener(null)
            tagItemCheckbox.setOnClickListener(null)
        }
    }
}
