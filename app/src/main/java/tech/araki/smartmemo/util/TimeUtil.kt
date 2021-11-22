package tech.araki.smartmemo.util

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object TimeUtil {
    private const val DATE_FORMAT = "yyyy/MM/dd"
    private const val DATETIME_FORMAT = "yyyy/MM/dd HH:mm"
    private val DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT)
    private val DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_FORMAT)
    // 何回も使うのでプロパティとして保持しておく
    val ZONE_ID: ZoneId = ZoneId.systemDefault()

    /**
     * UNIX epoch time millis to String
     */
    fun Long.toDateString(): String = this.toZonedDateTime().format(DATE_FORMATTER)

    /**
     * UNIX epoch time millis to String (Datetime format)
     */
    fun Long.toDatetimeString(): String = this.toZonedDateTime().format(DATETIME_FORMATTER)

    /**
     * UNIX epoch time millis to [ZonedDateTime]
     */
    fun Long.toZonedDateTime(): ZonedDateTime =
        Instant.ofEpochMilli(this)
            .atZone(ZONE_ID)
}