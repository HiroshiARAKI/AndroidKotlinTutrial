package tech.araki.smartmemo

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import tech.araki.smartmemo.util.TimeUtil.toZonedDateTime
import java.io.Serializable
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

class DatetimePicker : DialogFragment() {
    interface Listener: Serializable {
        fun onDateTimeChanged(dateTimeWrapper: DateTimeWrapper)
    }

    private val onDateChanged =
        DatePicker.OnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            dateTimeWrapper.year = year
            dateTimeWrapper.month = monthOfYear + 1 // 1始まりにしておく
            dateTimeWrapper.dayOfMonth = dayOfMonth
        }

    private val onTimeChanged =
        TimePicker.OnTimeChangedListener { _, hourOfDay, minute ->
            dateTimeWrapper.hour = hourOfDay
            dateTimeWrapper.minute = minute
        }

    private val dateTimeWrapper = DateTimeWrapper()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Viewのレイアウトを読み込む
        val view = View.inflate(requireContext(), R.layout.dialog_datetime_picker, null)

        // 各種Pickerの初期化
        val initialDateTime = (requireArguments()[KEY_TIME_MILLIS] as Long).toZonedDateTime()
        view.findViewById<DatePicker>(R.id.date_picker).init(initialDateTime)
        view.findViewById<TimePicker>(R.id.time_picker).init(initialDateTime)

        val cancelButton = view.findViewById<Button>(R.id.picker_cancel_button)
        val updateButton = view.findViewById<Button>(R.id.picker_update_button)

        cancelButton.setOnClickListener { dismiss() }
        updateButton.setOnClickListener { update() }

        // AlertDialogをベースにDialog生成
        return AlertDialog.Builder(requireContext()).create().apply {
            setView(view)
        }
    }

    // Listenerをargumentsから読み込んで、メソッドを発火
    private fun update() {
        (requireArguments()[KEY_LISTENER] as Listener).onDateTimeChanged(dateTimeWrapper)
        dismiss()
    }

    // DatePickerを初期化する
    private fun DatePicker.init(z: ZonedDateTime) {
        // DataPickerのmonthは0始まりなので -1 しておく
        updateDate(z.year, z.monthValue - 1, z.dayOfMonth)
        setOnDateChangedListener(onDateChanged)
    }

    // TimePickerを初期化する
    private fun TimePicker.init(z: ZonedDateTime) {
        hour = z.hour
        minute = z.minute
        setOnTimeChangedListener(onTimeChanged)
        setIs24HourView(true)
    }

    companion object {
        private const val KEY_TIME_MILLIS = "time_millis"
        private const val KEY_LISTENER = "listener"

        fun newInstance(timeMillis: Long, listener: DatetimePicker.Listener): DatetimePicker {
            return DatetimePicker().apply {
                arguments = bundleOf(
                    KEY_TIME_MILLIS to timeMillis,
                    KEY_LISTENER to listener
                )
            }
        }
    }

    /** 一時的な日付のラッパークラス */
    data class DateTimeWrapper(
        var year: Int? = null,
        var month: Int? = null,
        var dayOfMonth: Int? = null,
        var hour: Int? = null,
        var minute: Int? = null
    ) {
        /**
         * UNIX時刻に変換する。
         * @param baseDateTime ベースとなる[ZonedDateTime]。もしプロパティがnullであればここから補完される。
         */
        fun toTimeMillis(baseDateTime: ZonedDateTime): Long {
            return ZonedDateTime.of(
                year ?: baseDateTime.year,
                month ?: baseDateTime.monthValue,
                dayOfMonth ?: baseDateTime.dayOfMonth,
                hour ?: baseDateTime.hour,
                minute ?: baseDateTime.minute,
                0,
                0,
                baseDateTime.zone
            ).toEpochSecond() * TimeUnit.SECONDS.toMillis(1)  // [sec] to [ms]
        }
    }
}