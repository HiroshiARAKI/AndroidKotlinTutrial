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

    companion object {
        fun getById(id: Long) = when(id) {
            0L -> ID
            1L -> UPDATE
            2L -> EXPIRE
            else -> ID
        }
    }
}