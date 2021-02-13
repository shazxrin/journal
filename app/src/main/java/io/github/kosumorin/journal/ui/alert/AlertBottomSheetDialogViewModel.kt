package io.github.kosumorin.journal.ui.alert

import androidx.lifecycle.ViewModel
import io.github.kosumorin.journal.utils.data.ConsumableLiveData
import javax.inject.Inject

enum class AlertBottomSheetResponse { POSITIVE, NEGATIVE }

class AlertBottomSheetDialogViewModel @Inject constructor() : ViewModel() {
    // Settings.
    var title: CharSequence = ""
    var positiveButtonText: CharSequence = ""
    var negativeButtonText: CharSequence = ""
    var dismissOnNegative: Boolean = false

    // Live Data.
    val userSelectionLiveData = ConsumableLiveData<AlertBottomSheetResponse>()

    // Action.
    fun reset() {
        title = ""
        positiveButtonText = ""
        negativeButtonText = ""
        dismissOnNegative = false
    }
}
