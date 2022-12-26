package io.github.shazxrin.journal.feature.timeline

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.shazxrin.journal.R
import io.github.shazxrin.journal.data.entity.Entry
import io.noties.markwon.Markwon
import kotlinx.android.synthetic.main.item_timeline_entry.view.*
import kotlinx.android.synthetic.main.item_timeline_header.view.*
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.lang.Exception

class TimelineListAdapter(private val markwon: Markwon)
    : ListAdapter<TimelineListAdapter.TimelineListItem, RecyclerView.ViewHolder>(TimelineListItemDiffCallback()) {
    private var onEntryItemClickedHandler: ((Entry) -> Unit)? = null

    fun setData(localDate: LocalDate, entries: List<Entry>) {
        val finalListItems = entries.map { TimelineListEntryItem(it) }
            .toMutableList<TimelineListItem>()
            .apply {
                add(0, TimelineListHeader(localDate))
                toList()
            }

        submitList(finalListItems)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TimelineListItem.TYPE_HEADER) {
            return HeaderViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_timeline_header, parent, false)
            )
        }
        else if (viewType == TimelineListItem.TYPE_ENTRY) {
            return EntryItemViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_timeline_entry, parent, false),
                onEntryItemClickedHandler
            )
        }

        throw Exception("Unspecified view type!")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        if (holder is EntryItemViewHolder && item is TimelineListEntryItem) {
            holder.clear()
            holder.bind(item.entry)
        }
        else if (holder is HeaderViewHolder && item is TimelineListHeader) {
            holder.clear()
            holder.bind(item.date)
        }
    }

    fun setOnEntryItemClicked(handler: (Entry) -> Unit) {
        onEntryItemClickedHandler = handler
    }

    open class TimelineListItem(val viewType: Int) {
        companion object {
            const val TYPE_HEADER = 1
            const val TYPE_ENTRY = 2
        }
    }
    class TimelineListHeader(val date: LocalDate) : TimelineListItem(TYPE_HEADER)
    class TimelineListEntryItem(val entry: Entry) : TimelineListItem(TYPE_ENTRY)

    class TimelineListItemDiffCallback : DiffUtil.ItemCallback<TimelineListItem>() {
        override fun areItemsTheSame(oldItem: TimelineListItem, newItem: TimelineListItem): Boolean {
            return if (oldItem is TimelineListEntryItem && newItem is TimelineListEntryItem) {
                oldItem.entry.id == newItem.entry.id
            } else if (oldItem is TimelineListHeader && newItem is TimelineListHeader) {
                oldItem.date == newItem.date
            } else {
                false
            }
        }

        override fun areContentsTheSame(oldItem: TimelineListItem, newItem: TimelineListItem): Boolean {
            return if (oldItem is TimelineListEntryItem && newItem is TimelineListEntryItem) {
                oldItem.entry == newItem.entry
            } else if (oldItem is TimelineListHeader && newItem is TimelineListHeader) {
                oldItem.date == newItem.date
            } else {
                false
            }
        }
    }

    inner class HeaderViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val dayTextView = view.day

        fun bind(date: LocalDate) {
            if (date.isEqual(LocalDate.now())) {
                dayTextView.text = view.context.getText(R.string.list_item_timeline_today)
            }
            else {
                dayTextView.text = date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
            }

        }

        fun clear() {
            dayTextView.text = ""
        }
    }

    inner class EntryItemViewHolder(
        private val view: View,
        private val onEntryItemClickHandler: ((Entry) -> Unit)?
    ) : RecyclerView.ViewHolder(view) {
        private val titleTextView = view.title
        private val timeTextView = view.time
        private val contentTextView = view.content

        fun bind(entry: Entry?) {
            if (entry == null) {
                return
            }

            titleTextView.text = entry.title
            contentTextView.text = markwon.toMarkdown(entry.content)
            timeTextView.text = entry.dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))

            view.setOnClickListener {
                onEntryItemClickHandler?.invoke(entry)
            }
        }

        fun clear() {
            titleTextView.text = ""
            timeTextView.text = ""
            contentTextView.text = ""

            view.setOnClickListener(null)
        }
    }
}
