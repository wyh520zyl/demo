package wuge.social.com.activity


import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.DecelerateInterpolator
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.tencent.imsdk.v2.V2TIMConversation
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import wuge.social.com.R
import wuge.social.com.http.*
import wuge.social.com.model.Response1
import wuge.social.com.model.User
import wuge.social.com.util.ScaleTransitionPagerTitleView
import wuge.social.com.util.Sha1Util
import wuge.social.com.util.Timestamp
import wuge.social.com.view.MarqueTextView
import java.io.File
import java.util.*

class PersonalActivity1 : BaseActivity() {
    var broadcastReceiver: UpdateUIBroadcastReceiver? = null
    private var personalRegion: ImageView? = null
    private var personalShade: ImageView? = null//遮罩
    private var personalIndicator: MagicIndicator? = null
    private var personalRelation: FrameLayout? = null
    private var personalViewpager: ViewPager? = null
    private var personalInformationSetText: TextView? = null
    private var personalRelationBtn: TextView? = null
    private val personalDataList = listOf("基本信息", "动态")
    private val UNSELECTED = -0x555556 //未选择的字体颜色
    private val UNDERLINE = -0xcd9409 //下划线的字体颜色
    private var toId: String? = null
    private var codeUser: CodeUserResult? = null
    private var yourself = false//是否是自己
    private var isAlterUser=false//是否修改了个人信息
    private var isDown=false//是否按下了
    private var time=0L
    private var sharePop: PopupWindow? = null

    companion object {
        val ACTION_UPDATEUI = "action.updateUI"
        private const val REQUEST_IMAGE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal1)
        registrationRadio()
        toId = intent.getStringExtra("toId")
        yourself=toId==User.user_id
        init()
        getToId()
        setMessageInitMagicIndicator()
    }
    private fun registrationRadio() {
        val filter = IntentFilter()
        filter.addAction(ACTION_UPDATEUI)
        broadcastReceiver = UpdateUIBroadcastReceiver()
        registerReceiver(broadcastReceiver, filter)
    }
    @SuppressLint("ClickableViewAccessibility")
    private fun init() {
        personalShade=findViewById(R.id.personal_shade)
        personalRegion=findViewById(R.id.personal_region)
        personalIndicator=findViewById(R.id.personal_indicator)
        personalViewpager=findViewById(R.id.personal_viewpager)
        personalRelation=findViewById(R.id.personal_relation)
        personalRelationBtn=findViewById(R.id.personal_relation_btn)
        personalInformationSetText=findViewById(R.id.personal_information_set_text)
        personalIndicator = findViewById<View>(R.id.personal_indicator) as MagicIndicator
        if(yourself){
            personalRegion!!.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        isDown=true
                        time = System.currentTimeMillis()
                        object : Thread() {
                            override fun run() {
                                //这儿是耗时操作，完成之后更新UI；
                                sleep(500)
                                if(isDown){
                                    runOnUiThread {
                                        setWall()
                                    }
                                }
                            }
                        }.start()
                    }
                    MotionEvent.ACTION_UP -> {
                        isDown=false
                      val thisTime = System.currentTimeMillis()
                      if(thisTime-time<490){
                          val list= arrayListOf(codeUser!!.wall_image)
//                          ImagePreview.getInstance()
//                              .setContext(this)
//                              .setIndex(0)
//                              .setErrorPlaceHolder(R.drawable.load_failed)
//                              .setImageList(list)
//                              .start()
                      }
                    }
                }
                return@OnTouchListener true
            })
        }else{
            personalRegion!!.setOnClickListener {
                val list = arrayListOf(codeUser!!.wall_image)
//                ImagePreview.getInstance()
//                    .setContext(this)
//                    .setIndex(0)
//                    .setErrorPlaceHolder(R.drawable.load_failed)
//                    .setImageList(list)
//                    .start()
            }
        }
    }

    fun personalOnclick( v : View ){
        when(v.id){
            R.id.information_return->{
                finish()
            }
            R.id.personal_information_set->{//设置或者举报
                if(yourself){//是自己进入设置界面
                    val intent = Intent(this, PersonalSetActivity::class.java)
                    startActivity(intent)
                }else{//不是自己进入举报界面
//                    if(codeUser!!.friend_type==2){//是好友进入好友设置页面
//                        //是好友
//                        val intent = Intent(this, PersonalFriendSetActivity()::class.java)
//                        val gson= Gson()
//                        val codeUserGson =gson.toJson(codeUser)
//                        intent.putExtra("codeUser",codeUserGson)
//                        startActivity(intent)
//                    }else{//不是好友,进入举报页面
//                        val intent = Intent(this, FriendReportActivity::class.java)
//                        intent.putExtra("friend_id", codeUser!!.id)
//                        startActivity(intent)
//                    }
                }
            }
            R.id.personal_relation_btn->{
                if(codeUser!!.friend_type==2){//是好友进入聊天页面
                    //进入聊天界面
                    val intent = Intent(this, ImActivity::class.java)
                    intent.putExtra("type", V2TIMConversation.V2TIM_C2C)
                    intent.putExtra("id", codeUser!!.id)
                    intent.putExtra("name", codeUser!!.nickname)
                    startActivity(intent)
                }else if(codeUser!!.friend_type==3){//不是好友,申请添加好友
                    if(codeUser!!.social_value.toInt()>0){
                        addFriend()
                    }else{
                        addFriends()
                    }
                }
            }
            R.id.personal_region->{

            }
        }
    }
    private fun setWall(){
        personalShade!!.alpha = 0.7f
        @SuppressLint("InflateParams") val view = LayoutInflater.from(this).inflate(R.layout.set_gender, null, false)
        sharePop = PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true)
        sharePop!!.setBackgroundDrawable(null)
        sharePop!!.isFocusable = true
        sharePop!!.showAtLocation(window.decorView, Gravity.BOTTOM, 0, 0)
        val informationGenderSetBoy = view.findViewById<TextView>(R.id.information_gender_set_boy)
        val informationGenderSetGirl = view.findViewById<TextView>(R.id.information_gender_set_girl)
        val informationGenderSetCancel = view.findViewById<TextView>(R.id.information_gender_set_cancel)
        informationGenderSetBoy.visibility = View.GONE
        informationGenderSetGirl.text = "更换背景"
        informationGenderSetGirl.setOnClickListener {
            false.pickImage()
            sharePop!!.dismiss()
        }
        informationGenderSetCancel.setOnClickListener { sharePop!!.dismiss() }
        sharePop!!.setOnDismissListener {
            personalShade!!.alpha = 0f
        }
    }

    private fun Boolean.pickImage(){
//        val showCamera = true
//        val maxNum = 1
//        val imageSpanCount = 3
//        val theme = R.style.sl_theme_dark
//
//        ImageSelector.startImageAction(
//            this@PersonalActivity1, REQUEST_IMAGE,
//            MediaSelectConfig.Builder()
//                .setSelectMode(SelectMode.MODE_SINGLE)
//                .setOriginData(mSelectPath)
//                .setTheme(theme)
//                .setShowCamera(showCamera)
//                .setPlaceholderResId(R.mipmap.ic_launcher)
//                .setOpenCameraOnly(this)
//                .setMaxCount(maxNum)
//                .setImageSpanCount(imageSpanCount)
//                .build()
//        )
    }

    private var mSelectPath: ArrayList<String>? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_IMAGE) {
//            if (resultCode == Activity.RESULT_OK) {
//                mSelectPath = ImageSelector.getSelectResults(data)
//                val sb = StringBuilder()
//                mSelectPath?.forEach {
//                    sb.append(it)
//                    sb.append("\n")
//                }
//                Log.e("ImageSelect", "结果： $mSelectPath")
//                mSelectPath?.get(0)?.let { setWallImage(it) }
//            }
//        }
    }

    private fun setWallImage(path: String) {
//        upload.SetHead()
        val file= File(path)
        val photo= file.asRequestBody(path.toMediaTypeOrNull())
        val photoPart = MultipartBody.Part.createFormData("wall_image",file.name,photo)
        val userId= User.user_id.toRequestBody("text/plain".toMediaTypeOrNull())
        val token= User.token.toRequestBody("text/plain".toMediaTypeOrNull())
        val timeStamp= Timestamp.getSecondTimestamp(Date()).toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val apiSign= Sha1Util.shaEncrypt(Timestamp.getTimeStamp()).toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val apiService : ApiService = HiRetrofit.create(ApiService::class.java)
        apiService.userInfoUpd(
            userId,
            token,
            timeStamp,
            apiSign,
            photoPart
        ).enqueue(object : Callback<wuge.social.com.model.Response1> {
            override fun onResponse(call: Call<wuge.social.com.model.Response1>, response: Response<wuge.social.com.model.Response1>) {
                Log.d("onResponse->getImage",response.body()?.toString()?:"response is null")
                val res=response.body()
                if(res!!.status==1){//修改背景墙成功
                    val wallImage=res.result.data
                    Log.d("onResponse->wall_image","wall_image=$wallImage")
                    codeUser!!.wall_image= wallImage
                    Glide.with(applicationContext)
                        .load(codeUser!!.wall_image)
                        .thumbnail(Glide.with(applicationContext).load(R.mipmap.map_default))
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
                        .into(personalRegion!!)
                }
            }
            override fun onFailure(call: Call<wuge.social.com.model.Response1>, t: Throwable) {
                Log.d("Retrofit_onFailure",t.message?:"unknown reason")
            }
        })
    }
    private fun setUser(resultUri: Uri) {
//        upload.SetHead()
        val file=File(resultUri.path)
        val photo= RequestBody.create(resultUri.path?.toMediaTypeOrNull(),file)
        val photoPart = MultipartBody.Part.createFormData("headImg",file.name,photo)
        val userId= RequestBody.create("text/plain".toMediaTypeOrNull(),User.user_id)
        val token= RequestBody.create("text/plain".toMediaTypeOrNull(),User.token)
        val timeStamp= RequestBody.create("text/plain".toMediaTypeOrNull(),Timestamp.getSecondTimestamp(Date()).toString())
        val apiSign= RequestBody.create("text/plain".toMediaTypeOrNull(),Sha1Util.shaEncrypt(Timestamp.getTimeStamp()).toString())
        val apiService : ApiService = HiRetrofit.create(ApiService::class.java)
        apiService.userInfoUpd(
            userId,
            token,
            timeStamp,
            apiSign,
            photoPart
        ).enqueue(object : Callback<Response1> {
            override fun onResponse(call: Call<Response1>, response: Response<Response1>) {
                Log.d("onResponse->getImage",response.body()?.toString()?:"response is null")
                val res=response.body()
                if(res!!.status==1){//修改成功
                    val heading=res.result.data
                    Log.d("onResponse->heading","heading=$heading")
                    User.headImg= heading
                }
            }
            override fun onFailure(call: Call<Response1>, t: Throwable) {
                Log.d("Retrofit_onFailure",t.message?:"unknown reason")
            }
        })
    }
    private fun getToId(){
        val map=HiRetrofit.getParameter(hashMapOf("to_id" to toId!!))
        val apiService : ApiService = HiRetrofit.create(ApiService::class.java)
        apiService.queryUser(map)
            .enqueue(object : Callback<CodeUser> {
                override fun onResponse(call: Call<CodeUser>, response: Response<CodeUser>){
                    Log.d("PersonalActivity_getToId",response.body()?.toString()?:"response is null")
                    codeUser =response.body()!!.result
                    setCodeUser()
                }

                override fun onFailure(call: Call<CodeUser>, t: Throwable) {
                    Log.d("Retrofit_onFailure",t.message?:"unknown reason")
                }
            })
    }

    private fun setCodeUser(){
        Glide.with(applicationContext)
            .load(codeUser!!.wall_image)
            .thumbnail(Glide.with(applicationContext).load(R.mipmap.map_default))
            .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
            .into(personalRegion!!)
        if(yourself){
            personalRelation!!.visibility=View.GONE
            personalInformationSetText!!.text="设置"

        }else{
            personalRelation!!.visibility=View.VISIBLE
            personalInformationSetText!!.text="举报"
            if(codeUser!!.friend_type==2){//是好友
                personalRelationBtn!!.text="发消息"
                personalInformationSetText!!.text = "设置"
                personalRelationBtn!!.setBackgroundResource(R.drawable.sign_in_btn_ok)
            }else if(codeUser!!.friend_type==3){//不是好友
                personalRelationBtn!!.text="加好友"
                personalInformationSetText!!.text = "举报"
                personalRelationBtn!!.setBackgroundResource(R.drawable.sign_in_btn_no)
            }
        }
        personalViewpager!!.adapter= PageAdapter(supportFragmentManager,codeUser!!)
    }


    @SuppressLint("SetTextI18n")
    fun addFriend() {
        personalShade!!.alpha = 0.7f
        @SuppressLint("InflateParams")
        val contentView: View = LayoutInflater.from(this).inflate(R.layout.personal_add_friend, null, true)
        val window = PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true)
        window.setBackgroundDrawable(null)
        window.isFocusable = true
        window.showAtLocation(getWindow().decorView, Gravity.CENTER, 0, 0)
        val personalAddFriendHint = contentView.findViewById<TextView>(R.id.personal_add_friend_hint)
        val personalAddFriendCancel = contentView.findViewById<TextView>(R.id.personal_add_friend_cancel)
        val personalAddFriendConfirm = contentView.findViewById<TextView>(R.id.personal_add_friend_confirm)
        personalAddFriendHint.text = "添加 " + codeUser!!.nickname + " 为好友需要花费 " + codeUser!!.social_value + " 金币！"
        personalAddFriendCancel.setOnClickListener { window.dismiss() }
        personalAddFriendConfirm.setOnClickListener {
            window.dismiss()
            addFriends()}
        window.setOnDismissListener { personalShade!!.alpha = 0f }
    }
   private fun addFriends(){
        val map=HiRetrofit.getParameter(hashMapOf("friend_id" to toId!!))
        val apiService : ApiService = HiRetrofit.create(ApiService::class.java)
       apiService.addUserFriend(map)
            .enqueue(object : Callback<wuge.social.com.model.Response> {
                override fun onResponse(call: Call<wuge.social.com.model.Response>, response: Response<wuge.social.com.model.Response>){
                    val res : wuge.social.com.model.Response=response.body()!!
                    if(res.status==1){
                        personalRelationBtn!!.text="发消息"
                        personalInformationSetText!!.text = "设置"
                        personalRelationBtn!!.setBackgroundResource(R.drawable.sign_in_btn_ok)
                        codeUser!!.friend_type=2
                    }
                }
                override fun onFailure(call: Call<wuge.social.com.model.Response>, t: Throwable) {
                    Log.d("Retrofit_onFailure",t.message?:"unknown reason")
                }
            })
    }
    private class PageAdapter(fragmentManager: FragmentManager,private var codeUser: CodeUserResult) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment = PersonalFragment.newInstance(position ,codeUser)
        override fun getCount(): Int { return 2 }
        override fun getPageTitle(position: Int): CharSequence? { //设置标题
            when (position) {
                0 -> return "基本信息"
                1 -> return "动态"
            }
            return null
        }
    }

    class PersonalFragment : Fragment() {
        private var mParam = 0 //用来表示当前需要展示的是哪一页
        private lateinit var taskGridView: View
        companion object {//personal
        private lateinit var mCodeUser: CodeUserResult
        private const val PERSONAL_TERMS_RESULT = "personal_TERMS_RESULT"

            fun newInstance(position: Int, codeUser: CodeUserResult) = PersonalFragment().apply {
                arguments = Bundle().apply {
                    putInt(PERSONAL_TERMS_RESULT, position)
                    mCodeUser=codeUser
                }
            }
        }
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            if (arguments != null) {
                mParam = arguments!!.getInt(PERSONAL_TERMS_RESULT)
            }
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            when (mParam) {
                0 -> {
                    taskGridView = inflater.inflate(R.layout.personal_information_item, container, false)
                    setCodeUser()
                }
                1 -> {
                    taskGridView = inflater.inflate(R.layout.personal_dynamic, container, false)
                }
            }
            return taskGridView
        }

        private fun setCodeUser(){
            val personalNickname = taskGridView.findViewById<MarqueTextView>(R.id.personal_nickname)
            val personalAddress = taskGridView.findViewById<MarqueTextView>(R.id.personal_address)
            val personalId = taskGridView.findViewById<TextView>(R.id.personal_id)
            val personalAge = taskGridView.findViewById<TextView>(R.id.personal_age)
            val personalCharm = taskGridView.findViewById<TextView>(R.id.personal_charm)
            val personalStatus = taskGridView.findViewById<TextView>(R.id.personal_status)
            val personalConstellation = taskGridView.findViewById<TextView>(R.id.personal_constellation)
            val personalGrade = taskGridView.findViewById<TextView>(R.id.personal_grade)
            val personalSignature = taskGridView.findViewById<TextView>(R.id.personal_signature)
            val personalGender = taskGridView.findViewById<ImageView>(R.id.personal_gender)
            val personalEssentialListview = taskGridView.findViewById<GridView>(R.id.personal_essential_listview)
            var personalPhoto = taskGridView.findViewById<FrameLayout>(R.id.personal_photo)
            val personalPhotoDefault = taskGridView.findViewById<ImageView>(R.id.personal_photo_default)
            if(mCodeUser.note_name.isNotEmpty()){
                personalNickname!!.text=mCodeUser.note_name
            }else{
                personalNickname!!.text=mCodeUser.nickname
            }
            personalAddress!!.text=mCodeUser.address
            personalId!!.text=mCodeUser.id
            personalAge!!.text=mCodeUser.age.toString()
            personalCharm!!.text=mCodeUser.charm_value
            personalStatus!!.text=mCodeUser.nearby_status
            personalConstellation!!.text=mCodeUser.constellation
            personalGrade!!.text=mCodeUser.levels
            personalSignature!!.text=mCodeUser.signature
            when(mCodeUser.sex.toInt()){//设置性别图片
                0->{
                    personalGender!!.visibility=View.INVISIBLE
                }
                1->{
                    personalGender!!.visibility=View.VISIBLE
                    personalGender.setImageDrawable(ContextCompat.getDrawable(personalGender.context, R.mipmap.wuge_friend_play_item_boy))
                }
                2->{
                    personalGender!!.visibility=View.VISIBLE
                    personalGender.setImageDrawable(ContextCompat.getDrawable(personalGender.context, R.mipmap.wuge_friend_play_item_girl))
                }
            }
            if (mCodeUser.imgs.isEmpty()) {
                personalPhotoDefault.visibility = View.VISIBLE
                personalEssentialListview.visibility = View.GONE
            } else {
                Log.d("PersonalActivity1->setCodeUser","image={$mCodeUser}")
                personalPhotoDefault.visibility = View.GONE
                personalEssentialListview.visibility = View.VISIBLE
                //图片查看器借用BigImageViewPager-master
                personalEssentialListview.adapter= context?.let { PersonalEssentialAdapter(it,mCodeUser.imgs) }
                setListViewHeightBasedOnChildren(personalEssentialListview)//动态设置GridView高度
                personalEssentialListview.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
//                    ImagePreview.getInstance()
//                        .setContext(context!!)
//                        .setIndex(position)
//                        .setErrorPlaceHolder(R.drawable.load_failed)
//                        .setImageList(mCodeUser.imgs)
//                        .start()
                }
            }

        }
        //动态设置GridView高度
        private fun setListViewHeightBasedOnChildren(listView: GridView) {
            // 获取listview的adapter
            val listAdapter = listView.adapter ?: return
            // 固定列宽，有多少列
            val col = 2 // listView.getNumColumns();
            var totalHeight = 0
            // i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，
            // listAdapter.getCount()小于等于8时计算两次高度相加
            var i = 0
            while (i < listAdapter.count) {

                // 获取listview的每一个item
                val listItem = listAdapter.getView(i, null, listView)
                listItem.measure(0, 0)
                // 获取item的高度和
                totalHeight += listItem.measuredHeight
                i += col
            }
            // 获取listview的布局参数
            val params = listView.layoutParams
            // 设置高度
            params.height = totalHeight
            // 设置margin
            (params as ViewGroup.MarginLayoutParams).setMargins(10, 10, 10, 10)
            // 设置参数
            listView.layoutParams = params
        }
    }
class PersonalEssentialAdapter(var context: Context, private var lists:List<String>):BaseAdapter(){
    var imageView:ImageView?=null
    override fun getCount(): Int {
        return lists.size
    }

    override fun getItem(position: Int): Any {
        return lists[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View?
        if(convertView==null){
            imageView=ImageView(context)
            view = LayoutInflater.from(context).inflate(R.layout.item_image,null)
            Log.d("EnclosureAdapter", lists[position])
            imageView=view.findViewById(R.id.iv_thum)
            view.tag=imageView
        }else{
            view =convertView
            imageView= view.tag as ImageView?
        }
        imageView?.let {
            Glide.with(context)
                .load(lists[position])
                .thumbnail(Glide.with(context).load(R.mipmap.map_default))
                .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
                .into(imageView!!)
        }
        return view!!
    }
}

    private fun setMessageInitMagicIndicator() {
        val commonNavigator = CommonNavigator(applicationContext)
        commonNavigator.isAdjustMode = true
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return personalDataList.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val simplePagerTitleView: SimplePagerTitleView =
                    ScaleTransitionPagerTitleView(context)
                simplePagerTitleView.normalColor = UNSELECTED
                simplePagerTitleView.selectedColor = Color.BLACK
                simplePagerTitleView.text = personalDataList[index]
                simplePagerTitleView.textSize = 24f
                simplePagerTitleView.setOnClickListener { personalViewpager!!.currentItem = index }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val linePagerIndicator = LinePagerIndicator(context)
                linePagerIndicator.endInterpolator = DecelerateInterpolator(1.6f)
                linePagerIndicator.mode = LinePagerIndicator.MODE_WRAP_CONTENT
                linePagerIndicator.setColors(UNDERLINE)
                return linePagerIndicator
            }
        }
        personalIndicator!!.navigator = commonNavigator
        ViewPagerHelper.bind(personalIndicator, personalViewpager)
    }
    /**
     * 定义广播接收器（内部类）
     *
     * @author lenovo
     */
    class UpdateUIBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d("PersonalActivity1->UpdateUIBroadcastReceiver->onReceive","开始修改数据")
            (context as PersonalActivity1).isAlterUser=true
            val url = Objects.requireNonNull(intent.extras)!!.getString("headimg")
            val uri=Uri.parse(url)
            context.setUser(uri)
            context.getToId()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 注销广播
        unregisterReceiver(broadcastReceiver)
        if(isAlterUser){

        }
    }
}