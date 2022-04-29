package com.kongqw.wechathelper.utils

import android.util.Log
import com.kongqw.wechathelper.WeChatHelper

object Logger {

    fun d(log: String?) {
        if (WeChatHelper.IS_LOGGABLE) {
            if (log != null) {
                Log.d("Logger", log)
            }
        }
    }
}