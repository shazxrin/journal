package io.github.kosumorin.journal.feature.tag

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.kosumorin.journal.R
import io.github.kosumorin.journal.data.entity.Tag

class TagListAdapter : ListAdapter<Tag, RecyclerView.ViewHolder>(TagDiffCallback()) {
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
        private val tagNameTextView = view.findViewById<AppCompatTextView>(R.id.tag_name)

        fun bind(tag: Tag) {
            tagNameTextView.text = tag.name
        }

        fun clear() {
            tagNameTextView.text = ""
        }
    }
}
