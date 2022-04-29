package wuge.social.com

import android.app.Application
import android.os.StrictMode
import com.tencent.imsdk.v2.V2TIMSDKConfig
import com.tencent.qcloud.tim.uikit.TUIKit
import com.tencent.qcloud.tim.uikit.config.CustomFaceConfig
import com.tencent.qcloud.tim.uikit.config.GeneralConfig
import com.kongqw.wechathelper.WeChatHelper.Companion.getInstance

/**
 *
 * @author jackson
 * @version 1.0
 * @date 2020/6/11 15:36
 */
class App1 : Application() {
    val IM_APP_ID = 1400449074 //IM 的appId1400449074

    override fun onCreate() {
        super.onCreate()

        //初始化腾讯IM SDK
        val configs = TUIKit.getConfigs()
        configs.sdkConfig = V2TIMSDKConfig()
        configs.customFaceConfig = CustomFaceConfig()
        configs.generalConfig = GeneralConfig()
        TUIKit.init(this, IM_APP_ID, configs)
        getInstance(applicationContext).init(com.tencent.imsdk.BuildConfig.DEBUG)

    }

    private fun enabledStrictMode() {
        StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
//                .detectAll()
//                .detectDiskReads()
//                .detectDiskWrites()
                        .detectNetwork()
                        .penaltyLog()
                        .penaltyDialog()
                        .penaltyDeath()
                        .build()
        )

        StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                        //检测资源是否正确关闭
                        .detectLeakedClosableObjects()
                        .penaltyLog()
                        .build()
        );
    }
}