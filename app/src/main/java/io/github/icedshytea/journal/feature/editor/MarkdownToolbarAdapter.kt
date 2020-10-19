package io.github.icedshytea.journal.feature.editor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.icedshytea.journal.R

class MarkdownToolbarAdapter()
    : ListAdapter<MarkdownToolbarAdapter.MarkdownToolItem, RecyclerView.ViewHolder>(MarkdownToolItemDiffCallback()) {
    var onToolItemClickHandler: ((MarkdownToolItem) -> Unit)? = null

    enum class MarkdownToolItem(val symbol: String, val hasSymbolTail: Boolean, val iconRes: Int) {
        BOLD("**", true, R.drawable.ic_bold),
        ITALIC("*", true, R.drawable.ic_italic),
        QUOTE("> ", false, R.drawable.ic_quote),
        CODE("```", true, R.drawable.ic_code),
        UNORD_LIST("- ", false, R.drawable.ic_unord_list),
        ORD_LIST("1. ", false, R.drawable.ic_ord_list),
        H1("# ", false, R.drawable.ic_h1),
        H2("## ", false, R.drawable.ic_h2),
        H3("### ", false, R.drawable.ic_h3),
        H4("#### ", false, R.drawable.ic_h4),
        H5("##### ", false, R.drawable.ic_h5),
        H6("###### ", false, R.drawable.ic_h6)
    }

    init {
        submitList(MarkdownToolItem.values().toMutableList())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MarkdownToolItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_markdown_tool, parent, false),
            onToolItemClickHandler
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holder: MarkdownToolItemViewHolder = holder as MarkdownToolItemViewHolder

        holder.clear()
        holder.bind(getItem(position))
    }

    class MarkdownToolItemDiffCallback : DiffUtil.ItemCallback<MarkdownToolbarAdapter.MarkdownToolItem>() {
        override fun areItemsTheSame(
            oldItem: MarkdownToolItem,
            newItem: MarkdownToolItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: MarkdownToolItem,
            newItem: MarkdownToolItem
        ): Boolean {
            return  oldItem.symbol == newItem.symbol
        }

    }

    class MarkdownToolItemViewHolder(
        private val view: View,
        private val onToolItemClickHandler: ((MarkdownToolItem) -> Unit)?
    ) : RecyclerView.ViewHolder(view) {
        private val markdownToolIconImageView = view.findViewById<ImageView>(R.id.markdown_tool_icon)

        fun clear() {
            markdownToolIconImageView.setImageDrawable(null)
            markdownToolIconImageView.setOnClickListener(null)
        }

        fun bind(markdownToolItem: MarkdownToolItem) {
            markdownToolIconImageView.setImageResource(markdownToolItem.iconRes)
            markdownToolIconImageView.setOnClickListener { onToolItemClickHandler?.invoke(markdownToolItem) }
        }
    }
}