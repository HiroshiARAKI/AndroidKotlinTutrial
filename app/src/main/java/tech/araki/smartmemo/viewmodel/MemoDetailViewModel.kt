package tech.araki.smartmemo.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemoDetailViewModel(app: Application) : MemoViewModel(app) {

    fun deleteMemo(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        memoRepository.deleteMemo(id)
    }
}