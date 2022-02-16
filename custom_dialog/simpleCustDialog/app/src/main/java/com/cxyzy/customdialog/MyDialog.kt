package com.cxyzy.customdialog

import android.app.Dialog
import android.content.Context

class MyDialog(context: Context) : Dialog(context) {
    init {
        setContentView(R.layout.dialog_layout)
//        setCanceledOnTouchOutside(false)
    }
}
