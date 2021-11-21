package tech.araki.smartmemo.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object TimeUtil {
    private const val DATE_FORMAT = "yyyy/MM/dd"
    private const val DATETIME_FORMAT = "yyy/MM/dd HH:ss"
    private val DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT)
    private val DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_FORMAT)
    // 何回も使うのでプロパティとして保持しておく
    private val ZONE_ID = ZoneId.systemDefault()

    /**
     * UNIX epoch time millis to String
     */
    fun Long.toDateString(): String =
        Instant.ofEpochMilli(this)
            .atZone(ZONE_ID)
            .format(DATE_FORMATTER)

    /**
     * UNIX epoch time millis to String (Datetime format)
     */
    fun Long.toDatetimeString(): String =
        Instant.ofEpochMilli(this)
            .atZone(ZONE_ID)
            .format(DATETIME_FORMATTER)
}