package wuge.social.com.activity
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.ftd.livepermissions.LivePermissions
import com.ftd.livepermissions.PermissionResult
import com.tencent.imsdk.v2.V2TIMCallback
import com.tencent.imsdk.v2.V2TIMManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import wuge.social.com.R
import wuge.social.com.http.ApiService
import wuge.social.com.http.CodeUser
import wuge.social.com.http.CodeUserResult
import wuge.social.com.http.HiRetrofit
import wuge.social.com.model.User
import wuge.social.com.util.Sha1Util
import wuge.social.com.util.Timestamp
import java.util.*
import kotlin.collections.HashMap

class StartupActivity : BaseActivity() {
    private var BACK_LOCATION_PERMISSION = "android.permission.ACCESS_BACKGROUND_LOCATION"
    private var isPrivacy:String=""
    private var userId:String=""
    private var token:String=""
    private var userSig:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)
        object : Thread() {
            override fun run() {
                sleep(2000)
                //这儿是耗时操作，完成之后更新UI；
                runOnUiThread {
                    if (isPrivacy!="isPrivacy")
                        userNotice()
                }
            }
        }.start()
    }
    private fun init(){
        applyForPermission()
    }

    private fun toLoginActivity(){
        Thread {
            Log.d("toLoginActivity", "开始进入LogInActivity")
            Thread.sleep(1000)
            runOnUiThread {
                val intent = Intent(this, LogInActivity::class.java)
                startActivity(intent)
            }
        }.start()
    }

    /**
     * 监听GPS
     */
    private fun initGPS() {
        val locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager
        // 判断GPS模块是否开启，如果没有则开启
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this@StartupActivity, "请打开GPS", Toast.LENGTH_SHORT).show()
            val dialog = AlertDialog.Builder(this)
            dialog.setMessage("请打开GPS")
            dialog.setPositiveButton("确定") { _, _ ->
                // 转到手机设置界面，用户设置GPS
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(intent, 0) // 设置完成后返回到原来的界面
            }
            dialog.setNeutralButton("取消") { _, _ ->
                println("点击了取消")
                finish()
            }
            dialog.show()
        } else {
            println("已经打开了gps")
            val sharedPreferences:SharedPreferences=getSharedPreferences("test", Activity.MODE_PRIVATE)
            isPrivacy = sharedPreferences.getString("isPrivacy", "").toString()
            userId = sharedPreferences.getString("user_id", "").toString()
            token = sharedPreferences.getString("token", "").toString()
            userSig = sharedPreferences.getString("userSig", "").toString()
            if(isPrivacy=="isPrivacy"){
                init()
            }
        }
    }

    /**
     *表示Activity已经启动完成，进入到了前台，可以同用户进行交互了。
     */
    override fun onResume() {
        super.onResume()
        Log.d("StartupActivity","onResume")
        initGPS()
    }
    /**
     * 当Activity不可见的时候回调此方法。需要在这里释放全部用户使用不到的资源。可以做
     * 较重量级的工作，如对注册广播的解注册，对一些状态数据的存储。此时Activity还不会
     * 被销毁掉，而是保持在内存中，但随时都会被回收。通常发生在启动另一个Activity或切
     * 换到后台时
     */
    override fun onStop() {
        super.onStop()
        Log.d("StartupActivity","onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("StartupActivity","onDestroy")
    }

    fun login(user: CodeUser?) {
        val activity = baseContext
        V2TIMManager.getInstance().login(userId, userSig, object : V2TIMCallback {
            override fun onError(i: Int, s: String) {}
            override fun onSuccess() {
                Thread {
                    runOnUiThread {
                        setUser(user)
                        val intent = Intent(activity, HomeActivity::class.java)
                        startActivity(intent)
                    }
                }.start()
            }
        })
    }
    @SuppressLint("InflateParams")
    private fun userNotice(){
        val startupShade = findViewById<ImageView>(R.id.startup_shade)
        startupShade.alpha = 0.7f
        val commentView: View = LayoutInflater.from(this).inflate(R.layout.startup_hint, null, false)
        Log.d("userNotice->commentView:",commentView.toString())
        val sharePop = PopupWindow(
            commentView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )
        Log.d("userNotice->sharePop:",sharePop.toString())
        sharePop.setBackgroundDrawable(null)
        sharePop.isFocusable = false
        sharePop.showAtLocation(commentView, Gravity.CENTER, 0, 0)//此处在用户拒绝后对导致报错
        val removeChatBtnNot = commentView.findViewById<TextView>(R.id.remove_chat_btn_not)
        val removeChatBtnOk = commentView.findViewById<TextView>(R.id.remove_chat_btn_ok)
        val startupUserAgreement = commentView.findViewById<TextView>(R.id.startup_user_agreement)
        //        TextView startup_privacy_policy=commentView.findViewById(R.id.startup_privacy_policy);
        val style = SpannableStringBuilder()
        style.append("你可阅读《用户使用协议》和《隐私政策》了解详细信息。如果你同意，请点击\"同意\"开始接受我们的服务")
        //设置部分文字点击事件
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val activity=widget.context
                val intent = Intent(activity, WebActivity::class.java)
                intent.putExtra("url", "https://circle.wgzy69.com/api/protocol")
                startActivity(intent)
            }
        }
        style.setSpan(clickableSpan, 4, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val foregroundColorSpan1 = ForegroundColorSpan(Color.parseColor("#326bf7"))
        style.setSpan(foregroundColorSpan1, 4, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val clickableSpan1: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val activity=widget.context
                val intent = Intent(activity, WebActivity::class.java)
                intent.putExtra("url", "https://circle.wgzy69.com/api/privacy")
                startActivity(intent)
            }
        }
        style.setSpan(clickableSpan1, 13, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        startupUserAgreement.text = style
        //设置部分文字颜色
        val foregroundColorSpan = ForegroundColorSpan(Color.parseColor("#326bf7"))
        style.setSpan(foregroundColorSpan, 13, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        //配置给TextView
        startupUserAgreement.movementMethod = LinkMovementMethod.getInstance()
        startupUserAgreement.text = style
        removeChatBtnOk.setOnClickListener {
            println("StartupActivity测试3.4")
            val sharedPreferences= getSharedPreferences("test", Activity.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("isPrivacy", "isPrivacy")
            editor.apply()
            isPrivacy = sharedPreferences.getString("isPrivacy", "").toString()
            sharePop.dismiss()

        }
        removeChatBtnNot.setOnClickListener { //
            finish()
        }
        sharePop.setOnDismissListener {
            if("isPrivacy" == isPrivacy){
                init()
                startupShade.alpha = 0f
            }
        }
    }

    private fun setUser(codeUse: CodeUser?){
        Log.d("setUser","user:"+codeUse.toString())
        val user: CodeUserResult? = codeUse?.result
        if (user != null) {
            User.user_id = userId
            User.address = user.address
            //   User.adolescent_status = user.adolescent_status
            User.age = user.age.toString()
            User.birthday = user.birthday
            User.charm_value = user.charm_value
            //User.client_id = user.client_id
            User.constellation = user.constellation
            User.exp = user.exp
            //  User.game_id = user.g
            User.gold = user.gold
            User.headImg = user.headImg
//            User.idCard = user.idCard
//            User.is_im = user.is_im
//            User.is_online = user.is_online
//            User.latitude = user.latitude
            User.levels = user.levels
//            User.longitude = user.longitude
//            User.mch_id = user.mch_id
            User.nearby_status = user.nearby_status
            User.nickname = user.nickname
            //  User.pwd = user.pwd
            User.sex = user.sex
            User.signature = user.signature
            User.social_value = user.social_value
            // User.source = user.source
            // User.szr_id = user.szr_id
            //   User.user_id = user.user_id
            //  User.wall_url = user.wall_url
            User.token=token
        }
    }

    //权限判断
    private fun applyForPermission(){
        LivePermissions(this).request(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            BACK_LOCATION_PERMISSION
        ).observe(this, {
            when(it){
                is PermissionResult.Grant -> {  //权限允许
                }
                is PermissionResult.Rationale -> {  //权限拒绝
                    it.permissions.forEach { _ ->
                    }
                }
                is PermissionResult.Deny -> {   //权限拒绝，且勾选了不再询问
                    it.permissions.forEach { _ ->
                    }
                }
            }
            verification()
        })
    }

    //验证登录
    private fun verification(){
        if (userId.isNotEmpty() && token.isNotEmpty()) {
            User.user_id=userId
            User.token=token
            val map=HashMap<String,String>()
            map["user_id"] = userId
            map["token"] = token
            map["timeStamp"]= Timestamp.getSecondTimestamp(Date()).toString()
            map["apiSign"]= Sha1Util.shaEncrypt(Timestamp.getTimeStamp()).toString()
            map["to_id"]= userId
            Log.d("StartupActivity->userId","user_id"+map["user_id"])
            Log.d("StartupActivity->token","token"+map["token"])
            Log.d("StartupActivity->timeStamp","timeStamp"+map["timeStamp"])
            Log.d("StartupActivity->apiSign","apiSign"+map["apiSign"])

            val apiService : ApiService = HiRetrofit.create(ApiService::class.java)
            apiService.queryUser(map)
                .enqueue(object : Callback<CodeUser> {
                    override fun onResponse(call: Call<CodeUser>, response: Response<CodeUser>){
                        Log.d("Retrofit_onResponse",response.body()?.toString()?:"response is null")
                        if(userSig.isNotEmpty())
                            login(response.body())
                    }

                    override fun onFailure(call: Call<CodeUser>, t: Throwable) {
                        Log.d("Retrofit_onFailure",t.message?:"unknown reason")
                        toLoginActivity()
                    }
                })
        }else{
            toLoginActivity()
        }
    }
}