package com.cxyzy.safedialog

import android.app.Activity
import android.app.Dialog

class DemoDialog(activity: Activity) : SafeBaseDialog(activity) {

    init {
        setContentView(R.layout.dialog_layout)
        setCanceledOnTouchOutside(false)
    }

}