package tech.araki.smartmemo.fragment

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
import tech.araki.smartmemo.dialog.DatetimePicker
import tech.araki.smartmemo.R
import tech.araki.smartmemo.data.Memo
import tech.araki.smartmemo.util.TimeUtil.toDatetimeString
import tech.araki.smartmemo.util.makeToast
import tech.araki.smartmemo.viewmodel.MemoDetailViewModel
import java.lang.ClassCastException

class MemoDetailFragment : Fragment(R.layout.fragment_memo_detail), DatetimePicker.Listener {
    interface Listener {
        fun onDelete(memoTitle: String)
        fun onUpdate(memo: Memo)
    }

    private val viewModel: MemoDetailViewModel by viewModels()
    private lateinit var listener: Listener

    private lateinit var memo: Memo
    private lateinit var expire: TextView
    private lateinit var contents: EditText

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? Listener
            ?: throw ClassCastException("The parent activity needs to implement Listener")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        memo = requireArguments()[KEY_MEMO] as Memo
        Log.d(this::class.simpleName, "memo = $memo")

        val title = view.findViewById<TextView>(R.id.memo_detail_title)
        val date = view.findViewById<TextView>(R.id.memo_detail_date)
        val deleteButton = view.findViewById<Button>(R.id.memo_delete_button)
        contents = view.findViewById(R.id.memo_detail_contents)
        expire = view.findViewById(R.id.memo_detail_expire)

        title.text = memo.title
        date.text =
            getString(R.string.memo_detail_date_format, memo.updateTimeMillis.toDatetimeString())
        expire.text =
            getString(R.string.memo_detail_expire_format, memo.expireTimeMillis.toDatetimeString())
        contents.setText(memo.contents)
        deleteButton.setOnClickListener { onDeleteMemo(memo) }

        expire.setOnClickListener { showDatetimePicker(memo.expireTimeMillis) }

        viewModel.updateExpireFailEvent.observe(this) {
            makeToast(requireContext(), getString(R.string.datetime_fail))
        }
    }

    /** Fragmentが破棄される際にMemoを更新しておく */
    override fun onDestroy() {
        super.onDestroy()
        // Memoコンテンツは直接EditTextから取得する
        memo = memo.copy(
            contents = contents.text.toString(),
            updateTimeMillis = System.currentTimeMillis()
        )
        // [重要！]
        // MemoDetailViewModelのスコープでDBアクセス (Memo更新) をしてしまうと、
        // Fragment破棄と同時に処理がKillされてしまうので、上位のMainActivityにDBアクセスは任せる。
        listener.onUpdate(memo)
    }

    override fun onDateTimeChanged(dateTimeWrapper: DatetimePicker.DateTimeWrapper) {
        Log.d(this::class.simpleName, "datetime is changed as $dateTimeWrapper")

        memo = memo.copy(expireTimeMillis = viewModel.getNewExpireDateTime(memo, dateTimeWrapper))
        expire.text =
            getString(R.string.memo_detail_expire_format, memo.expireTimeMillis.toDatetimeString())
    }

    private fun onDeleteMemo(memo: Memo) {
        viewModel.deleteMemo(memo.id)
        listener.onDelete(memo.title)
        parentFragmentManager.popBackStack()
    }

    private fun showDatetimePicker(expiredTime: Long) {
        DatetimePicker.newInstance(expiredTime, this)
            .show(parentFragmentManager, "datetime_picker")
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