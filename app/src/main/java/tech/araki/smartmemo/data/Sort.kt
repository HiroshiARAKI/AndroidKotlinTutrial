package tech.araki.smartmemo.data

import androidx.annotation.StringRes
import tech.araki.smartmemo.R

/**
 * ソートのアイテムを管理する列挙型
 */
enum class Sort(@StringRes val id: Int) {
    ID(R.string.sort_id),
    UPDATE(R.string.sort_update),
    EXPIRE(R.string.sort_expire);

    /** [memoItems]にソートを適用する */
    fun applyTo(memoItems: List<Memo>) =
        when(this) {
            ID ->  memoItems.sortedBy { it.id }  // ID昇順=追加順
            UPDATE ->  memoItems.sortedByDescending { it.updateTimeMillis }  // 更新日降順
            EXPIRE ->  memoItems.sortedByDescending { it.expireTimeMillis }  // 破棄日降順
        }

    companion object {
        fun getById(id: Long) = when(id) {
            0L -> ID
            1L -> UPDATE
            2L -> EXPIRE
            else -> ID
        }
    }
}