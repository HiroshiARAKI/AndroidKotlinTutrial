package tech.araki.smartmemo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import tech.araki.smartmemo.data.Memo
import tech.araki.smartmemo.dialog.DatetimePicker
import tech.araki.smartmemo.viewmodel.MemoDetailViewModel
import java.time.Instant
import java.time.ZoneId

class MemoDetailViewModelTest {
    // 内部でLiveDataを扱う場合に下記の実行ルールを設定する必要がある
    // Ruleは必ずpublicにする
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // App をモック化する
    private val mockApp: App = mockk(relaxed = true)
    // モック化されたAppを注入してインスタンスを生成
    private val mockViewModel = MemoDetailViewModel(app = mockApp)

    /** 通常ケース */
    @Test
    fun `getNewExpireDateTime - normal case`() {
        val newExpiredTime = mockViewModel.getNewExpireDateTime(MEMO, DATE_WRAPPER_21000101_0000)
        assertThat(newExpiredTime).isEqualTo(TIME_21000101_0000)
    }

    /** 新しい破棄日が過去なために起き変わらないケース */
    @Test
    fun `getNewExpireDateTime - not replaced new expired time`() {
        val newExpiredTime = mockViewModel.getNewExpireDateTime(MEMO, DATE_WRAPPER_20000101_0000)
        assertThat(newExpiredTime).isEqualTo(TIME_20211209_1700)
    }

    companion object {
        private const val TIME_20000101_0000 = 946652400000L  // 2000/01/01 00:00
        private const val TIME_20211209_1700 = 1639036800000L  // 2021/12/09 17:00
        private const val TIME_21000101_0000 = 4102412400000L  // 2100/01/01 00:00

        private val DATETIME_20000101_0000 = Instant.ofEpochMilli(TIME_20000101_0000).atZone(ZoneId.systemDefault())
        private val DATETIME_21000101_0000 = Instant.ofEpochMilli(TIME_21000101_0000).atZone(ZoneId.systemDefault())

        private val MEMO = Memo(0, "", "", 0, 0, TIME_20211209_1700)

        private val DATE_WRAPPER_20000101_0000 = DatetimePicker.DateTimeWrapper(
            year = DATETIME_20000101_0000.year,
            month = DATETIME_20000101_0000.monthValue,
            dayOfMonth = DATETIME_20000101_0000.dayOfMonth,
            hour = DATETIME_20000101_0000.hour,
            minute = DATETIME_20000101_0000.minute
        )

        private val DATE_WRAPPER_21000101_0000 = DatetimePicker.DateTimeWrapper(
            year = DATETIME_21000101_0000.year,
            month = DATETIME_21000101_0000.monthValue,
            dayOfMonth = DATETIME_21000101_0000.dayOfMonth,
            hour = DATETIME_21000101_0000.hour,
            minute = DATETIME_21000101_0000.minute
        )
    }
}