package io.github.icedshytea.journal.feature.editor

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import io.github.icedshytea.journal.R
import io.github.icedshytea.journal.common.ui.actionBar
import io.github.icedshytea.journal.feature.MainFragment
import io.github.icedshytea.journal.utils.alert.BottomAlertDialogFragment
import io.github.icedshytea.journal.utils.datetime.DatePickerDialogFragment
import io.github.icedshytea.journal.utils.datetime.DateTimeHelper
import io.github.icedshytea.journal.utils.datetime.TimePickerDialogFragment
import io.noties.markwon.Markwon
import kotlinx.android.synthetic.main.fragment_editor.*
import javax.inject.Inject

class EditorFragment() : MainFragment(),
    DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
{
    @Inject
    lateinit var markwon: Markwon

    private lateinit var editorViewModel: EditorViewModel

    private val args: EditorFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        editorViewModel = initViewModel()

        if (!editorViewModel.hasInit) {
            editorViewModel.isViewingMode = args.entryId != -1

            // Load entry by id (view mode).
            if (editorViewModel.isViewingMode) {
                editorViewModel.load(args.entryId)
            }

            editorViewModel.hasInit = true
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Override back pressed behaviour to show unsaved changes dialog.
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (editorViewModel.isDirty) {
                    BottomAlertDialogFragment(
                        "Do you want to save or discard changes?",
                        "Save",
                        "Discard",
                        DialogInterface.OnClickListener { _, id ->
                            when (id) {
                                R.id.positive_button -> editorViewModel.save()
                                R.id.negative_button -> findNavController().navigateUp()
                            }
                        }
                    ).show(childFragmentManager, "SaveBottomAlertDialogFragment")
                }
                else {
                    findNavController().navigateUp()
                }
            }
        })
    }

    //region Viewmodel Binding
    private fun bindViewModelFields() {
        editorViewModel.dateTimeField.observe(viewLifecycleOwner, Observer { value ->
            date.text = DateTimeHelper.formatDate(value)
            time.text = DateTimeHelper.formatTime(value)
        })

        title.doAfterTextChanged { text ->
            if (editorViewModel.isViewingMode) {
                return@doAfterTextChanged
            }

            if (editorViewModel.titleField.value != text.toString()) {
                editorViewModel.titleField.postValue(text.toString())
                editorViewModel.isDirty = true
            }
        }

        content.doAfterTextChanged { text ->
            if (editorViewModel.isViewingMode) {
                return@doAfterTextChanged
            }

            if (editorViewModel.contentField.value != text.toString()) {
                editorViewModel.contentField.postValue(text.toString())
                editorViewModel.isDirty = true
            }
        }
    }

    private fun bindViewModelStatuses() {
        editorViewModel.saveActionResult.consume(viewLifecycleOwner, Observer { status ->
            status
                .onSuccess {
                    Toast.makeText(context, "Entry saved!", Toast.LENGTH_SHORT).show()

                    hideSoftKeyboard()

                    findNavController().navigateUp()
                }
                .onFailure {
                    Toast.makeText(context, "Error occurred while saving entry", Toast.LENGTH_SHORT).show()
                }
        })

        editorViewModel.loadActionResult.consume(viewLifecycleOwner, Observer { status ->
            status
                .onSuccess {
                    val titleValue = editorViewModel.titleField.value ?: ""
                    val contentValue = editorViewModel.contentField.value ?: ""

                    title.setText(titleValue)
                    content.setText(if (editorViewModel.isViewingMode) markwon.toMarkdown(contentValue) else contentValue)
                }
                .onFailure {
                    Toast.makeText(context, "Failed to load entry", Toast.LENGTH_SHORT).show()
                }
        })

        editorViewModel.deleteActionResult.consume(viewLifecycleOwner, Observer { status ->
            status
                .onSuccess {
                    Toast.makeText(context, "Entry deleted!", Toast.LENGTH_SHORT).show()

                    findNavController().navigateUp()
                }
                .onFailure {
                    Toast.makeText(context, "Error occurred while deleting entry", Toast.LENGTH_SHORT).show()
                }
        })
    }
    //endregion

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_editor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViewModelFields()
        bindViewModelStatuses()

        setupBottomAppBar()

        // Setup viewing mode.
        if (editorViewModel.isViewingMode) {
            disableFields()
        }
        else {
            title.requestFocus()

            showSoftKeyboard()
        }

        // Setup date & time chips.
        date.setOnClickListener { handleDateChipSelected() }
        time.setOnClickListener { handleTimeChipSelected() }

        // Setup empty space inside the scroll view.
        editor_scrollview.setOnTouchListener { v, event ->
            v.performClick()

            if (event.action == MotionEvent.ACTION_DOWN) {
                content.requestFocus()
            }

            // Don't consume the event; just intercepting events in this listener.
            return@setOnTouchListener false
        }
    }

    //region Date & Time chips handling
    private fun handleDateChipSelected() {
        val datePicker = DatePickerDialogFragment(this, editorViewModel.dateTimeField.value!!.toLocalDate())
        datePicker.show(childFragmentManager, "DatePickerDialogFragment")
    }

    private fun handleTimeChipSelected() {
        // TODO: LiveField's guarantees that value will never be null since it is init on creation. Need to fix LiveField API.
        val timePicker = TimePickerDialogFragment(this, editorViewModel.dateTimeField.value!!.toLocalTime())
        timePicker.show(childFragmentManager, "TimePickerDialogFragment")
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        // TODO: LiveField's guarantees that value will never be null since it is init on creation. Need to fix LiveField API.
        val oldDateTime = editorViewModel.dateTimeField.value!!
        // Java's Calendar API's months start from 0 so we need to +- accordingly with 310's months.
        val newDateTime = oldDateTime.withDayOfMonth(dayOfMonth).withMonth(month + 1).withYear(year);

        editorViewModel.dateTimeField.postValue(newDateTime)
        editorViewModel.isDirty = true
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val oldDateTime = editorViewModel.dateTimeField.value!!
        val newDateTime = oldDateTime.withHour(hourOfDay).withMinute(minute)

        editorViewModel.dateTimeField.postValue(newDateTime)
        editorViewModel.isDirty = true
    }
    //endregion

    //region App Bar Options Menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        menu.clear()

        inflater.inflate(R.menu.menu_editor, menu)

        if (editorViewModel.isViewingMode) {
            if (!editorViewModel.isDirty) {
                menu.findItem(R.id.save).isVisible = false
            }

            menu.findItem(R.id.preview).isVisible = false
            menu.findItem(R.id.edit).isVisible = true
        }
        else {
            menu.findItem(R.id.preview).isVisible = true
            menu.findItem(R.id.edit).isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> editorViewModel.save()
            R.id.delete -> {
                BottomAlertDialogFragment(
                    "Are you sure you want to delete?",
                    "Yes",
                    "No",
                    DialogInterface.OnClickListener { dialog, id ->
                        when (id) {
                            R.id.positive_button -> editorViewModel.delete()
                            R.id.negative_button -> dialog.cancel()
                        }
                    }
                ).show(childFragmentManager, "DeleteBottomAlertDialogFragment")
            }
            R.id.preview -> {
                editorViewModel.isViewingMode = true

                // Enable markdown rendering
                content.setText(markwon.toMarkdown(editorViewModel.contentField.value ?: ""))

                disableFields()

                hideSoftKeyboard()

                activity?.invalidateOptionsMenu()
                setupBottomAppBar()
            }
            R.id.edit -> {
                editorViewModel.isViewingMode = false

                // Disable markdown rendering.
                content.setText(editorViewModel.contentField.value ?: "")

                enableFields()

                title.requestFocus()
                title.setSelection(title.text?.length ?: 0) // Set cursor at the back

                showSoftKeyboard()

                activity?.invalidateOptionsMenu()
                setupBottomAppBar()
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
}
