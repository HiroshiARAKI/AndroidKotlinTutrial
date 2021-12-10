package tech.araki.smartmemo

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tech.araki.smartmemo.data.Memo
import tech.araki.smartmemo.model.MemoDao
import tech.araki.smartmemo.model.MemoDatabase

@RunWith(AndroidJUnit4::class)
class MemoDaoTest {
    private lateinit var dao: MemoDao
    private lateinit var db: MemoDatabase

    @Before  // テスト前に実行される
    fun setup() {
        // モックではないContextを取得
        val context = ApplicationProvider.getApplicationContext<Context>()
        // メモリ上でDBを作成 = 一時的な仮DB
        db = Room.inMemoryDatabaseBuilder(
            context,
            MemoDatabase::class.java
        ).build()
        dao = db.memoDao()
    }

    @Test
    fun insertMemoTest() {
        MEMO_ITEMS.forEach {
            dao.insertMemo(it)
        }

        val result = dao.fetchAll()

        assertThat(result.map { it.title }).isEqualTo(MEMO_TITLES)
    }

    @After  // テスト後に実行される
    fun tearDown() {
        db.close()
    }

    companion object {
        // 仮のメモタイトル
        private val MEMO_TITLES = List(5) { "Sample Memo $it" }
        // 仮のメモ
        private val MEMO_ITEMS = MEMO_TITLES.map { createMemo(it) }

        private fun createMemo(title: String) = Memo(
            id = 0,
            title = title,
            contents = "",
            createdTimeMillis = 0,
            updateTimeMillis = 0,
            expireTimeMillis = 0
        )
    }
}