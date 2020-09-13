package io.github.icedshytea.journal.feature.editor

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import io.github.icedshytea.journal.R
import io.github.icedshytea.journal.databinding.FragmentEditorBinding
import io.github.icedshytea.journal.feature.MainFragment
import kotlinx.android.synthetic.main.fragment_editor.*

class EditorFragment : MainFragment() {
    private lateinit var editorViewModel: EditorViewModel

    private val args: EditorFragmentArgs by navArgs()
    private var isViewingMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        editorViewModel = initViewModel()
        isViewingMode = args.entryId != -1

        // Load entry by id (view mode).
        if (isViewingMode) {
            editorViewModel.load(args.entryId)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Bind viewmodel fields.
        val binding = FragmentEditorBinding.inflate(inflater, container, false)
        binding.viewModel = editorViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // Bind viewmodel statuses.
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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBottomAppBar()

        if (isViewingMode) {
            disableFields()
        }
        else {
            title.requestFocus()

            showSoftKeyboard()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        menu.clear()

        if (isViewingMode) {
            inflater.inflate(R.menu.menu_editor_viewer, menu)
        }
        else {
            inflater.inflate(R.menu.menu_editor, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> editorViewModel.save()
            R.id.delete -> editorViewModel.delete()
            R.id.edit -> {
                if (isViewingMode) {
                    isViewingMode = false

                    enableFields()

                    title.requestFocus()
                    title.setSelection(title.text?.length ?: 0) // Set cursor at the back

                    showSoftKeyboard()

                    activity?.invalidateOptionsMenu()
                    setupBottomAppBar()
                }
            }
            android.R.id.home -> {
                if (!isViewingMode) hideSoftKeyboard()

                activity?.onBackPressed()
            }

            else -> return super.onOptionsItemSelected(item)
        }

        return true
    }

    private fun setupBottomAppBar() {
        fabBottomAppBar?.hide()

        bottomAppBar?.performShow()
        bottomAppBar?.hideOnScroll = false
    }

    private fun disableFields() {
        title.isFocusable = false
        title.isFocusableInTouchMode = false
        content.isFocusable = false
        content.isFocusableInTouchMode = false
    }

    private fun enableFields() {
        title.isFocusable = true
        title.isFocusableInTouchMode = true
        content.isFocusable = true
        content.isFocusableInTouchMode = true
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
