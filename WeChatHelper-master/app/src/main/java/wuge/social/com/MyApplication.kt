package wuge.social.com

import android.app.Application
import com.kongqw.wechathelper.WeChatHelper

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return
//        }
//        LeakCanary.install(this)

        // init Library
        WeChatHelper.getInstance(applicationContext).init(BuildConfig.DEBUG)
    }
}