package tech.araki.smartmemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tech.araki.smartmemo.data.Memo
import tech.araki.smartmemo.data.Sort
import tech.araki.smartmemo.util.hideSoftwareKeyboard
import tech.araki.smartmemo.util.makeToast
import tech.araki.smartmemo.viewmodel.MainViewModel

class MainActivity
    : AppCompatActivity(), NewMemoFragment.Listener, MemoDetailFragment.Listener {

    private val viewModel: MainViewModel by viewModels()

    // sortSpinnerのアイテムが選択された時の挙動
    private val sortAdapterListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            Log.d(this::class.simpleName, "onItemSelected: position=$position, id=$id")
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addButton: Button = findViewById(R.id.add_button)

        val recyclerView: RecyclerView = findViewById(R.id.main_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MainAdapter(::onItemClick)
        recyclerView.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )

        // ソートのセレクターを作成する
        // レイアウトは既存のものを使うことにする
        val sortSpinner: Spinner = findViewById(R.id.sort_spinner)
        val sortAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item)
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // 全選択肢を追加
        Sort.values().forEach {
            sortAdapter.add(getString(it.id))
        }
        // Spinnerの設定
        sortSpinner.adapter = sortAdapter
        sortSpinner.onItemSelectedListener = sortAdapterListener

        addButton.setOnClickListener {
            supportFragmentManager.beginTransaction().run {
                add(R.id.main_container, NewMemoFragment())
                addToBackStack(null)
                commit()
            }
        }
        viewModel.memoItems.observe(this) {
            (recyclerView.adapter as MainAdapter).setMemoItems(it)
        }
        viewModel.loadMemoItems()
    }

    override fun onDismiss(memoTitle: String) {
        hideSoftwareKeyboard(R.id.main_container)
        viewModel.loadMemoItems()
        makeToast(this, getString(R.string.new_memo_added_text, memoTitle))
    }

    override fun onUpdate(memo: Memo) {
        hideSoftwareKeyboard(R.id.main_container)
        viewModel.updateMemo(memo)
    }

    override fun onDelete(memoTitle: String) {
        hideSoftwareKeyboard(R.id.main_container)
        viewModel.loadMemoItems()
        makeToast(this, getString(R.string.memo_delete_text, memoTitle))
    }

    // RecyclerViewのアイテムがクリックされたときの処理
    private fun onItemClick(memo: Memo) {
        supportFragmentManager.beginTransaction().run {
            add(R.id.main_container, MemoDetailFragment.newInstance(memo))
            addToBackStack(null)
            commit()
        }
    }
}