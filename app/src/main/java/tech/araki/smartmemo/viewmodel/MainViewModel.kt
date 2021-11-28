package tech.araki.smartmemo.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tech.araki.smartmemo.data.Memo
import tech.araki.smartmemo.data.Sort

class MainViewModel(app: Application) : MemoViewModel(app) {

    /** ViewModel内で扱うミュータブルなLiveData */
    private val _memoItems = MutableLiveData<List<Memo>>()

    // 現在のソート方法
    private var currentSorting: Sort? = null

    /** 外部公開用のイミュータブルなLiveData */
    val memoItems: LiveData<List<Memo>> = _memoItems

    /** メモリストを読み込む */
    fun loadMemoItems() {
        viewModelScope.launch(Dispatchers.IO) {
            _memoItems.postValue(memoRepository.fetchAllMemo())
        }
    }

    /**
     * Memoを更新する。
     */
    fun updateMemo(memo: Memo) = viewModelScope.launch(Dispatchers.IO) {
        memoRepository.updateMemo(memo)
        val items = memoRepository.fetchAllMemo()
        _memoItems.postValue(
            currentSorting?.applyTo(items) ?: items
        )
    }

    /**
     * 指定されたソート方法適用する
     */
    fun sortBy(sort: Sort) {
        currentSorting = sort
        viewModelScope.launch(Dispatchers.Main) {
            val items = _memoItems.value ?: withContext(Dispatchers.IO) {
                memoRepository.fetchAllMemo()  // もしまだデータフェッチが行われていない場合は読み込みを行う
            }
            _memoItems.postValue(sort.applyTo(items))
        }
    }
}