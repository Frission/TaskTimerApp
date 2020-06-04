package com.firatyildiz.tasktimer.activities

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    companion object {
        private const val TAG = "DatePickerFragment"
        const val DATE_PICKER_ID = "ID"
        const val DATE_PICKER_TITLE = "TITLE"
        const val DATE_PICKER_DATE = "DATE"
    }

    var dialogId = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val cal = GregorianCalendar()
        var title: String? = null

        arguments?.let { args ->
            dialogId = args.getInt(DATE_PICKER_ID)
            title = args.getString(DATE_PICKER_TITLE)

            val givenDate = args.getSerializable(DATE_PICKER_DATE) as Date?
            givenDate?.let { date ->
                cal.time = date
                Log.d(TAG, "onCreateDialog: retrieved date is : ${date.toString()}")
            }
        }

        val year = cal.get(GregorianCalendar.YEAR)
        val month = cal.get(GregorianCalendar.MONTH)
        val day = cal.get(GregorianCalendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), this, year, month, day)
        title?.let {
            datePickerDialog.setTitle(title)
        }

        return datePickerDialog
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // activities using this dialog must implement its callbacks
        if (context !is DatePickerDialog.OnDateSetListener)
            throw ClassCastException("$context must implement DatePickerDialog.OnDateSetListener interface")
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        Log.d(TAG, "onDateSet: date is being set by ${DatePickerFragment::class.simpleName}")
        val listener = activity as? DatePickerDialog.OnDateSetListener
        listener?.let {
            view?.tag =
                dialogId // pass the id back in the tag, to save the caller storing their own copy
            it.onDateSet(view, year, month, dayOfMonth)
        }
    }

}