package wuge.social.com.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.kongqw.wechathelper.WeChatHelper
import com.kongqw.wechathelper.listener.OnWeChatAuthLoginListener
import com.kongqw.wechathelper.net.response.AccessTokenInfo
import com.kongqw.wechathelper.net.response.WeChatUserInfo
import com.tencent.imsdk.v2.V2TIMCallback
import com.tencent.imsdk.v2.V2TIMManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
import wuge.social.com.R
import wuge.social.com.http.ApiService
import wuge.social.com.http.HiRetrofit
import wuge.social.com.model.ImSign
import wuge.social.com.model.User
import wuge.social.com.model.WxLogin
import wuge.social.com.util.Sha1Util.shaEncrypt
import wuge.social.com.util.Timestamp
import java.util.*
//import wuge.social.com.http.ApiService
//import wuge.social.com.http.HiRetrofit
//import wuge.social.com.model.ImSign
//import wuge.social.com.model.User
//import wuge.social.com.model.WxLogin
//import wuge.social.com.util.Sha1Util.shaEncrypt
//import wuge.social.com.util.Timestamp
//import java.util.*
import kotlin.collections.HashMap

class LogInActivity : BaseActivity(), OnWeChatAuthLoginListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Log.d("LogInActivity->onCreate","进入了LogInActivity")
    }
    override fun onWeChatAuthLoginStart() {
        Log.d("LogInActivity->onWeChatAuthLoginStart","开始申请授权登录")

    }
    override fun onWeChatAuthLoginSuccess(accessTokenInfo: AccessTokenInfo?, weChatUserInfo: WeChatUserInfo?) {
        Log.d("accessTokenInfo ="," $accessTokenInfo")
        Log.d("weChatUserInfo = ","{$weChatUserInfo}")
        Log.d("LogInActivity->onWeChatAuthLoginSuccess","授权登录成功: ${weChatUserInfo?.nickname}  access_token = ${accessTokenInfo?.access_token}")
        if (weChatUserInfo != null) {
            val map=HashMap<String,String>()
            //获取IM签名
            map["nickname"]= weChatUserInfo.nickname.toString()
            map["headImg"]= weChatUserInfo.headimgurl.toString()
            map["openid"]= weChatUserInfo.openid.toString()
            map["sex"]= weChatUserInfo.sex.toString()
            map["unionid"]= weChatUserInfo.unionid.toString()
            getToken(map)
        }
    }

    override fun onWeChatAuthLoginCancel() {
        Toast.makeText(applicationContext, "取消授权登录", Toast.LENGTH_SHORT).show()
    }

    override fun onWeChatAuthLoginAuthDenied() {
        Toast.makeText(applicationContext, "拒绝授权登录", Toast.LENGTH_SHORT).show()
    }

    override fun onWeChatAuthLoginError(errorCode: Int?, errorMessage: String?) {
        Toast.makeText(applicationContext, "授权登录异常 错误码:$errorCode,错误信息:$errorMessage", Toast.LENGTH_SHORT).show()
    }

    //微信登录
    fun wxLogin( view: View) {
        WeChatHelper.getInstance(applicationContext).authLogin(this)
    }

    private fun getToken(map:Map<String, String>){
        val apiService : ApiService = HiRetrofit.create(ApiService::class.java)
        apiService.getToken(map)
            .enqueue(object : Callback<WxLogin> {
                override fun onResponse(call: Call<WxLogin>, response: Response<WxLogin>) {
                    Log.d("getToken_onResponse",response.body()?.toString()?:"response is null")
                    val wxLogin=response.body()
                    if (wxLogin != null) {
                        if(wxLogin.status==1){
                            User.user_id=wxLogin.result.user_id
                            User.token=wxLogin.result.token
                            User.headImg= map["headImg"].toString()
                            User.nickname= map["nickname"].toString()
                            val signMap=HiRetrofit.getParameter(null)
                            //获取IM签名
                            getImSign(signMap)
                        }
                    }
                }

                override fun onFailure(call: Call<WxLogin>, t: Throwable) {
                    Log.d("Retrofit_onFailure",t.message?:"unknown reason")
                }
            })
    }

    fun getImSign(map:Map<String, String>){
        val apiService : ApiService = HiRetrofit.create(ApiService::class.java)
        apiService.getImSign(map)
            .enqueue(object : Callback<ImSign> {
                override fun onResponse(call: Call<ImSign>, response: Response<ImSign>) {
                    Log.d("onResponse->getImSign",response.body()?.toString()?:"response is null")
                    val imSign=response.body()
                    if (imSign != null) {
                        if(imSign.status==1){
                            Log.d("onResponse->getImSign1",imSign.result.toString())
                            val imSignResult=imSign.result
                            val userSig=imSignResult.UserSig
                            val siMap=HashMap<String,String>()
                            siMap["user_id"]= map["user_id"].toString()
                            siMap["token"]= map["token"].toString()
                            siMap["userSig"]= userSig
                            siMap["timeStamp"]= Timestamp.getSecondTimestamp(Date()).toString()
                            siMap["apiSign"]= shaEncrypt(Timestamp.getTimeStamp()).toString()
                            login(siMap)
                        }
                    }
                }

                override fun onFailure(call: Call<ImSign>, t: Throwable) {
                    Log.d("Retrofit_onFailure",t.message?:"unknown reason")
                }
            })
    }

    fun login(map: Map<String, String>) {
        val activity = baseContext
        val mySharedPreferences= getSharedPreferences("test", Activity.MODE_PRIVATE)
        val editor = mySharedPreferences.edit()
         editor.putString("user_id", map["user_id"]);
         editor.putString("token", map["token"]);
         editor.putString("userSig", map["userSig"]);
         editor.apply()
        V2TIMManager.getInstance().login(map["user_id"], map["userSig"], object : V2TIMCallback {
            override fun onError(i: Int, s: String) {
                Log.d("LogInActivity->login","im登录失败："+s)
            }
            override fun onSuccess() {
                runOnUiThread {
                    Log.d("LogInActivity->login","im登录成功")
                    val intent = Intent(activity,HomeActivity::class.java)
                    startActivity(intent)
                }
            }
        })
    }
    fun userAgreement(view: View){
        val intent = Intent(this, WebActivity::class.java)
        intent.putExtra("url", "https://circle.wgzy69.com/api/protocol")
        startActivity(intent)
    }
    fun privacyPolicy(view: View){
        val intent = Intent(this, WebActivity::class.java)
        intent.putExtra("url", "https://circle.wgzy69.com/api/privacy")
        startActivity(intent)
    }
}