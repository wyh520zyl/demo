package wuge.social.com.activity

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import cn.finalteam.rxgalleryfinal.RxGalleryFinal
import cn.finalteam.rxgalleryfinal.RxGalleryFinalApi
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultDisposable
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.yalantis.ucrop.model.AspectRatio
import retrofit2.Call
import retrofit2.Callback
import wuge.social.com.R
import wuge.social.com.http.ApiService
import wuge.social.com.http.HiRetrofit
import wuge.social.com.model.Response
import wuge.social.com.model.User
import wuge.social.com.util.ReuseUtil
import wuge.social.com.util.SimpleRxGalleryFinal
import java.text.SimpleDateFormat
import java.util.*

class PersonalSetActivity : BaseActivity(){

    var broadcastReceiver: UpdateUIBroadcastReceiver? = null
    private var personalSetHeading:ImageView?=null//头像
    private var personalShade:ImageView?=null//遮罩
    private var personalSetNickname:TextView?=null//昵称
    private var personalSetId:TextView?=null//id
    private var personalSetGender:TextView?=null//性别
    private var personalSetBirthday:TextView?=null//生日
    private var personalSetConstellation:TextView?=null//星座
    private var personalSetAddress:TextView?=null//地址
    private var personalSetSignature:TextView?=null//签名
    private var personalSetLevels:TextView?=null//等级
    private var personalSetCharm:TextView?=null//魅力
    private var personalSetValue:TextView?=null//身价
    private var personalSetBirthdayLayout:RelativeLayout?=null//身价
    private var maxDate: Long = System.currentTimeMillis()
    private var minDate: Long = 0
    private var defaultDate: Long = 0
    private var constellation:String=""
    private var isAlterUser=false//是否修改了个人信息
    //private val permsSd = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
   // private val permsPic = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_set)
        registrationRadio()
        init()
    }
    private fun registrationRadio() {
        val filter = IntentFilter()
        filter.addAction(ACTION_UPDATEUI)
        broadcastReceiver = UpdateUIBroadcastReceiver()
        registerReceiver(broadcastReceiver, filter)
    }
    private fun init(){
        personalSetHeading=findViewById(R.id.personal_set_heading)
        personalSetNickname=findViewById(R.id.personal_set_nickname)
        personalSetId=findViewById(R.id.personal_set_id)
        personalSetGender=findViewById(R.id.personal_set_gender)
        personalSetBirthday=findViewById(R.id.personal_set_birthday)
        personalSetConstellation=findViewById(R.id.personal_set_constellation)
        personalSetAddress=findViewById(R.id.personal_set_address)
        personalSetSignature=findViewById(R.id.personal_set_signature)
        personalSetLevels=findViewById(R.id.personal_set_levels)
        personalSetCharm=findViewById(R.id.personal_set_charm)
        personalSetValue=findViewById(R.id.personal_set_value)
        personalShade=findViewById(R.id.personal_shade)
        personalSetBirthdayLayout=findViewById(R.id.personal_set_birthday_layout)
        setUser()
    }

    fun setUser() {
        Log.d("PersonalSetActivity->setUser","resultCode1")
        personalSetNickname!!.text=User.nickname
        personalSetId!!.text=User.user_id
        personalSetGender!!.text=User.sex
        personalSetBirthday!!.text=User.birthday
        personalSetConstellation!!.text=User.constellation
        personalSetAddress!!.text=User.address
        personalSetSignature!!.text=User.signature
        personalSetLevels!!.text=User.levels
        personalSetCharm!!.text=User.charm_value
        personalSetValue!!.text=User.social_value
        when (User.sex.toInt()) {
            1 -> {
                personalSetGender!!.text="男"
            }
            2 -> {
                personalSetGender!!.text="女"
            }
            else ->{
                personalSetGender!!.text=""
            }
        }
        Glide.with(applicationContext)
            .load(User.headImg)
            .thumbnail(Glide.with(applicationContext).load(R.mipmap.map_default))
            .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
            .into(personalSetHeading!!)
    }

    fun personalSetOnclick(v : View){
        when(v.id){
            R.id.personal_set_return->{
                finish()
            }
            R.id.personal_set_heading->{//修改头像
                openRadio()
            }
            R.id.personal_set_nickname_layout->{//修改昵称
//                val intent = Intent(this, SetNickNameActivity::class.java)
//                intent.putExtra("to_id",User.user_id)
//                startActivity(intent)
            }
            R.id.personal_set_qr_layout->{//二维码
                setQr()
            }
            R.id.personal_set_gender_layout->{//修改性别
                setGender()
            }
            R.id.personal_set_birthday_layout->{//修改生日
                setBirthday()
            }
            R.id.personal_set_address_layout->{//修改地区

            }
            R.id.personal_set_signature_layout->{//修改签名
//                val intent1 = Intent(this, SetSignatureActivity::class.java)
//                startActivity(intent1)
            }
            R.id.personal_set_value_layout->{//修改身价
                setSocialStatus()
            }
        }
    }

    /**
     * 获取年
     */
    private fun getYear(): Int {
        val cd = Calendar.getInstance()
        return cd[Calendar.YEAR]
    }

    private fun getTimes(date: Date): String? { //可根据需要自行截取数据显示
        @SuppressLint("SimpleDateFormat") val format = SimpleDateFormat("yyyy-MM-dd")
        return format.format(date)
    }
    //设置生日
    private fun setBirthday() {
//        var displayList: MutableList<Int>? = mutableListOf()
//        displayList?.add(DateTimeConfig.YEAR)
//        displayList?.add(DateTimeConfig.MONTH)
//        displayList?.add(DateTimeConfig.DAY)
//        var model = R.drawable.shape_bg_dialog_custom
//        var pickerLayout = 0
//
//        CardDatePickerDialog.builder(this)
//            .setDisplayType(displayList)
//            .setBackGroundModel(model)
//            .setMaxTime(maxDate)
//            .setPickerLayout(pickerLayout)
//            .setMinTime(minDate)
//            .setDefaultTime(defaultDate)
//            .setWrapSelectorWheel(false)
//            .setThemeColor(if (model == R.drawable.shape_bg_dialog_custom) Color.parseColor("#416AEE") else 0)
//            .showDateLabel(false)
//            .showFocusDateInfo(false)
//            .setOnChoose("选择") {
//                Log.d("setBirthday","选择的"+StringUtils.conversionTime(it, "yyyy-MM-dd"))
//                constellation= star(StringUtils.conversionTime(it, "MM").toInt(),StringUtils.conversionTime(it, "dd").toInt())
//                setUser("birthday",StringUtils.conversionTime(it, "yyyy-MM-dd"))
//            }
//            .setOnCancel("关闭") {
//            }.build().show()
    }

    /**
     * 自定义单选
     */
    private fun openRadio(){
        RxGalleryFinal
            .with(this@PersonalSetActivity)
            .image()
            .radio()
            .cropAspectRatioOptions(0, AspectRatio("3:3", 30F, 30F))
            .crop()
            .imageLoader(ImageLoaderType.FRESCO)
            .subscribe(object : RxBusResultDisposable<ImageRadioResultEvent?>() {
                @Throws(Exception::class)
                override fun onEvent(t: ImageRadioResultEvent?) {
                    if (t != null) {
                        Toast.makeText(
                            baseContext,
                            "选中了图片路径：" + t.result.originalPath,
                            Toast.LENGTH_SHORT
                        ).show()
                        val inputImg = t.result.originalPath
                          Toast.makeText(this@PersonalSetActivity, "没有图片演示，请选择‘拍照裁剪’功能", Toast.LENGTH_SHORT).show()
                          RxGalleryFinalApi.cropScannerForResult(this@PersonalSetActivity, RxGalleryFinalApi.getModelPath(), inputImg);//调用裁剪.RxGalleryFinalApi.getModelPath()为模拟的输出路径
                        object : SimpleRxGalleryFinal.RxGalleryFinalCropListener {

                            override fun getSimpleActivity(): AppCompatActivity {
                        return this@PersonalSetActivity
                            }

                            override fun onCropCancel() {
                                Toast.makeText(simpleActivity, "裁剪被取消", Toast.LENGTH_SHORT).show()
                            }

                            override fun onCropSuccess(uri: Uri?) {
                                Toast.makeText(simpleActivity, "裁剪成功：$uri", Toast.LENGTH_SHORT)
                                    .show()
                                println("裁剪后的路径")
                            }

                            override fun onCropError(errorMessage: String) {
                                Toast.makeText(simpleActivity, errorMessage, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }


            })
            .openGallery()
    }
//    private fun setHeading() {
//        if (mRbCropZD.isChecked()) {
//            //直接裁剪
//            val inputImg = ""
//            Toast.makeText(this@PersonalSetActivity, "没有图片演示，请选择‘拍照裁剪’功能", Toast.LENGTH_SHORT).show()
//            //  RxGalleryFinalApi.cropScannerForResult(MainActivity.this, RxGalleryFinalApi.getModelPath(), inputImg);//调用裁剪.RxGalleryFinalApi.getModelPath()为模拟的输出路径
//        } else {
//            SimpleRxGalleryFinal.get().init(
//                object : SimpleRxGalleryFinal.RxGalleryFinalCropListener {
//
//                    override fun getSimpleActivity(): AppCompatActivity {
//                        return this@PersonalSetActivity
//                    }
//
//                    override fun onCropCancel() {
//                        Toast.makeText(simpleActivity, "裁剪被取消", Toast.LENGTH_SHORT).show()
//                    }
//
//                    override fun onCropSuccess(uri: Uri?) {
//                        Toast.makeText(simpleActivity, "裁剪成功：$uri", Toast.LENGTH_SHORT).show()
//                        println("裁剪后的路径")
//                    }
//
//                    override fun onCropError(errorMessage: String) {
//                        Toast.makeText(simpleActivity, errorMessage, Toast.LENGTH_SHORT).show()
//                    }
//                }
//            ).openCamera()
//        }
//    }

  


    /*
       获取二维码
       */
    private fun setQr() {
//        personalShade!!.alpha = 0.7f
//        @SuppressLint("InflateParams") val view: View = LayoutInflater.from(this).inflate(R.layout.wuge_user_qr, null, false)
//        val sharePop = PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true)
//        sharePop.setBackgroundDrawable(null)
//        sharePop.isFocusable = true
//        sharePop.showAtLocation(window.decorView, Gravity.CENTER, 0, 0)
//        val qrImage = view.findViewById<ImageView>(R.id.qr_image)
//        val qrMyImage = view.findViewById<ImageView>(R.id.qr_my_image)
//        val logoImage = view.findViewById<ImageView>(R.id.logo_image)
//        qrImage.setImageBitmap(EncodingHandler.createQRCode(User.user_id, 400, 400))
//        Glide.with(applicationContext)
//            .load(User.headImg)
//            .thumbnail(Glide.with(applicationContext).load(R.mipmap.map_default))
//            .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
//            .into(qrMyImage)
//        Glide.with(applicationContext)
//            .load(User.headImg)
//            .thumbnail(Glide.with(applicationContext).load(R.mipmap.map_default))
//            .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
//            .into(logoImage)
//        sharePop.setOnDismissListener {
//            personalShade!!.alpha = 0f
//        }
    }

    //修改性别
    @SuppressLint("InflateParams")
    private fun setGender(){
        personalShade!!.alpha = 0.7f
        val view: View = LayoutInflater.from(this).inflate(R.layout.set_gender, null, false)
        val sharePop = PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true)
        sharePop.setBackgroundDrawable(null)
        sharePop.isFocusable = true
        sharePop.showAtLocation(window.decorView, Gravity.BOTTOM, 0, 0)
        val informationGenderSetBoy = view.findViewById<TextView>(R.id.information_gender_set_boy)
        val informationGenderSetGirl = view.findViewById<TextView>(R.id.information_gender_set_girl)
        val informationGenderSetCancel = view.findViewById<TextView>(R.id.information_gender_set_cancel)
        informationGenderSetBoy.setOnClickListener {
            if (User.sex != "1") {
                setUser("sex","1")
            }
            sharePop.dismiss()
        }
        informationGenderSetGirl.setOnClickListener {
            if (User.sex != "2") {
                setUser("sex","2")
            }
            sharePop.dismiss()
        }
        informationGenderSetCancel.setOnClickListener { sharePop.dismiss() }
        sharePop.setOnDismissListener { personalShade!!.alpha = 0f }
    }

    private fun setUser( type:String,text:String){
        val map= HiRetrofit.getParameter(hashMapOf(type to text))
        val apiService : ApiService = HiRetrofit.create(ApiService::class.java)
        apiService.userInfoUpd(map)
            .enqueue(object : Callback<Response> {
                override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                    Log.d("setLocation_onResponse",response.body()?.toString()?:"response is null")
                    val body=response.body()
                    if (body != null) {
                        if(body.status==1){//修改个人信息成功
                            isAlterUser=true
                            when(type){
                                "sex" -> User.sex=text
                                "birthday"-> {
                                    User.birthday=text
                                    User.constellation=constellation
                                }
                                "social_value"->{
                                    User.social_value=text
                                }
                            }
                            setUser()
                        }
                    }
                }
                override fun onFailure(call: Call<Response>, t: Throwable) {
                    Log.d("setLocation->onFailure",t.message?:"unknown reason")
                }
            })
    }



    companion object {
        private const val REQUEST_IMAGE = 2
        private const val REQUEST_IMAGE_SELECT = 20
        private const val REQUEST_IMAGE_CROP = 30
        val ACTION_UPDATEUI = "action.updateUI"

        fun star(m:Int,d:Int):String{
            var res="格式错误"
            val date= intArrayOf(20,19,21,20,21,22,23,23,23,24,23,22)
            val index=m//索引
            val luckdData= arrayListOf<Map<String,Any>>(
                mapOf("星座" to "摩羯座"),
                mapOf("星座" to "水瓶座"),
                mapOf("星座" to "双鱼座"),
                mapOf("星座" to "白羊座"),
                mapOf("星座" to "金牛座"),
                mapOf("星座" to "双子座"),
                mapOf("星座" to "巨蟹座"),
                mapOf("星座" to "狮子座"),
                mapOf("星座" to "处女座"),
                mapOf("星座" to "天秤座"),
                mapOf("星座" to "天蝎座"),
                mapOf("星座" to "射手座"),
                mapOf("星座" to "摩羯座")
            )
            when(m){
                1,2,3,4,5,6,7,8,9,10,11,12->{
                    when(d){
                        in 1..31->
                            if(d<date[m-1]){
                                res=luckdData[index-1]["星座"].toString()
                            }else{
                                res=luckdData[index]["星座"].toString()
                            }
                            else->res="天数格式错误！"
                    }
                }else->{
                    res="月份格式错误"
                }
            }
            return res
        }
    }

    /**
     * 定义广播接收器（内部类）
     *
     * @author lenovo
     */
    class UpdateUIBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d("UpdateUIBroadcastReceiver->onReceive","开始修改数据")
            (context as PersonalSetActivity).isAlterUser=true
            (context as PersonalSetActivity).setUser()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(isAlterUser){
            ReuseUtil.refreshRecyclerViewActivity(PersonalActivity1.ACTION_UPDATEUI,this@PersonalSetActivity)
        }
        // 注销广播
        unregisterReceiver(broadcastReceiver)
    }

    //修改身价
    private fun setSocialStatus() {
        personalShade!!.alpha = 0.7f
        @SuppressLint("InflateParams") val view: View = LayoutInflater.from(this).inflate(R.layout.set_social_status, null, false)
        val sharePop = PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true)
        sharePop.setBackgroundDrawable(null)
        sharePop.isFocusable = true
        sharePop.showAtLocation(window.decorView, Gravity.CENTER, 0, 0)
        val socialStatusEdit = view.findViewById<EditText>(R.id.social_status_edit)
        val socialStatusConfirm = view.findViewById<Button>(R.id.social_status_confirm)
        val socialStatusCancel = view.findViewById<Button>(R.id.social_status_cancel)

        //确定
        socialStatusConfirm.setOnClickListener {
            val status = socialStatusEdit.text.toString().trim { it <= ' ' }
            if (status != "") {
                sharePop.dismiss()
                setUser("social_value",status)
            }
        }

        //取消
        socialStatusCancel.setOnClickListener { sharePop.dismiss() }
        sharePop.setOnDismissListener { personalShade!!.alpha = 0f }
    }
}