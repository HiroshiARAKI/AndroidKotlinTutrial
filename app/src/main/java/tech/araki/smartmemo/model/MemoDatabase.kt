package tech.araki.smartmemo.model

import androidx.room.Database
import androidx.room.RoomDatabase
import tech.araki.smartmemo.data.Memo

@Database(entities = [Memo::class], version = 1)
abstract class MemoDatabase : RoomDatabase() {
    abstract fun memoDao(): MemoDao
}