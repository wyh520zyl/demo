package com.cxyzy.safedialog

import android.app.Activity
import android.app.Dialog
import android.os.Build

/**
 * 安全显示dialog
 * 对所属activity应销毁的情况进行保护，避免显示时crash
 */
open class SafeBaseDialog(activity: Activity) : Dialog(activity) {
    private var mActivity: Activity = activity

    override fun show() {
        if (!isFinishingOrDestroyed(mActivity)) {
            super.show()
        }
    }

    private fun isFinishingOrDestroyed(activity: Activity): Boolean {
        return activity.isFinishing
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed)
    }
}