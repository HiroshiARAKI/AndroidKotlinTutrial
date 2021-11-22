package tech.araki.smartmemo.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.concurrent.TimeUnit

/**
 * 単一のメモを表すデータクラス
 * @param id ユニークなID
 * @param title メモのタイトル
 * @param contents メモの内容
 * @param createdTimeMillis メモ作成日
 * @param updateTimeMillis メモ更新日
 * @param expireTimeMillis メモの締め切り (廃棄日)
 */
@Entity
data class Memo(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = TITLE) val title: String,
    @ColumnInfo(name = CONTENTS) val contents: String,
    @ColumnInfo(name = CREATED_TIME) val createdTimeMillis: Long,
    @ColumnInfo(name = UPDATE_TIME) val updateTimeMillis: Long,
    @ColumnInfo(name = EXPIRE_TIME) val expireTimeMillis: Long
) : Serializable {
    companion object {
        const val TITLE = "title"
        const val CONTENTS = "contents"
        const val CREATED_TIME = "created_time_millis"
        const val UPDATE_TIME = "update_time_millis"
        const val EXPIRE_TIME = "expire_time_millis"

        /**
         * creates 10 Fake Memos
         */
        fun createFakes(): List<Memo> {
            val now = System.currentTimeMillis()
            return List(10) {
                Memo(
                    id = it,
                    title = "Memo $it",
                    contents = "memo description",
                    createdTimeMillis = now,
                    updateTimeMillis = now,
                    expireTimeMillis = now + TimeUnit.DAYS.toMillis(7)
                )
            }
        }
    }
}