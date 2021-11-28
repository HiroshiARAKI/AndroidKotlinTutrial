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
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tech.araki.smartmemo.data.Memo
import tech.araki.smartmemo.data.Sort
import tech.araki.smartmemo.util.PREFERENCES_KEY_SORT_SETTING
import tech.araki.smartmemo.util.dataStore
import tech.araki.smartmemo.util.hideSoftwareKeyboard
import tech.araki.smartmemo.util.makeToast
import tech.araki.smartmemo.viewmodel.MainViewModel
import java.io.IOException


class MainActivity
    : AppCompatActivity(), NewMemoFragment.Listener, MemoDetailFragment.Listener {

    private val viewModel: MainViewModel by viewModels()

    // ソート設定に関するFlow
    private val sortSettingFlow by lazy {
        dataStore.data
            .catch { exception ->
                if (exception is IOException) emit(emptyPreferences())
                else throw exception
            }.map { preferences ->
                preferences[PREFERENCES_KEY_SORT_SETTING] ?: 0  // デフォルトは0にしておく
            }
    }

    // sortSpinnerのアイテムが選択された時の挙動
    private val sortAdapterListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            Log.d(this::class.simpleName, "onItemSelected: position=$position, id=$id")
            // DataStoreに、選択されているposition (ID)を非同期で格納しておく
            lifecycleScope.launch(Dispatchers.IO) {
                dataStore.edit { settings ->
                    settings[PREFERENCES_KEY_SORT_SETTING] = position
                }
            }
            viewModel.sortBy(Sort.getById(id))
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            viewModel.sortBy(Sort.ID)
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
        // DataStoreからデータを非同期で取得する
        lifecycleScope.launch(Dispatchers.Main) {
            sortSpinner.setSelection(getFirstSortSetting())
        }

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

    // 初期ソート設定を読み込む
    // NOTE: DataStoreへのアクセスはIOスレッドで行うようにする
    private suspend fun getFirstSortSetting() =
        withContext(Dispatchers.IO) { sortSettingFlow.first() }
}