package tech.araki.smartmemo

import android.app.Application
import tech.araki.smartmemo.repository.MemoRepository

class App : Application() {
    /**
     * [MemoRepository]のインスタンス
     */
    val memoRepository: MemoRepository by lazy { MemoRepository(this) }
}