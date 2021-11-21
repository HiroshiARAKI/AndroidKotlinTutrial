package tech.araki.smartmemo

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import tech.araki.smartmemo.data.Memo
import tech.araki.smartmemo.util.TimeUtil.toDatetimeString
import tech.araki.smartmemo.viewmodel.MemoDetailViewModel
import java.lang.ClassCastException

class MemoDetailFragment : Fragment(R.layout.fragment_memo_detail) {
    interface Listener {
        fun onDelete(memoTitle: String)
    }

    private val viewModel: MemoDetailViewModel by viewModels()
    private lateinit var listener: Listener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? Listener
            ?: throw ClassCastException("The parent activity needs to implement Listener")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val memo = requireArguments()[KEY_MEMO] as Memo
        Log.d(this::class.simpleName, "memo = $memo")

        val title = view.findViewById<TextView>(R.id.memo_detail_title)
        val date = view.findViewById<TextView>(R.id.memo_detail_date)
        val contents = view.findViewById<EditText>(R.id.memo_detail_contents)
        val expire = view.findViewById<TextView>(R.id.memo_detail_expire)
        val deleteButton = view.findViewById<Button>(R.id.memo_delete_button)

        title.text = memo.title
        date.text =
            getString(R.string.memo_detail_date_format, memo.updateTimeMillis.toDatetimeString())
        expire.text =
            getString(R.string.memo_detail_expire_format, memo.expireTimeMillis.toDatetimeString())
        contents.setText(memo.contents)
        deleteButton.setOnClickListener { onDeleteMemo(memo) }

        expire.setOnClickListener { showDatetimePicker() }
    }

    private fun onDeleteMemo(memo: Memo) {
        viewModel.deleteMemo(memo.id)
        listener.onDelete(memo.title)
        parentFragmentManager.popBackStack()
    }

    private fun showDatetimePicker() {
        DatetimePicker().show(parentFragmentManager, "datetime_picker")
    }

    companion object {
        private const val KEY_MEMO = "memo"

        /** [MemoDetailFragment]の引数ありインスタンスを生成する */
        fun newInstance(memo: Memo): MemoDetailFragment {
            return MemoDetailFragment().apply {
                arguments = bundleOf(
                    KEY_MEMO to memo
                )
            }
        }
    }
}