package tech.araki.smartmemo.repository

import android.content.Context
import android.util.Log
import androidx.room.Room
import tech.araki.smartmemo.data.Memo
import tech.araki.smartmemo.model.MemoDatabase

class MemoRepository(context: Context) {
    private val dao = Room.databaseBuilder(
        context,
        MemoDatabase::class.java,
        DATABASE_NAME
    ).build().memoDao()

    /**
     * MemoをDBに挿入する
     * @param title Memoのタイトル
     * @param contents Memoの内容
     * @param expiredDuration 廃棄するまでの期間
     */
    fun insertMemo(title: String, contents: String, expiredDuration: Long) {
        val currentTime = System.currentTimeMillis()
        dao.insertMemo(
            Memo(
                id = 0,
                title = title,
                contents = contents,
                createdTimeMillis = currentTime,
                updateTimeMillis = currentTime,
                expireTimeMillis = currentTime + expiredDuration
            )
        )
    }

    /**
     * 全てのMemoを取得する
     */
    fun fetchAllMemo(): List<Memo> {
        val memoItems = dao.fetchAll()
        Log.d(this::class.simpleName, "fetched Memo Item = $memoItems")
        return memoItems
    }

    /**
     * メモを削除する
     */
    fun deleteMemo(id: Int) = dao.deleteMemo(id)

    /**
     * メモを更新する。
     */
    fun updateMemo(memo: Memo) = dao.updateMemo(memo)

    companion object {
        const val DATABASE_NAME = "memo"
    }
}