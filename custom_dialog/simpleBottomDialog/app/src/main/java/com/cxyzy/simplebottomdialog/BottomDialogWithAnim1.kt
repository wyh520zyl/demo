package com.cxyzy.simplebottomdialog

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.TranslateAnimation
import kotlinx.android.synthetic.main.dialog_bottom_anim1.*

/**
 * 操作视图实现弹出框显示动画
 */
class BottomDialogWithAnim1(context: Context) : Dialog(context, R.style.common_dialog) {
    init {
        setContentView(R.layout.dialog_bottom_anim1)
        changeDialogStyle()
        showWithMoveAnim(rootLayout)
    }

    /**
     * 动画方式显示，从底部向上显示出来。
     * 如果其他想要其他方式，则修改里面的animation实现即可。
     */
    private fun showWithMoveAnim(srcView: View) {
        val animateTime = 300L
        val animation = TranslateAnimation(0f, 0f, getWindowHeight(context) - srcView.height.toFloat(), 0f)
        animation.fillAfter = true
        animation.duration = animateTime
        srcView.startAnimation(animation)
    }

    private fun getWindowHeight(context: Context): Int {
        val point = Point()
        val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = manager.defaultDisplay
        if (Build.VERSION.SDK_INT > 16) {
            display.getRealSize(point)
        } else {
            display.getSize(point)
        }

        return Point(point).y
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
