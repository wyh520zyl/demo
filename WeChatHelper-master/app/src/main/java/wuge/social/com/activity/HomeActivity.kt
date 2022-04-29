package wuge.social.com.activity
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Outline
import android.graphics.Rect
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import com.airbnb.lottie.LottieAnimationView
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.LocationSource
import com.amap.api.maps.LocationSource.OnLocationChangedListener
import com.amap.api.maps.MapView
import com.github.kongpf8848.tkbanner.TKBanner
import com.github.kongpf8848.tkbanner.TKBannerFrameLayout
import kotlinx.android.synthetic.main.home_tab_wuge.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import wuge.social.com.R
import wuge.social.com.extension.dp2px
import wuge.social.com.homepage.DiscoverFragment
import wuge.social.com.homepage.MessageFragment
import wuge.social.com.homepage.SettingsFragment
import wuge.social.com.homepage.WuGeFragment
import wuge.social.com.http.ApiService
import wuge.social.com.http.HiRetrofit
import wuge.social.com.model.*
import wuge.social.com.util.ImageLoader
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : BaseActivity(), View.OnClickListener{
    private val TAG="HomeActivity"
    private var mTabList=arrayOfNulls<RelativeLayout>(4)//底部导航栏的4个导航按钮
    private var mJsonList=arrayOfNulls<LottieAnimationView>(4)//底部导航栏的4个未选状态的json文件控件
    private var mTxtList=arrayOfNulls<TextView>(4)//底部导航栏的4个未选状态的textview
    private var mImageList=arrayOfNulls<ImageView>(4)//底部导航栏的4个未选状态的背景图片
    private val mTabArray = arrayOf(WuGeFragment(),MessageFragment(),DiscoverFragment(),SettingsFragment())//首页的四个页面
    private val mColorArray = arrayOfNulls<Int>(2)//底部导航栏textview的字体颜色
    private var index = 0//用于遍历时的下标
    private var aMap: AMap? = null
    private var mListener: OnLocationChangedListener? = null
    private var mLocationClient: AMapLocationClient? = null
    private var mLocationOption: AMapLocationClientOption? = null
    //退出时的时间
    private var mExitTime: Long = 0
    //主页面的遮罩控件
    var homeShade: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        Log.d("HomeActivity->onCreate", "进入了HomeActivity:${User.user_id}")
        initView()
        getColor()
        initFragment()
        initEvent()
        setSelect(0) //初始值设置为0，默认wuGeFragment为首页
        getLocation()//定位
        getUser()
    }
    private fun initView() {

        //导航栏控件
        homeShade = findViewById(R.id.home_shade)
        (findViewById<View>(R.id.id_tab_WeChat) as RelativeLayout).also { mTabList[0] = it }
        (findViewById<View>(R.id.id_tab_Frd) as RelativeLayout).also { mTabList[1] = it }
        (findViewById<View>(R.id.id_tab_Contacts) as RelativeLayout).also { mTabList[2] = it }
        (findViewById<View>(R.id.id_tab_Settings) as RelativeLayout).also { mTabList[3] = it }
        (findViewById<View>(R.id.wuge_json) as LottieAnimationView).also { mJsonList[0] = it }
        (findViewById<View>(R.id.message_json) as LottieAnimationView).also { mJsonList[1] = it }
        (findViewById<View>(R.id.discover_json) as LottieAnimationView).also { mJsonList[2] = it }
        (findViewById<View>(R.id.my_json) as LottieAnimationView).also { mJsonList[3] = it }
        (findViewById<View>(R.id.txt_wuge) as TextView).also { mTxtList[0] = it }
        (findViewById<View>(R.id.txt_message) as TextView).also { mTxtList[1] = it }
        (findViewById<View>(R.id.txt_discover) as TextView).also { mTxtList[2] = it }
        (findViewById<View>(R.id.txt_my) as TextView).also { mTxtList[3] = it }
        (findViewById<View>(R.id.wuge_image) as ImageView).also { mImageList[0] = it }
        (findViewById<View>(R.id.message_image) as ImageView).also { mImageList[1] = it }
        (findViewById<View>(R.id.discover_image) as ImageView).also { mImageList[2] = it }
        (findViewById<View>(R.id.my_image) as ImageView).also { mImageList[3] = it }
        (findViewById<View>(R.id.my_image) as ImageView)
        getImage()
    }

    private fun initFragment(){
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        for( tab in mTabArray){
            transaction.add(R.id.id_content, tab)
        }
        transaction.commit()
    }

    /**
     * 事件启动函数
     * 全屏监听太耗内存
     * 限制只监听4个LinearLayout
     */
    private fun initEvent() {
        for(mTab in mTabList){
            mTab!!.setOnClickListener(this)
        }
    }
    private fun setSelect(i: Int) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        hideFragment(transaction)
        mColorArray[1]?.let { mTxtList[i]?.setTextColor(it) }
        mImageList[i]?.visibility = View.INVISIBLE
        mJsonList[i]?.visibility = View.VISIBLE
        mJsonList[i]?.playAnimation()
        transaction.show(mTabArray[i])
        transaction.commit()
    }

    /**
     * 将4个fragment先全部隐藏
     */
    private fun hideFragment(transaction: FragmentTransaction) {
        while (index < 4) {//while 循环的遍历方式
            transaction.hide(mTabArray[index])
            mColorArray[0]?.let { mTxtList[index]?.setTextColor(it) }
            mJsonList[index]?.visibility = View.INVISIBLE
            mImageList[index]?.visibility = View.VISIBLE
            index++
        }
        index=0
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.id_tab_WeChat -> if( mJsonList[0]?.visibility==View.INVISIBLE) setSelect(0)
            R.id.id_tab_Frd -> if( mJsonList[1]?.visibility==View.INVISIBLE) setSelect(1)
            R.id.id_tab_Contacts -> if( mJsonList[2]?.visibility==View.INVISIBLE) setSelect(2)
            R.id.id_tab_Settings -> if( mJsonList[3]?.visibility==View.INVISIBLE) setSelect(3)
        }
    }

    //获取颜色
    private fun getColor() {
        mColorArray[0] = ContextCompat.getColor(this,R.color.navigation_text_color_not)
        mColorArray[1] = ContextCompat.getColor(this,R.color.personal_essential_headline)
    }
    private var slideshow: Slideshow? =null
    private var coverPhoto: CoverPhoto? =null

    private fun getImage(){
        val map=HiRetrofit.getParameter(null)
        val apiService : ApiService = HiRetrofit.create(ApiService::class.java)
        apiService.getSlideshow(map)
            .enqueue(object : Callback<Slideshow> {
                override fun onResponse(call: Call<Slideshow>, response: Response<Slideshow>) {
                    Log.d("onResponse->getImage",response.body()?.toString()?:"response is null")
                    slideshow =response.body()
                    val status: Int? =slideshow?.status
                    if(status==1){
                        setBanner(slideshow?.result)
                    }
                }
                override fun onFailure(call: Call<Slideshow>, t: Throwable) {
                    Log.d("Retrofit_onFailure",t.message?:"unknown reason")
                }
            })
        apiService.getCoverPhoto(map)
            .enqueue(object : Callback<CoverPhoto> {
                override fun onResponse(call: Call<CoverPhoto>, response: Response<CoverPhoto>) {
                    Log.d("onResponse->getCoverPhoto",response.body()?.toString()?:"response is null")
                    coverPhoto =response.body()
                    val status: Int? =coverPhoto?.status
                    if(status==1){
                        coverPhoto?.let { setEnclosure(it.result) }
                    }
                }
                override fun onFailure(call: Call<CoverPhoto>, t: Throwable) {
                    Log.d("Retrofit_onFailure",t.message?:"unknown reason")
                }
            })
    }

    //设置轮播图
    private fun setBanner(lists: List<SlideshowResult>?){
//        wuge_banner.setAdapter(ImageAdapter(DataBean.getTestData()))
//        wuge_banner.setIndicator(CircleIndicator(this))
//        wuge_banner.setIndicatorGravity(IndicatorConfig.Direction.CENTER)

        wuge_banner.apply {
            setAutoPlayInterval(5000)//设置轮播间隔
            lists?.let { getBannerData(it) }?.let { setData(models = it) }//设置数据
          //  setOnPageChangeListener(wuge_indicator)//设置ViewPager Page切换事件
            setOnBannerItemClickListener(object : TKBanner.OnBannerItemClickListener<BannerItem> {//设置ViewPager Page点击事件
            override fun onBannerItemClick(banner: TKBanner, model: BannerItem, position: Int) {
                Log.d("TAG", "onBannerItemClick() called with: banner = $banner, model = $model, position = $position")
            }
            })
            setAdapter(object : TKBanner.Adapter<BannerItem> {//填充ViewPager Page
            override fun fillBannerItem(
                banner: TKBanner,
                view: View,
                model: BannerItem,
                position: Int
            ) {
                if (view is TKBannerFrameLayout) {
                    view.setBannerLeftRightMargin(dp2px(16f))
                    view.setBannerTextBottomMargin(dp2px(8f))
                    view.getBannerRelativeLayout().apply {
                        outlineProvider = object : ViewOutlineProvider() {
                            override fun getOutline(view: View?, outline: Outline?) {
                                outline?.setRoundRect(Rect(0, 0, view!!.width, view.height), dp2px(5f).toFloat())
                            }
                        }
                        clipToOutline = true
                    }
                    ImageLoader.getInstance().load(
                        context = this@HomeActivity,
                        url = model.url,
                        imageView = view.getBannerImageView()
                    )
                    view.setBannerTextSize(20f)
                    view.setBannerText(model.title)
                }
            }
            })
        }
        Looper.myQueue().addIdleHandler {
            wuge_indicator.setUp(count = wuge_banner.getRealCount())
            false
        }
    }
    private fun setEnclosure(games: List<CoverPhotoResult>){
        val wug:WuGeFragment= mTabArray[0] as WuGeFragment
        val message:MessageFragment= mTabArray[1] as MessageFragment
        wug.setEnclosureList(this@HomeActivity,games)
        message.setViewPager(supportFragmentManager)
//        setDiscover()
    }

    private fun setDiscover(){
//        val discoverFragment:DiscoverFragment= mTabArray[2] as DiscoverFragment
//        discoverFragment.setViewPager(supportFragmentManager,this)
    }
    //准备轮播图数据
    private fun getBannerData(lists: List<SlideshowResult>): List<Any> {
        val listOf = arrayListOf<BannerItem>()
        for((i, s) in lists.withIndex()){
            val b= BannerItem(
                id = i,
                url = s.slideshow_url,
                title = ""
            )
            listOf.add(i,b)
        }
        return listOf
    }

    private fun getLocation() {
        val mapView=MapView(this)
        if (aMap == null) {
            aMap = mapView.map
        }
        setUpMap()
        aMap?.moveCamera(CameraUpdateFactory.zoomTo(17F))
    }
    private fun setUpMap() {
        aMap!!.setLocationSource(mLocationSource) // 设置定位监听
        aMap!!.isMyLocationEnabled = true // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
    }
    private var mLocationSource: LocationSource = object : LocationSource {
        override fun activate(onLocationChangedListener: OnLocationChangedListener) {
            mListener = onLocationChangedListener
            //初始化定位
            initAmapLocation()
        }
        private fun initAmapLocation() {
            //初始化定位
            mLocationClient = AMapLocationClient( this@HomeActivity)
            //设置定位回调监听
            mLocationClient!!.setLocationListener(mAMapLocationListener)
            //初始化AMapLocationClientOption对象
            mLocationOption = AMapLocationClientOption()
            // 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
            mLocationOption!!.locationPurpose =
                AMapLocationClientOption.AMapLocationPurpose.Transport
            //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
            mLocationOption!!.locationMode =
                AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
            mLocationOption!!.interval = 380000
            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption!!.isNeedAddress = true
            //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
            mLocationOption!!.httpTimeOut = 20000
            if (null != mLocationClient) {
                mLocationClient!!.setLocationOption(mLocationOption)
                //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
                //mLocationClient!!.stopLocation()
                mLocationClient!!.startLocation()
            }
        }
        override fun deactivate() {
            mListener = null
            if (mLocationClient != null) {
                mLocationClient!!.stopLocation()
                mLocationClient!!.onDestroy()
            }
            mLocationClient = null
        }
    }
    var mAMapLocationListener =
        AMapLocationListener { amapLocation ->
            if (amapLocation != null) {
                if (amapLocation.errorCode == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    mListener!!.onLocationChanged(amapLocation) // 显示系统小蓝点,不写这一句无法显示到当前位置
                    amapLocation.locationType //获取当前定位结果来源，如网络定位结果，详见定位类型表
                    amapLocation.latitude //获取纬度
                    amapLocation.longitude //获取经度
                    amapLocation.accuracy //获取精度信息
                    amapLocation.address //地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                    amapLocation.country //国家信息
                    amapLocation.province //省信息
                    amapLocation.city //城市信息
                    amapLocation.district //城区信息
                    amapLocation.street //街道信息
                    amapLocation.streetNum //街道门牌号信息
                    amapLocation.cityCode //城市编码
                    amapLocation.adCode //地区编码
                    amapLocation.aoiName //获取当前定位点的AOI信息
                    amapLocation.buildingId //获取当前室内定位的建筑物Id
                    amapLocation.floor //获取当前室内定位的楼层
                    amapLocation.gpsAccuracyStatus //获取GPS的当前状态
                    amapLocation.locationDetail //定位信息描述
                    amapLocation.bearing //获取方向角信息
                    amapLocation.speed //获取速度信息  单位：米/秒
                    amapLocation.poiName //获取当前位置的POI名称
                    //获取定位时间
                    @SuppressLint("SimpleDateFormat") val sdf = SimpleDateFormat("yyyy-MM-dd")
                    val date = Date(amapLocation.time)
                    sdf.format(date)
                    Log.e(TAG, "获取当前定位结果来源:::" + amapLocation.locationType)
                    Log.e(TAG, "获取纬度:::" + amapLocation.latitude)
                    Log.e(TAG, "获取经度:::" + amapLocation.longitude)
                    Log.e(TAG, "获取精度信息:::" + amapLocation.accuracy)
                    Log.e(TAG, "获取地址:::" + amapLocation.address)
                    Log.e(TAG, "获取国家信息:::" + amapLocation.country)
                    Log.e(TAG, "获取省信息:::" + amapLocation.province)
                    Log.e(TAG, "获取城市信息:::" + amapLocation.city)
                    Log.e(TAG, "获取城市信息:::" + amapLocation.cityCode)
                    Log.e(TAG, "获取城区信息:::" + amapLocation.district)
                    Log.e(TAG, "获取街道信息:::" + amapLocation.street)
                    Log.e(TAG, "获取街道门牌号信息:::" + amapLocation.streetNum)
                    Log.e(TAG, "获取地区编码:::" + amapLocation.adCode)
                    Log.e(TAG, "获取当前定位点的AOI信息:::" + amapLocation.aoiName)
                    Log.e(TAG, "获取当前室内定位的建筑物Id:::" + amapLocation.buildingId)
                    Log.e(TAG, "获取当前室内定位的楼层:::" + amapLocation.floor)
                    Log.e(TAG, "获取GPS的当前状态:::" + amapLocation.gpsAccuracyStatus)
                    Log.e(TAG, "获取定位信息描述:::" + amapLocation.locationDetail)
                    Log.e(TAG, "获取方向角信息:::" + amapLocation.bearing)
                    Log.e(TAG, "获取速度信息:::" + amapLocation.speed + "m/s")
                    Log.e(TAG, "获取海拔高度信息:::" + amapLocation.altitude)
                    Log.e(TAG, "获取当前位置的POI名称:::" + amapLocation.poiName)
                    User.province=amapLocation.province
                    User.city=amapLocation.city
                    User.district=amapLocation.district
                    User.longitude=amapLocation.longitude.toString()
                    User.latitude=amapLocation.latitude.toString()
                    val locationMap=HiRetrofit.getParameter(hashMapOf(
                        "longitude" to User.longitude,
                        "latitude" to User.latitude,
                        "address" to User.city+User.district
                    ))
                    setLocation(locationMap)
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("aMapError", "location Error, ErrCode:" + amapLocation.errorCode + ", errInfo:" + amapLocation.errorInfo)
                }
            }
        }

    //上传定位结果
    private fun setLocation(map:Map<String, String>){
        val apiService : ApiService = HiRetrofit.create(ApiService::class.java)
        apiService.setLocation(map)
            .enqueue(object : Callback<wuge.social.com.model.Response> {
                override fun onResponse(call: Call<wuge.social.com.model.Response>, response: Response<wuge.social.com.model.Response>) {
                    Log.d("setLocation_onResponse",response.body()?.toString()?:"response is null")
                    val body=response.body()
                    if (body != null) {
                        if(body.status==1){//定位成功
                            Log.d("setLocation->onResponse","定位成功")
                        }
                    }
                }
                override fun onFailure(call: Call<wuge.social.com.model.Response>, t: Throwable) {
                    Log.d("setLocation->onFailure",t.message?:"unknown reason")
                }
            })
    }
    fun homeOnClick(v: View) {
        when (v.id) {
            R.id.map_image -> {
                Log.d(TAG, "点击了物格")
                for(game in coverPhoto?.result!!){
                    if(game.gameType==2){
//                        val intent = Intent(this, GameWebViewActivity::class.java)
//                        intent.putExtra("game_url", game.cover_url + "?token=" + User.token + "&user_id=" + User.user_id)
//                        startActivity(intent)
                        break
                    }
                }
            }
            R.id.wuge_sign_in->{
//                val intent = Intent(this, SignInActivity::class.java)
//                startActivity(intent)
            }
            R.id.wuge_task->{
//                val intent = Intent(this, TaskActivity::class.java)
//                startActivity(intent)
            }
            R.id.wuge_qr_code->{
                if(homeShade!!.alpha==0f){
                    setQr()
                }
            }
            R.id.wuge_headimg->{

                val intent = Intent(this, PersonalActivity1::class.java)
                intent.putExtra("toId",User.user_id)
                startActivity(intent)
            }
//            R.id.message_scan->{//二维码扫描
//                startQrCode()
//            }
            R.id.discover_issue_text->{//发布动态
                val intent = Intent(this, DiscoverIssueActivity::class.java)
                startActivity(intent)
            }
        }
    }


    /*
   获取二维码
   */
    private fun setQr() {
//        homeShade!!.alpha = 0.7f
//        @SuppressLint("InflateParams") val view: View = LayoutInflater.from(this).inflate(R.layout.wuge_user_qr, null, false)
//        val sharePop = PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true)
//        sharePop.setBackgroundDrawable(null)
//        sharePop.isFocusable = true
//        sharePop.showAtLocation(window.decorView, Gravity.CENTER, 0, 0)
//        val qrImage = view.findViewById<ImageView>(R.id.qr_image)
//        val qrMyImage = view.findViewById<ImageView>(R.id.qr_my_image)
//        val logoImage = view.findViewById<ImageView>(R.id.logo_image)
//        qrImage.setImageBitmap(EncodingHandler.createQRCode(User.user_id, 400, 400))
//
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
//            homeShade!!.alpha = 0f
//        }
    }
    private fun startQrCode() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
//                Toast.makeText(this, "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_LONG).show()
//            }
//            // 申请权限
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), Constant.REQ_PERM_CAMERA)
//            return
//        }
//        // 二维码扫码
//        val intent = Intent(this, CaptureActivity::class.java)
//        startActivityForResult(intent, Constant.REQ_QR_CODE)
    }

    //对返回键进行监听
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
            exit()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun exit() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            Toast.makeText(this, "再按一次退出当前应用程序", Toast.LENGTH_SHORT).show()
            mExitTime = System.currentTimeMillis()
        } else {
            ActivityCollector.finishAll()
            android.os.Process.killProcess(android.os.Process.myPid())
        }
    }
    fun getUser(){
        val map=HiRetrofit.getParameter(null)
        val apiService : ApiService = HiRetrofit.create(ApiService::class.java)
        apiService.getUser(map)
            .enqueue(object : Callback<UserModel> {
                override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                    Log.d("getUser",response.body()?.toString()?:"response is null")
                    val body=response.body()
                    if (body != null) {
                        if(body.code==1){//定位成功
                            Log.d("getUser->onResponse","定位成功")
                        }
                    }
                }
                override fun onFailure(call: Call<UserModel>, t: Throwable) {
                    Log.d("getUser->onFailure",t.message?:"unknown reason")
                }
            })
    }
}