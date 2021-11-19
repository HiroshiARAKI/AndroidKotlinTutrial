package tech.araki.smartmemo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import tech.araki.smartmemo.App

/**
 * Memoアプリで使用する独自[ViewModel]クラス。
 */
abstract class MemoViewModel(application: Application) : AndroidViewModel(application) {
    protected val memoRepository = (application as App).memoRepository
}