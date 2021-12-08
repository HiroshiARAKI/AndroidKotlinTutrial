package tech.araki.smartmemo.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.araki.smartmemo.dialog.DatetimePicker
import tech.araki.smartmemo.data.Memo
import tech.araki.smartmemo.util.TimeUtil.toZonedDateTime

class MemoDetailViewModel(app: Application) : MemoViewModel(app) {

    private val _updateExpireFailEvent = MutableLiveData<Unit>()

    /** 破棄日の更新が失敗した時のイベント */
    val updateExpireFailEvent: LiveData<Unit>  = _updateExpireFailEvent

    /**
     * MemoをIDをから削除する。
     * @param id
     */
    fun deleteMemo(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        memoRepository.deleteMemo(id)
    }

    /**
     * メモの新しい破棄日を取得する。
     * 現在の時刻より前の日付が選択されている場合は更新を行われずに[updateExpireFailEvent]を通知する。
     * @param memo
     * @param targetDateTime 更新対象の[DatetimePicker.DateTimeWrapper]
     */
    fun getNewExpireDateTime(
        memo: Memo,
        targetDateTime: DatetimePicker.DateTimeWrapper
    ): Long {
        val originalExpireTime = memo.expireTimeMillis
        val now = System.currentTimeMillis()
        val newExpireTime = targetDateTime.toTimeMillis(originalExpireTime.toZonedDateTime())

        if (now > newExpireTime) {
            _updateExpireFailEvent.postValue(Unit)
            return memo.expireTimeMillis
        }
        return newExpireTime
    }
}