package tech.araki.smartmemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tech.araki.smartmemo.data.Memo
import tech.araki.smartmemo.util.hideSoftwareKeyboard
import tech.araki.smartmemo.util.makeToast
import tech.araki.smartmemo.viewmodel.MainViewModel

class MainActivity
    : AppCompatActivity(), NewMemoFragment.Listener, MemoDetailFragment.Listener {

    private val viewModel: MainViewModel by viewModels()

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