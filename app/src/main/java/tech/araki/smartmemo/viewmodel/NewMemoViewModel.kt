package tech.araki.smartmemo.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class NewMemoViewModel(app: Application) : MemoViewModel(app) {

    private val _insertEvent = MutableLiveData<String>()
    /** DB挿入イベントLiveData */
    val insertEvent: LiveData<String> = _insertEvent

    /**
     * Memoを登録する
     */
    fun registerMemo(
        title: String,
        contents: String,
        expireDuration: Long = TimeUnit.DAYS.toMillis(7)) {
        viewModelScope.launch(Dispatchers.IO) {
            memoRepository.insertMemo(title, contents, expireDuration)
            _insertEvent.postValue(title)
        }
    }
}
