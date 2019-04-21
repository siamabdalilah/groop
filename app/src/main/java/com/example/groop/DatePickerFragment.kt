package com.example.groop


import android.R
import android.app.DatePickerDialog
//import android.app.DialogFragment
import androidx.fragment.app.DialogFragment
import android.os.Bundle
//import android.app.Fragment
import androidx.fragment.app.FragmentActivity
import android.widget.TextView
import android.widget.DatePicker
import android.app.Dialog
import androidx.annotation.Nullable
import kotlinx.android.synthetic.main.create_groop.*
import java.util.Calendar

/**
 * A simple [Fragment] subclass.
 * https://android--examples.blogspot.com/2015/05/how-to-use-datepickerdialog-in-android.html
 * used ^^ for this entire class. NOT MY WORK!
 */
class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(@Nullable savedInstanceState: Bundle?): Dialog {
        //Use the current date as the default date in the date picker

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        //Create a new DatePickerDialog instance and return it
        /*
            DatePickerDialog Public Constructors - Here we uses first one
            public DatePickerDialog (Context context, DatePickerDialog.OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth)
            public DatePickerDialog (Context context, int theme, DatePickerDialog.OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth)
         */
        return DatePickerDialog(activity, this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        //Do something with the date chosen by the user
        val tv = activity!!.starttime_id as TextView
        tv.text = "Date changed..."
        tv.text = tv.text.toString() + "\nYear: " + year
        tv.text = tv.text.toString() + "\nMonth: " + month
        tv.text = tv.text.toString() + "\nDay of Month: " + day
        tv.text=""+year+"-"+month+"-"+day

       // val stringOfDate = day.toString() + "/" + month + "/" + year
        //tv.text = tv.text.toString() + "\n\nFormatted date: " + stringOfDate
    }
}