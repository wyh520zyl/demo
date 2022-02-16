package com.cxyzy.customdialog

import android.app.Dialog
import android.content.Context

/**
 * 带显示动画的弹出框
 */
class MyDialogWithAnimation(context: Context) : Dialog(context) {
    init {
        setContentView(R.layout.dialog_layout)
        setCanceledOnTouchOutside(false)
        setAnimationsBottomUp()
    }

    private fun setAnimationsBottomUp() {
        window?.setWindowAnimations(R.style.dialogAnimBottomUp)
    }
}
