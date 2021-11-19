package tech.araki.smartmemo.model

import androidx.room.*
import tech.araki.smartmemo.data.Memo

@Dao
interface MemoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMemo(memo: Memo)

    @Query("SELECT * FROM memo")
    fun fetchAll(): List<Memo>

    @Delete
    fun deleteMemo(memo: Memo)
}