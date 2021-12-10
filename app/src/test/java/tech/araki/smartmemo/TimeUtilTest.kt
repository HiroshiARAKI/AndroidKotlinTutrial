package tech.araki.smartmemo

import org.junit.Test
import com.google.common.truth.Truth.assertThat
import tech.araki.smartmemo.util.TimeUtil.toDateString
import tech.araki.smartmemo.util.TimeUtil.toDatetimeString


class TimeUtilTest {

    @Test
    fun `Long toDateString test`() {
        assertThat(TIME_20211209_1700.toDateString()).isEqualTo("2021/12/09")
    }

    @Test
    fun `Long toDatetimeString test`() {
        assertThat(TIME_20211209_1700.toDatetimeString()).isEqualTo("2021/12/09 17:00")
    }

    companion object {
        private const val TIME_20211209_1700 = 1639036800000L  // 2021/12/09 17:00
    }
}