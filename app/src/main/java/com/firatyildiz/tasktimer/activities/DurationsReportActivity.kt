package com.firatyildiz.tasktimer.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.PersistableBundle
import android.text.format.DateFormat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.firatyildiz.tasktimer.R
import com.firatyildiz.tasktimer.app.AppDialog
import com.firatyildiz.tasktimer.model.AppRoomDatabase
import com.firatyildiz.tasktimer.model.repositories.TimingRepository
import com.firatyildiz.tasktimer.model.viewmodel.TaskDurationsViewModel
import kotlinx.android.synthetic.main.activity_durations_report.*
import kotlinx.coroutines.launch
import java.util.*

class DurationsReportActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    AppDialog.DialogEvents {
    companion object {
        private const val TAG = "DurationsReportActivity"
        const val DIALOG_FILTER = 1
        const val DIALOG_DELETE = 2
        const val DELETE_DATE = "DELETION_DATE"
        const val CURRENT_DATE = "CURRENT_DATE"
        const val DISPLAY_WEEK = "DISPLAY_WEEK"
    }
    
    private lateinit var durationsViewModel: TaskDurationsViewModel
    private lateinit var timingRepository: TimingRepository
    private val calendar = GregorianCalendar()

    private var displayWeek = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_durations_report)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        durationsViewModel = ViewModelProvider(this).get(TaskDurationsViewModel::class.java)
        timingRepository = TimingRepository(AppRoomDatabase.getDatabase(this)?.timingDao()!!)

        if (savedInstanceState != null) {
            val timeInMillis = savedInstanceState.getLong(CURRENT_DATE, 0)
            if (timeInMillis != 0L) {
                calendar.timeInMillis = timeInMillis
                // make sure the time part is cleared, because we filter the database by seconds
                calendar.clear(GregorianCalendar.HOUR_OF_DAY)
                calendar.clear(GregorianCalendar.MINUTE)
                calendar.clear(GregorianCalendar.SECOND)
            }

            this.displayWeek = savedInstanceState.getBoolean(DISPLAY_WEEK, false)

            durationsViewModel.updateTaskDurations()
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putLong(CURRENT_DATE, calendar.timeInMillis)
        outState.putBoolean(DISPLAY_WEEK, displayWeek)
    }

//    override fun onStart() {
//        super.onStart()
//        applyFilter()
//    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_report, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val item = menu?.findItem(R.id.rm_filter_period)
        item?.let {
            // switch the icon to represent 7 days or one day
            item.icon =
                if (displayWeek) getDrawable(R.drawable.ic_filter_7_white_24dp) else getDrawable(R.drawable.ic_filter_1_white_24dp)
            item.title =
                if (displayWeek) getString(R.string.rm_title_filter_day) else getString(R.string.rm_title_filter_week)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.rm_filter_period -> {
                displayWeek = !displayWeek
                applyFilter()
                invalidateOptionsMenu()  // redraw the menu
                true
            }
            R.id.rm_filter_date -> {
                showDatePickerDialog(getString(R.string.date_title_filter), DIALOG_FILTER)
                true
            }
            R.id.rm_delete -> {
                showDatePickerDialog(getString(R.string.date_title_delete), DIALOG_DELETE)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDatePickerDialog(title: String, dialogId: Int) {
        Log.d(TAG, "showDatePickerDialog: showing date picker dialog")
        val dialogFragment = DatePickerFragment()
        val arguments = Bundle()

        arguments.putInt(DatePickerFragment.DATE_PICKER_ID, dialogId)
        arguments.putString(DatePickerFragment.DATE_PICKER_TITLE, title)
        arguments.putSerializable(DatePickerFragment.DATE_PICKER_DATE, calendar.time)

        dialogFragment.arguments = arguments
        dialogFragment.show(supportFragmentManager, "datePicker")
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        // check the id, so we know what to do with the result
        val dialogId = view?.tag
        calendar.set(year, month, dayOfMonth, 0, 0, 0)

        when (dialogId) {
            DIALOG_FILTER -> applyFilter()
            DIALOG_DELETE -> {
                val fromDate = DateFormat.getDateFormat(this).format(calendar.timeInMillis)

                val dialog = AppDialog()
                val args = Bundle()

                args.putInt(AppDialog.DIALOG_ID, 1) // We only have one dialog in this activity
                args.putString(
                    AppDialog.DIALOG_MESSAGE,
                    getString(R.string.delete_timings_message, fromDate)
                )
                args.putLong(DELETE_DATE, calendar.timeInMillis)
                dialog.arguments = args
                dialog.show(supportFragmentManager, null)
            }
            else -> throw IllegalArgumentException("Invalid mode when receiving DatePickerDialog result")
        }
    }

    private fun deleteRecords(timeInMillis: Long) {
        Log.d(TAG, "deleteRecords: deleting")

        val longDate: Long = timeInMillis / 1000 // get time in seconds
        lifecycleScope.launch {
            timingRepository.deleteAllTimingsBeforeTime(longDate)
            durationsViewModel.updateTaskDurations()
        }
    }

    override fun onPositiveDialogResult(dialogId: Int, args: Bundle?) {
        Log.d(TAG, "onPositiveDialogResult: called")

        val deleteDate = args?.getLong(DELETE_DATE)
        // clear all records from Timing table prior to the date selected
        deleteDate?.let { deleteRecords(it) }
    }

    override fun onNegativeDialogResult(dialogId: Int, args: Bundle?) {
        // do nothing
    }

    override fun onDialogCancelled(dialogId: Int) {
        // do nothing
    }

    private fun applyFilter() {
        if (displayWeek) {
            val currentCalendarDate: Date = calendar.time
            val dayOfWeek = calendar.get(GregorianCalendar.DAY_OF_WEEK)
            val weekStart = calendar.firstDayOfWeek

            Log.d(TAG, "applyFilter: first day of calendar week is $weekStart")
            Log.d(TAG, "applyFilter: dayOfWeek is $dayOfWeek")
            Log.d(TAG, "applyFilter: date is ${calendar.time}")

            // calculate week start and end dates
            calendar.set(GregorianCalendar.DAY_OF_WEEK, weekStart)

            val startDate = String.format(
                Locale.US, "%04d-%02d-%02d",
                calendar.get(GregorianCalendar.YEAR),
                calendar.get(GregorianCalendar.MONTH) + 1,
                calendar.get(GregorianCalendar.DAY_OF_MONTH)
            )
            calendar.add(
                GregorianCalendar.DATE,
                6
            ) // move forward 6 days to get the last day of the week

            val endDate = String.format(
                Locale.US, "%04d-%02d-%02d",
                calendar.get(GregorianCalendar.YEAR),
                calendar.get(GregorianCalendar.MONTH) + 1,
                calendar.get(GregorianCalendar.DAY_OF_MONTH)
            )

            durationsViewModel.showTaskDurationsBetweenDates(startDate, endDate)

            calendar.time = currentCalendarDate

            Log.d(
                TAG,
                "applyFilter: in applyFilter(7), start date is $startDate, end date is $endDate"
            )
        } else {
            val startDate = String.format(
                Locale.US, "%04d-%02d-%02d",
                calendar.get(GregorianCalendar.YEAR),
                calendar.get(GregorianCalendar.MONTH) + 1,
                calendar.get(GregorianCalendar.DAY_OF_MONTH)
            )

            durationsViewModel.showTaskDurationsAtDate(startDate)

            Log.d(TAG, "applyFilter: In applyFilter(1), Start Date is $startDate")
        }
    }
}
