package tech.araki.smartmemo

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class DatetimePicker : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Viewのレイアウトを読み込む
        val view = View.inflate(requireContext(), R.layout.dialog_datetime_picker, null)
        // AlertDialogをベースにDialog生成
        return AlertDialog.Builder(requireContext()).create().apply {
            setView(view)
        }
    }
}