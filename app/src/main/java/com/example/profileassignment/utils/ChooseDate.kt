package com.example.profileassignment.utils

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.DatePicker
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.example.profileassignment.R
import com.example.profileassignment.presentation.view.ProfileActivity


class ChooseDate : DialogFragment(), View.OnClickListener {
    private var datePicker: DatePicker? = null
    private var acceptButton: Button? = null
    private var ll:LinearLayout?=null

    private var isDateSetted = false
    private var year = 0
    private var month = 0
    private var day = 0

    private var listener: DateListener? = null
    private var context:Context?=null

    interface DateListener {
        fun onDateSelected(year: Int, month: Int, day: Int)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(com.example.profileassignment.R.layout.dialog_picker, container)
context=requireActivity()
       // getDialog()?.setTitle("")

        datePicker = rootView.findViewById(com.example.profileassignment.R.id.datePicker) as DatePicker
        acceptButton = rootView.findViewById(com.example.profileassignment.R.id.buttonAccept)
        ll=rootView.rootView.findViewById(com.example.profileassignment.R.id.ll)
        acceptButton!!.setOnClickListener(this)
        //setStyle(DialogFragment.STYLE_NO_TITLE, R.style.UserDialog);
       // ll?.getForeground()?.setAlpha(0);
        acceptButton?.background=resources.getDrawable(R.drawable.picker_btn_bg)


        val pickerSpinnersHolder =
            datePicker!!.getChildAt(0) as LinearLayout

        pickerSpinnersHolder.getChildAt(0).visibility = View.GONE

        if (isDateSetted) {
            datePicker?.updateDate(year, month, day)
        }

        return rootView
    }

    override fun onClick(v: View) {
        when (v.getId()) {
            com.example.profileassignment.R.id.buttonAccept -> {
                val year: Int = datePicker!!.year
                val month: Int = datePicker!!.getMonth() + 1 // months start in 0
                val day: Int = datePicker!!.getDayOfMonth()

                listener!!.onDateSelected(year, month, day)
            }
        }
        this.dismiss()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ProfileActivity) {
           // (context as ProfileActivity).deleteCollection(db.collection("Employee"),backgroundExecutor)
            this.listener = context
        }

    }

    fun setDate(year: Int, month: Int, day: Int) {
        this.year = year
        this.month = month
        this.day = day
        this.isDateSetted = true
    }
}