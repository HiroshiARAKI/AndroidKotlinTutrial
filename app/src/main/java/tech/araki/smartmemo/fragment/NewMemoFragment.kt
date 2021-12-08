package tech.araki.smartmemo.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import tech.araki.smartmemo.R
import tech.araki.smartmemo.viewmodel.NewMemoViewModel
import java.lang.ClassCastException

class NewMemoFragment : Fragment(R.layout.fragment_new_memo) {
    interface Listener {
        fun onDismiss(memoTitle: String)
    }

    private val viewModel: NewMemoViewModel by viewModels()

    private lateinit var listener: Listener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // ここでリスナーをセットしておく。もしActivityがListenerを実装していなければ例外を出してアプリを落とす。
        listener = context as? Listener
            ?: throw ClassCastException("The parent activity needs to implement OnDismissListener")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.new_memo_add_button).setOnClickListener {
            val title = view.findViewById<EditText>(R.id.new_memo_title).text.toString()
            val contents = view.findViewById<EditText>(R.id.new_memo_contents).text.toString()

            // タイトルが空ならばエラー処理
            // 本体ならばUI上で何かしら文言を表示するが割愛
            if (title.isEmpty())
                return@setOnClickListener

            viewModel.registerMemo(title, contents)
        }

        // DB挿入されたらFragmentを閉じる
        viewModel.insertEvent.observe(viewLifecycleOwner) { dismiss(it) }
    }

    private fun dismiss(memoTitle: String) {
        listener.onDismiss(memoTitle)
        parentFragmentManager.popBackStack()
    }
}