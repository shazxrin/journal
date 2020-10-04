package io.github.icedshytea.journal.feature.timeline

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.bottomappbar.BottomAppBar
import io.github.icedshytea.journal.R
import io.github.icedshytea.journal.common.ui.actionBar
import io.github.icedshytea.journal.feature.MainFragment
import io.github.icedshytea.journal.utils.datetime.DatePickerDialogFragment
import kotlinx.android.synthetic.main.fragment_timeline.*
import org.threeten.bp.LocalDate

class TimelineFragment : MainFragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var timelineViewModel: TimelineViewModel

    private val entryAdapter = TimelineListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        timelineViewModel = initViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_timeline, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBottomAppBar()
        fabBottomAppBar?.setOnClickListener {
            findNavController().navigate(
                TimelineFragmentDirections.timelineOpenEditorAction()
            )
        }

        recyclerView.adapter = entryAdapter
        timelineViewModel.datedEntryList.observe(viewLifecycleOwner, Observer {
            entryAdapter.setData(it.first, it.second)

            if (it.second.isEmpty()) {
                noEntryLayout.visibility = View.VISIBLE
            }
            else {
                noEntryLayout.visibility = View.INVISIBLE
            }
        })
        entryAdapter.setOnEntryItemClicked {
            findNavController().navigate(
                TimelineFragmentDirections.timelineOpenEditorAction(it.id)
            )
        }
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        timelineViewModel.currentDate = LocalDate.now()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_timeline, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.datePicker) {
            val datePickerDialogFragment = DatePickerDialogFragment(this, timelineViewModel.currentDate)
            datePickerDialogFragment.show(this.childFragmentManager, "datePicker")
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setupBottomAppBar() {
        fabBottomAppBar?.setImageResource(R.drawable.ic_add)
        fabBottomAppBar?.show()

        bottomAppBar?.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
        bottomAppBar?.performShow()
        bottomAppBar?.hideOnScroll = true

        actionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        // Java's Calendar API's months start from 0 so we need to +- accordingly with 310's months.
        timelineViewModel.currentDate = LocalDate.of(year, month + 1, dayOfMonth)
    }
}