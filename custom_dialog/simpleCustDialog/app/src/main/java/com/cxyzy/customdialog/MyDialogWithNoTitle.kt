package com.cxyzy.customdialog

import android.app.Dialog
import android.content.Context

/**
 * 默认弹出框外围有一圈透明的部分,导致用户点击时,感觉已经是点击在外面了,但是弹出框却不消失.
 * 本例就是消除这一圈透明的部分.
 */
class MyDialogWithNoTitle(context: Context) : Dialog(context, R.style.common_dialog) {
    init {
        setContentView(R.layout.dialog_layout)
    }
}
