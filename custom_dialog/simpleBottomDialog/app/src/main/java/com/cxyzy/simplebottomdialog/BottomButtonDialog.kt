package com.cxyzy.simplebottomdialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.dialog_bottom_button.*

class BottomButtonDialog(context: Context) : Dialog(context, R.style.common_dialog) {
    init {
        setContentView(R.layout.dialog_bottom_button)
        changeDialogStyle()
        editLayout.setOnClickListener { Toast.makeText(context, "edit", Toast.LENGTH_SHORT).show() }
        shareLayout.setOnClickListener { Toast.makeText(context, "share", Toast.LENGTH_SHORT).show() }
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
