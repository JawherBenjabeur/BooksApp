package com.example.your_books.my_app.ui.activities

import android.app.AlertDialog
import com.example.your_books.R

class LoadingDialog(val mActivity: MainActivity) {
    private lateinit var isdialog: AlertDialog
    fun startLoading(){
        //set View
        val inflater= mActivity.layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_dialog_loading,null)
        //set Dialog
        val builder=AlertDialog.Builder(mActivity)
        builder.setView(dialogView)
        builder.setCancelable(false)
        isdialog = builder.create()
        isdialog.show()

    }
    fun isDismiss(){
        isdialog.dismiss()
    }
}