package wuge.social.com.activity

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.*
import androidx.appcompat.app.AlertDialog
import cn.finalteam.rxgalleryfinal.RxGalleryFinal
import cn.finalteam.rxgalleryfinal.RxGalleryFinalApi
import cn.finalteam.rxgalleryfinal.bean.MediaBean
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultDisposable
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import wuge.social.com.R
import wuge.social.com.adapter.GridViewAdapter
import wuge.social.com.http.ApiService
import wuge.social.com.http.HiRetrofit
import wuge.social.com.model.Response
import wuge.social.com.model.User
import wuge.social.com.util.ReuseUtil
import wuge.social.com.util.Sha1Util
import wuge.social.com.util.Timestamp
import wuge.social.com.util.Upload
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class DiscoverIssueActivity :BaseActivity(),AdapterView.OnItemClickListener{
    private var gridView: GridView? = null//图片控件
    private var gvAdapter: GridViewAdapter? = null
    private var mEditText: EditText? = null
    private var listPath: ArrayList<String> = ArrayList()//图片或者视频路径集合
    private var issueAwait: LinearLayout? = null
    private var issueDefault: ImageView? = null
    private var lastIntent: Intent? = null
    private var video:String=""
    private var date:String=""
    private var type=0//当前是图片还是视频（1：图片 2：视频）
    //
    companion object {
        val ACTION_UPDATEUI = "action.updateUI"
        private const val REQUEST_IMAGE = 2
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.discover_issue)
        init()
    }
    fun init(){
        gridView = findViewById<View>(R.id.issue_grid) as GridView
        mEditText = findViewById(R.id.issue_edit)
        issueAwait = findViewById(R.id.issue_await)
        issueDefault = findViewById(R.id.issue_default)
        val issueBackground = findViewById<ImageView>(R.id.issue_background)
        gridView!!.onItemClickListener = this
        gvAdapter = GridViewAdapter()
        gvAdapter!!.setLists(listPath,baseContext,date)
        gridView!!.adapter = gvAdapter
        lastIntent = intent
        //显示加载动画
        val operatingAnim = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate_anim_online_gift)
        val lin = LinearInterpolator()
        operatingAnim.interpolator = lin
        issueBackground.startAnimation(operatingAnim)
    }

    fun issueOnclick(v :View){
        when(v.id){
            R.id.issue_return->{//返回
                finish()
            }
            R.id.issue_btn->{//发布
                Log.d("DiscoverIssueActivity->issue_btn",listPath.toString())
                //  content= mEditText?.text.toString().trim()
                // Upload(listPath,content,"成都市",baseContext).setUpload()
                //var map= mapOf<String, MultipartBody.Part>()
                val content= mEditText?.text.toString().trim().toRequestBody("text/plain".toMediaTypeOrNull())
                val userId= User.user_id.toRequestBody("text/plain".toMediaTypeOrNull())
                val token= User.token.toRequestBody("text/plain".toMediaTypeOrNull())
                val timeStamp= Timestamp.getSecondTimestamp(Date()).toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val apiSign= Sha1Util.shaEncrypt(Timestamp.getTimeStamp()).toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val list :ArrayList<File> = ArrayList()
                for (path in listPath){
                    val file= File(path)
                    list.add(file)
                }
                val apiService : ApiService = HiRetrofit.create(ApiService::class.java)
                if(type==1){
                    val lists =files("picture_text[]",list)
                    //  val map= HiRetrofit.getParameter(hashMapOf("content" to content))
                    apiService.releaseDynamic(
                        userId,
                        token,
                        timeStamp,
                        apiSign,
                        content,
                        lists
                    ).enqueue(object : Callback<Response> {
                        override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                            Log.d("issueOnclick_onResponse",response.body()?.toString()?:"response is null")
                            mEditText!!.setText("")
                        }
                        override fun onFailure(call: Call<Response>, t: Throwable) {
                            Log.d("issueOnclick->onFailure",t.message?:"unknown reason")
                        }
                    })
                }else if(type==2){
//                    val file= File(listPath[0])
//                    val re= file.asRequestBody("text/plain".toMediaTypeOrNull())
//                    val part=MultipartBody.Part.createFormData("video_text",file.name,re)
                    val u= Upload(listPath,mEditText?.text.toString().trim(),this)
                    u.setUpload()
//                    val file=File(listPath[0])
//                   // val photo= RequestBody.create(listPath[0].toMediaTypeOrNull(),file)
//                    val re= file.asRequestBody("text/plain".toMediaTypeOrNull())
//                    val photoPart = MultipartBody.Part.createFormData("video_text[]",file.name,re)
//                    apiService.releaseDynamic(
//                        userId,
//                        token,
//                        timeStamp,
//                        apiSign,
//                        content,
//                        photoPart
//                    ).enqueue(object : Callback<Response> {
//                        override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
//                            Log.d("issueOnclick_onResponse",response.body()?.toString()?:"response is null")
//                            mEditText!!.setText("")
//                        }
//                        override fun onFailure(call: Call<Response>, t: Throwable) {
//                            Log.d("issueOnclick->onFailure",t.message?:"unknown reason")
//                        }
//                    })
                }
            }
            R.id.issue_edit_layout->{
                ReuseUtil.showInput(mEditText!!,baseContext)
            }
        }
    }

    private fun files(name :String,files: List<File>): Array<MultipartBody.Part?> {
        //val parts: MutableList<MultipartBody.Part>[] =ArrayList(files.size)
        val array = arrayOfNulls<MultipartBody.Part>(files.size)
        for((i, file) in files.withIndex()){
            val re= file.asRequestBody("text/plain".toMediaTypeOrNull())
            val part=MultipartBody.Part.createFormData(name,file.name,re)
            array[i]=part
        }
        return array
    }
    private var mSelectPath: ArrayList<String>? = null
    private var list: List<MediaBean>? = null
    private fun issueImage() {

//        RxGalleryFinal.with(this).hidePreview();
        val rxGalleryFinal = RxGalleryFinal
            .with(this@DiscoverIssueActivity)
            .image()
            .multiple()
        if (list != null && list!!.isNotEmpty()) {
            println("asdasfsadwds")
            rxGalleryFinal
                .selected(list!!)
        }
        rxGalleryFinal.maxSize(8)
            .imageLoader(ImageLoaderType.FRESCO)
            .subscribe(object : RxBusResultDisposable<ImageMultipleResultEvent?>() {
                @Throws(Exception::class)
                override fun onEvent(t: ImageMultipleResultEvent?) {
                    if (t != null) {
                        list = t.result
                        if(type==2){
                            listPath.clear()
                        }
                        val sb = StringBuilder()
                        list?.forEach {
                            sb.append(it)
                            listPath.add(it.originalPath)
                            sb.append("\n")
                        }
                        type=1
                    }
                    if (t != null) {
                        Toast.makeText(
                            baseContext,
                            "已选择" + t.result.size + "张图片",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    gvAdapter!!.setLists(listPath,baseContext,date)
                    gridView!!.adapter = gvAdapter
                }

                override fun onComplete() {
                    super.onComplete()
                    Toast.makeText(baseContext, "OVER", Toast.LENGTH_SHORT).show()
                }


            })
            .openGallery()

    }
    //接收上个页面返回的数据
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_IMAGE) {
//            if (resultCode == Activity.RESULT_OK) {
//                mSelectPath = ImageSelector.getSelectResults(data)
//                val type= data?.getStringExtra("type")
//                val sb = StringBuilder()
//                if(type.equals("image")){
//                    this.type=1
//                    mSelectPath?.forEach {
//                        sb.append(it)
//                        listPath.add(it)
//                        sb.append("\n")
//                    }
//                    date=""
//                }else if(type.equals("video")){
//                    this.type=2
//                    listPath.clear()
//                    mSelectPath?.forEach {
//                        sb.append(it)
//                        listPath.add(it)
//                        sb.append("\n")
//                    }
//                    video= mSelectPath?.get(0) ?: ""
//                    date= data?.getStringExtra("date").toString()
//                }
//                gvAdapter!!.setLists(listPath,baseContext,date)
//                gridView!!.adapter = gvAdapter
//                Log.e("DiscoverIssue->ImageSelect", "结果： $mSelectPath")
//                Log.e("DiscoverIssue->listPath", "结果： $listPath")
//                Log.e("DiscoverIssue->listPath", "结果： $type")
//                mSelectPath?.clear()
//            }
//        }
    }
    @SuppressLint("InflateParams")
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        println("点击的+号位置$position")
        println("picPath.size()" + listPath.size)
        if (position == listPath.size) { // 点击“+”号位置添加图片
            // 点击“+”号位置添加图片
            issueDefault!!.alpha = 0.7f
            val contentView = LayoutInflater.from(this).inflate(R.layout.issue_select_hint, null, true)
            val window = PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true)
            window.setBackgroundDrawable(null)
            window.isFocusable = true
            window.showAtLocation(this.window.decorView, Gravity.BOTTOM, 0, 0)
            val issueSelectHintPhoto = contentView.findViewById<Button>(R.id.issue_select_hint_photo)
            val issueSelectHintVideo = contentView.findViewById<Button>(R.id.issue_select_hint_video)
            issueSelectHintPhoto.setOnClickListener {//选择了图片
                issueImage()
                window.dismiss()
            }

            issueSelectHintVideo.setOnClickListener {//选择了视频
                getVideo()
                window.dismiss()
            }

            window.setOnDismissListener { issueDefault!!.alpha = 0f }
        } else { // 点击图片删除
            showAlertDialog("提示", "是否删除此图片？", "确定", "取消",
                { dialog, _ ->

                    dialog.dismiss()
                }, { dialog, _ -> dialog.dismiss() })
        }
    }

    private fun getVideo(){
        RxGalleryFinalApi
            .getInstance(this@DiscoverIssueActivity)
            .setType(
                RxGalleryFinalApi.SelectRXType.TYPE_VIDEO,
                RxGalleryFinalApi.SelectRXType.TYPE_SELECT_RADIO
            )
            .setVDRadioResultEvent(object : RxBusResultDisposable<ImageRadioResultEvent?>() {
                @Throws(java.lang.Exception::class)

                override fun onEvent(t: ImageRadioResultEvent?) {
                    if (t != null) {
                        type=2
                        Toast.makeText(
                            applicationContext,
                            t.result.originalPath,
                            Toast.LENGTH_SHORT
                        ).show()
                        listPath.clear()
                        listPath.addAll(listOf(t.result.originalPath))
                        gvAdapter!!.setLists(listPath,baseContext,date)
                        gridView!!.adapter = gvAdapter
                    }
                }
            })
            .open()
    }
    /**
     * 带点击事件的双按钮AlertDialog
     *
     * @param title
     * 弹框标题
     * @param message
     * 弹框消息内容
     * @param positiveButton
     * 弹框第一个按钮的文字
     * @param negativeButton
     * 弹框第二个按钮的文字
     * @param positiveClickListener
     * 弹框第一个按钮的单击事件
     * @param negativeClickListener
     * 弹框第二个按钮的单击事件
     */
    private fun showAlertDialog(
        title: String?, message: String?,
        positiveButton: String?, negativeButton: String?,
        positiveClickListener: DialogInterface.OnClickListener?,
        negativeClickListener: DialogInterface.OnClickListener?
    ) {
        AlertDialog.Builder(this@DiscoverIssueActivity)
            .setCancelable(false).setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButton, positiveClickListener)
            .setNegativeButton(negativeButton, negativeClickListener)
            .show()
    }
}