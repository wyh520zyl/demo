package com.cxyzy.simplebottomdialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.ViewGroup

class BottomDialog(context: Context) : Dialog(context, R.style.common_dialog) {
    init {
        setContentView(R.layout.dialog_bottom)
        changeDialogStyle()
    }

    /**
     * 设置dialog居下占满屏幕
     */
    private fun changeDialogStyle() {
        window?.let {
            val params = it.attributes
            if (params != null) {
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                params.gravity = Gravity.BOTTOM
                it.attributes = params
            }
        }
    }
}
