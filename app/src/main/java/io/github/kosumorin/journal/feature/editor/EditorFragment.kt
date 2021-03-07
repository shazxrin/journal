package io.github.kosumorin.journal.feature.editor

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.kosumorin.journal.R
import io.github.kosumorin.journal.ui.actionBar
import io.github.kosumorin.journal.feature.MainFragment
import io.github.kosumorin.journal.feature.editor.markdown.MarkdownToolbarAdapter
import io.github.kosumorin.journal.feature.editor.tag.EditorTagListFragment
import io.github.kosumorin.journal.ui.alert.AlertFragment
import io.github.kosumorin.journal.ui.alert.AlertViewModel
import io.github.kosumorin.journal.ui.alert.AlertResponse
import io.github.kosumorin.journal.ui.datetime.DatePickerDialogFragment
import io.github.kosumorin.journal.ui.datetime.DatePickerDialogViewModel
import io.github.kosumorin.journal.utils.datetime.DateTimeHelper
import io.github.kosumorin.journal.ui.datetime.TimePickerDialogFragment
import io.github.kosumorin.journal.ui.datetime.TimePickerDialogViewModel
import io.noties.markwon.Markwon
import kotlinx.android.synthetic.main.fragment_editor.*
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import java.lang.RuntimeException
import javax.inject.Inject

class EditorFragment() : MainFragment() {
    // Markdown.
    @Inject lateinit var markwon: Markwon
    private val markdownToolbarAdapter = MarkdownToolbarAdapter()

    // View models.
    private val editorViewModel: EditorViewModel by navGraphViewModels(R.id.editor_nav_graph) {
        viewModelFactory
    }

    private lateinit var datePickerDialogViewModel: DatePickerDialogViewModel
    private lateinit var timePickerDialogViewModel: TimePickerDialogViewModel
    private lateinit var alertViewModel: AlertViewModel

    private val args: EditorFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        datePickerDialogViewModel = getSharedViewModel()
        timePickerDialogViewModel = getSharedViewModel()
        alertViewModel = getSharedViewModel()

        // Check initialization state of the viewmodel.
        if (!editorViewModel.hasInit) {
            editorViewModel.init()

            // Load entry by id (view mode).
            val entryId = args.entryId
            if (entryId != null) {
                editorViewModel.isViewingMode = true

                editorViewModel.loadEntry(entryId)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Override back pressed behaviour to show unsaved changes dialog.
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (editorViewModel.isDirty) {
                    alertViewModel.title = getText(R.string.alert_editor_save_title)
                    alertViewModel.positiveButtonText = getText(R.string.alert_editor_save_positive)
                    alertViewModel.negativeButtonText = getText(R.string.alert_editor_save_negative)

                    editorViewModel.alertState = EditorAlertState.UNSAVE_CHANGES

                    AlertFragment().show(
                        childFragmentManager,
                        "SaveAlertFragment"
                    )
                }
                else {
                    findNavController().navigateUp()
                }
            }
        })
    }

    //region Viewmodel Binding
    private fun bindViewModelLiveData() {
        editorViewModel.dateTimeLiveData.observe(viewLifecycleOwner, Observer { value ->
            date.text = DateTimeHelper.formatDate(value)
            time.text = DateTimeHelper.formatTime(value)
        })

        datePickerDialogViewModel.userSelectedDateLiveData.consume(viewLifecycleOwner, Observer { value ->
            val dateTime = editorViewModel.dateTimeLiveData.value ?: LocalDateTime.now()
            editorViewModel.dateTimeLiveData.postValue(LocalDateTime.of(value, dateTime.toLocalTime()))

            editorViewModel.isDirty = true
        })

        timePickerDialogViewModel.userSelectedTimeLiveData.consume(viewLifecycleOwner, Observer { value ->
            val dateTime = editorViewModel.dateTimeLiveData.value ?: LocalDateTime.now()
            editorViewModel.dateTimeLiveData.postValue(LocalDateTime.of(dateTime.toLocalDate(), value))

            editorViewModel.isDirty = true
        })

        title.doAfterTextChanged { text ->
            if (editorViewModel.isViewingMode) {
                return@doAfterTextChanged
            }

            if (editorViewModel.titleLiveData.value != text.toString()) {
                editorViewModel.titleLiveData.postValue(text.toString())
                editorViewModel.isDirty = true
            }
        }

        content.doAfterTextChanged { text ->
            if (editorViewModel.isViewingMode) {
                return@doAfterTextChanged
            }

            if (editorViewModel.contentLiveData.value != text.toString()) {
                editorViewModel.contentLiveData.postValue(text.toString())
                editorViewModel.isDirty = true
            }
        }

        editorViewModel.saveEntryResultLiveData.consume(viewLifecycleOwner, Observer { status ->
            status
                .onSuccess {
                    Toast.makeText(context, getText(R.string.toast_editor_entry_saved), Toast.LENGTH_SHORT).show()

                    if (editorViewModel.currentEntryId == null) {
                        hideSoftKeyboard()

                        findNavController().navigateUp()
                    } else {
                        editorViewModel.isDirty = false

                        showPreviewingMode()
                    }
                }
                .onFailure {
                    Toast.makeText(context, getText(R.string.toast_editor_entry_save_error), Toast.LENGTH_SHORT).show()
                }
        })

        editorViewModel.loadEntryResultLiveData.consume(viewLifecycleOwner, Observer { status ->
            status
                .onSuccess {
                    val titleValue = editorViewModel.titleLiveData.value ?: ""
                    val contentValue = editorViewModel.contentLiveData.value ?: ""

                    title.setText(titleValue)
                    content.setText(if (editorViewModel.isViewingMode) markwon.toMarkdown(contentValue) else contentValue)
                }
                .onFailure {
                    Toast.makeText(context, getText(R.string.toast_editor_entry_load_error), Toast.LENGTH_SHORT).show()
                }
        })

        editorViewModel.deleteEntryResultLiveData.consume(viewLifecycleOwner, Observer { status ->
            status
                .onSuccess {
                    Toast.makeText(context, getText(R.string.toast_editor_entry_deleted), Toast.LENGTH_SHORT).show()

                    findNavController().navigateUp()
                }
                .onFailure {
                    Toast.makeText(context, getText(R.string.toast_editor_entry_delete_error), Toast.LENGTH_SHORT).show()
                }
        })

        alertViewModel.userSelectionLiveData.consume(viewLifecycleOwner, Observer { res ->
            when (editorViewModel.alertState) {
                EditorAlertState.UNSAVE_CHANGES -> {
                    when (res) {
                        AlertResponse.POSITIVE -> editorViewModel.saveEntry()
                        AlertResponse.NEGATIVE -> findNavController().navigateUp()
                    }
                }
                EditorAlertState.DELETE -> {
                    when (res) {
                        AlertResponse.POSITIVE -> editorViewModel.deleteEntry()
                    }
                }
                else -> {
                    throw RuntimeException("Check if editor alert state has been properly set!")
                }
            }

            alertViewModel.reset()
            editorViewModel.alertState = EditorAlertState.NONE
        })

        editorViewModel.tagStore.selectedTagsLiveData.observe(viewLifecycleOwner) { tagList ->
            tags.text = tagList.joinToString(", ") { tag -> tag.name }
        }
    }
    //endregion

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_editor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBottomAppBar()

        bindViewModelLiveData()

        // Setup viewing mode.
        if (editorViewModel.isViewingMode) {
            disableFields()
        }
        else {
            title.requestFocus()

            showSoftKeyboard()
        }

        // Setup date & time chips.
        date.setOnClickListener {
            datePickerDialogViewModel.showSelectedDate =
                editorViewModel.dateTimeLiveData.value?.toLocalDate() ?: LocalDate.now()
            DatePickerDialogFragment().show(requireFragmentManager(), "DatePickerDialogFragment")
        }
        time.setOnClickListener {
            timePickerDialogViewModel.showSelectedTime =
                editorViewModel.dateTimeLiveData.value?.toLocalTime() ?: LocalTime.now()
            TimePickerDialogFragment().show(childFragmentManager, "TimePickerDialogFragment")
        }

        // Setup empty space inside the scroll view.
        editor_scrollview.setOnTouchListener { v, event ->
            v.performClick()

            if (event.action == MotionEvent.ACTION_DOWN) {
                content.requestFocus()
            }

            // Don't consume the event; just intercepting events in this listener.
            return@setOnTouchListener false
        }

        // Setup markdown toolbar.
        markdown_toolbar.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        markdownToolbarAdapter.onToolItemClickHandler = ({ markdownToolItem ->
            if (content.selectionStart == content.selectionEnd) {
                val curPos = content.selectionStart

                var symbolToAdd = markdownToolItem.symbol
                if (markdownToolItem.hasSymbolTail) symbolToAdd += markdownToolItem.symbol
                val newCurPos = curPos + markdownToolItem.symbol.length

                content.text?.insert(curPos, symbolToAdd)
                content.setSelection(newCurPos)
            } else {
                val selectedStart = content.selectionStart
                val selectedEnd = content.selectionEnd

                content.text?.insert(selectedStart, markdownToolItem.symbol)
                if (markdownToolItem.hasSymbolTail) {
                    content.text?.insert(selectedEnd + markdownToolItem.symbol.length, markdownToolItem.symbol)

                    content.setSelection(selectedEnd + (markdownToolItem.symbol.length * 2))
                } else {
                    content.setSelection(selectedEnd + markdownToolItem.symbol.length)
                }
            }
        })
        markdown_toolbar.adapter = markdownToolbarAdapter
        markdown_toolbar.visibility = if (editorViewModel.isViewingMode) View.GONE else View.VISIBLE

        tags.setOnClickListener {
            EditorTagListFragment().show(
                childFragmentManager,
                "TagListFragment"
            )
        }
    }

    //region App Bar Options Menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        menu.clear()

        inflater.inflate(R.menu.menu_editor, menu)

        if (editorViewModel.isViewingMode) {
            menu.findItem(R.id.save).isVisible = editorViewModel.isDirty

            menu.findItem(R.id.preview).isVisible = false
            menu.findItem(R.id.edit).isVisible = true

            // Show delete icon only when entry exists
            menu.findItem(R.id.delete).isVisible = editorViewModel.currentEntryId != null
        }
        else {
            menu.findItem(R.id.preview).isVisible = true
            menu.findItem(R.id.edit).isVisible = false
            menu.findItem(R.id.delete).isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                if (editorViewModel.titleLiveData.value?.length == 0
                    || editorViewModel.contentLiveData.value?.length == 0) {
                    Toast.makeText(requireContext(), getText(R.string.toast_editor_entry_empty_error), Toast.LENGTH_LONG)
                        .show()
                } else {
                    editorViewModel.saveEntry()
                }
            }
            R.id.delete -> {
                alertViewModel.title = getText(R.string.alert_editor_delete_title)
                alertViewModel.positiveButtonText = getText(R.string.alert_editor_delete_positive)
                alertViewModel.negativeButtonText = getText(R.string.alert_editor_delete_negative)
                alertViewModel.dismissOnNegative = true

                editorViewModel.alertState = EditorAlertState.DELETE

                AlertFragment().show(
                    childFragmentManager,
                    "DeleteAlertFragment"
                )
            }
            R.id.preview -> {
                showPreviewingMode()
            }
            R.id.edit -> {
                showEditingMode()
            }
            android.R.id.home -> {
                if (!editorViewModel.isViewingMode) hideSoftKeyboard()

                activity?.onBackPressed()
            }
            else -> return super.onOptionsItemSelected(item)
        }

        return true
    }
    //endregion

    private fun setupBottomAppBar() {
        fabBottomAppBar?.hide()

        bottomAppBar?.performShow()
        bottomAppBar?.hideOnScroll = false

        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun disableFields() {
        title.isFocusable = false
        title.isFocusableInTouchMode = false
        content.isFocusable = false
        content.isFocusableInTouchMode = false
        date.isEnabled = false
        time.isEnabled = false
    }

    private fun enableFields() {
        title.isFocusable = true
        title.isFocusableInTouchMode = true
        content.isFocusable = true
        content.isFocusableInTouchMode = true
        date.isEnabled = true
        time.isEnabled = true
    }

    private fun showSoftKeyboard() {
        if (resources.configuration.keyboard == Configuration.KEYBOARD_NOKEYS) {
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(title, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun hideSoftKeyboard() {
        if (resources.configuration.keyboard == Configuration.KEYBOARD_NOKEYS) {
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
        }
    }

    private fun showPreviewingMode() {
        editorViewModel.isViewingMode = true

        // Hide markdown toolbar.
        markdown_toolbar.visibility = View.GONE

        // Enable markdown rendering
        content.setText(markwon.toMarkdown(editorViewModel.contentLiveData.value ?: ""))

        disableFields()

        hideSoftKeyboard()

        activity?.invalidateOptionsMenu()
        setupBottomAppBar()
    }

    private fun showEditingMode() {
        editorViewModel.isViewingMode = false

        // Show markdown toolbar.
        markdown_toolbar.visibility = View.VISIBLE

        // Disable markdown rendering.
        content.setText(editorViewModel.contentLiveData.value ?: "")

        enableFields()

        title.requestFocus()
        title.setSelection(title.text?.length ?: 0) // Set cursor at the back

        showSoftKeyboard()

        activity?.invalidateOptionsMenu()
        setupBottomAppBar()
    }
}
