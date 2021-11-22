package tech.araki.smartmemo.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tech.araki.smartmemo.data.Memo

class MainViewModel(app: Application) : MemoViewModel(app) {

    /** ViewModel内で扱うミュータブルなLiveData */
    private val _memoItems = MutableLiveData<List<Memo>>()

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
        _memoItems.postValue(memoRepository.fetchAllMemo())
    }
}