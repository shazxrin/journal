package io.github.icedshytea.journal.utils.ui.alert

import androidx.lifecycle.ViewModel
import io.github.icedshytea.journal.utils.data.ConsumableLiveData
import javax.inject.Inject

enum class AlertBottomSheetResponse { POSITIVE, NEGATIVE }

class AlertBottomSheetDialogViewModel @Inject constructor() : ViewModel() {
    // Settings.
    var title: String = ""
    var positiveButtonText: String = ""
    var negativeButtonText: String = ""
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
