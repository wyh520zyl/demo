package wuge.social.com.homepage

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.view.animation.DecelerateInterpolator
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
//import androidx.recyclerview.widget.DefaultItemAnimator
//import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
//import com.airbnb.lottie.LottieAnimationView
//import com.bumptech.glide.Glide
//import com.google.gson.Gson
//import net.lucode.hackware.magicindicator.MagicIndicator
//import net.lucode.hackware.magicindicator.ViewPagerHelper
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
import wuge.social.com.R
//import wuge.social.com.activity.DiscoverItemParticularsActivity
//import wuge.social.com.activity.HomeActivity
//import wuge.social.com.activity.PersonalActivity1
//import wuge.social.com.homepage.DiscoverFragment.PlaceholderFragment.DiscoverAdapter.GridHolder
//import wuge.social.com.http.ApiService
//import wuge.social.com.http.HiRetrofit
//import wuge.social.com.lfrecycerview.LFRecyclerView
//import wuge.social.com.model.Discover
//import wuge.social.com.model.DiscoverData
//import wuge.social.com.model.Image
//import wuge.social.com.model.Response2
//import wuge.social.com.util.ReuseUtil
//import wuge.social.com.util.ScaleTransitionPagerTitleView
import wuge.social.com.view.CircleImageView
import wuge.social.com.view.CustomImageView
import wuge.social.com.view.NineGridlayout
import wuge.social.com.view.ScreenTools
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList


class DiscoverFragment : Fragment(){
    lateinit var v:View
   // private lateinit var magicIndicator: MagicIndicator
    private val discoverDataList = listOf("广场", "好友", "附近")
    private val UNSELECTED = -0x555556 //未选择的字体颜色
    private val UNDERLINE = -0xcd9409 //下划线的字体颜色
    private var discoverViewpager: ViewPager? = null
    private var discoverIssueText: TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        v=inflater.inflate(R.layout.home_tab_discover, container, false)
        discoverViewpager = v.findViewById(R.id.discover_viewpager)
        discoverIssueText = v.findViewById(R.id.discover_issue_text)
        return v
    }

//    fun setViewPager(fragmentManager: FragmentManager,context: Context) {
//        magicIndicator = v.findViewById(R.id.discover_indicator)
//        discoverViewpager!!.addOnPageChangeListener(this)
//        discoverViewpager?.adapter = discoverViewpager?.let { PageAdapter(fragmentManager) }
//        discoverViewpager!!.offscreenPageLimit = 3;
//        setMessageInitMagicIndicator(context)
//    }
//    class PlaceholderFragment : Fragment() , LFRecyclerView.LFRecyclerViewListener,
//        LFRecyclerView.LFRecyclerViewScrollChange{
//        private var mParam = 0 //用来表示当前需要展示的是哪一页
//        private lateinit var taskGridView: View
//        private var discoverSquareLFRecyclerView: LFRecyclerView? = null
//        private var discoverFriendsLFRecyclerView:LFRecyclerView? = null
//        private var discoverNearbyLFRecyclerView:LFRecyclerView? = null
//        private var discoverSquareImage: ImageView? = null
//        private var discoverNearbyImage:ImageView? = null
//        private var discoverFriendsImage:ImageView? = null
//        private var squarePage=1//发现页面的当前动态页
//        private var friendsPage=1
//        private var nearbyPage=1
//        private var squareList:ArrayList<DiscoverData>?= ArrayList()//广场页面的动态列表
//        private var friendsList:ArrayList<DiscoverData>?= ArrayList()
//        private var nearbyList:ArrayList<DiscoverData>?= ArrayList()
//        private var squareRefresh=true//是否是第一次加载或者正在刷新数据
//        private var friendsRefresh=true
//        private var nearbyRefresh=true
//        //private val discoverNum=20
//
//        override fun onCreate(savedInstanceState: Bundle?) {
//            super.onCreate(savedInstanceState)
//            if (arguments != null) {
//                mParam = arguments!!.getInt(SOLAR_TERMS_RESULT)
//            }
//        }
//
//
//        @SuppressLint("SetTextI18n")
//        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
//            when (mParam) {
//                0 -> {
//                    taskGridView= inflater.inflate(R.layout.discover_square, container, false)
//                    initSquare()
//                }
//                1 -> {
//                    taskGridView= inflater.inflate(R.layout.discover_friends, container, false)
//                    initFriends()
//                }
//                2 -> {
//                    taskGridView= inflater.inflate(R.layout.discover_nearby, container, false)
//                    initNearby()
//                }
//            }
//            return taskGridView
//        }
//
//        private fun initSquare(){
//            if(discoverSquareLFRecyclerView==null){
//                discoverSquareLFRecyclerView= taskGridView.findViewById(R.id.discover_square_LFRecyclerView)
//                discoverSquareImage = taskGridView.findViewById(R.id.discover_square_image)
//                getDiscoverSquare(2,squarePage)
//            }
//        }
//
//        private fun initFriends(){
//            if(discoverFriendsLFRecyclerView==null){
//                discoverFriendsLFRecyclerView= taskGridView.findViewById(R.id.discover_friends_list)
//                discoverFriendsImage = taskGridView.findViewById(R.id.discover_friends_image)
//                getDiscoverSquare(3,friendsPage)
//            }
//        }
//
//        private fun initNearby(){
//            if(discoverNearbyLFRecyclerView==null){
//                discoverNearbyLFRecyclerView= taskGridView.findViewById(R.id.discover_nearby_list)
//                discoverNearbyImage = taskGridView.findViewById(R.id.discover_nearby_image)
//                getDiscoverSquare(4,nearbyPage)
//            }
//        }
//
//
//        private fun getDiscoverSquare(dynamicType:Int,page:Int){
//            val map= HiRetrofit.getParameter(hashMapOf("page" to page.toString(),
//                "page_num" to "10","to_id" to "","dynamic_type" to dynamicType.toString()))
//            val apiService : ApiService = HiRetrofit.create(ApiService::class.java)
//            apiService.getDynamicList(map)
//                .enqueue(object : Callback<Discover> {
//                    override fun onResponse(call: Call<Discover>, response: Response<Discover>) {
//                        Log.d("getDiscoverSquare->onResponse",response.body()?.toString()?:"response is null")
//                        val discover: Discover? =response.body()
//                        if(discover!!.status==1){
//                            if(discover.result.per_page>0){
//                                when(dynamicType){
//                                    2->{
//                                        if(squareRefresh){
//                                            val list: List<DiscoverData> = discover.result.data
//                                            squareList= list as ArrayList<DiscoverData>?
//                                            setDiscoverSquare(dynamicType,squareList!!)
//                                            discoverSquareLFRecyclerView!!.adapter!!.notifyItemInserted(0)
//                                            discoverSquareLFRecyclerView!!.adapter!!.notifyItemRangeChanged(0, squareList!!.size)
//                                        }else{
//                                            squareList!!.addAll(discover.result.data)
//                                            discoverSquareLFRecyclerView!!.adapter?.notifyItemRangeInserted(squareList!!.size-1,1)
//                                        }
//                                        if(discover.result.data.size<10){
//                                            discoverSquareLFRecyclerView!!.setLoadMore(false)
//                                        }
//                                    }
//                                    3->{
//                                        if(friendsRefresh){
//                                            val list: List<DiscoverData> = discover.result.data
//                                            friendsList= list as ArrayList<DiscoverData>?
//                                            setDiscoverSquare(dynamicType,friendsList!!)
//                                            discoverFriendsLFRecyclerView!!.adapter!!.notifyItemInserted(0)
//                                            discoverFriendsLFRecyclerView!!.adapter!!.notifyItemRangeChanged(0, friendsList!!.size)
//                                        }else{
//                                            friendsList!!.addAll(discover.result.data)
//                                            discoverFriendsLFRecyclerView!!.adapter?.notifyItemRangeInserted( friendsList!!.size-1,1)
//                                        }
//                                        if(discover.result.data.size<10){
//                                            discoverFriendsLFRecyclerView!!.setLoadMore(false)
//                                        }
//                                    }
//                                    4->{
//                                        if(nearbyRefresh){
//                                            val list: List<DiscoverData> = discover.result.data
//                                            nearbyList= list as ArrayList<DiscoverData>?
//                                            setDiscoverSquare(dynamicType,nearbyList!!)
//                                            discoverNearbyLFRecyclerView!!.adapter!!.notifyItemInserted(0)
//                                            discoverNearbyLFRecyclerView!!.adapter!!.notifyItemRangeChanged(0, nearbyList!!.size)
//                                        }else{
//                                            nearbyList!!.addAll(discover.result.data)
//                                            discoverNearbyLFRecyclerView!!.adapter?.notifyItemRangeInserted( nearbyList!!.size-1,1)
//                                        }
//                                        if(discover.result.data.size<10){
//                                            discoverNearbyLFRecyclerView!!.setLoadMore(false)
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    override fun onFailure(call: Call<Discover>, t: Throwable) {
//                        Log.d("getDiscoverSquare->onFailure",t.message?:"unknown reason")
//                    }
//                })
//        }
//        fun setDiscoverSquare(dynamicType:Int,list:List<DiscoverData>) {
//            if (list.isNotEmpty()) {
//                when(dynamicType){
//                    2->{
//                        discoverSquareLFRecyclerView!!.setLoadMore(true)
//                        discoverSquareLFRecyclerView!!.setRefresh(true)
//                        discoverSquareLFRecyclerView!!.setNoDateShow()
//                        discoverSquareLFRecyclerView!!.setAutoLoadMore(true)
//                        discoverSquareLFRecyclerView!!.setLFRecyclerViewListener(this@PlaceholderFragment)
//                        discoverSquareLFRecyclerView!!.setScrollChangeListener(this@PlaceholderFragment)
//                        discoverSquareLFRecyclerView!!.itemAnimator = DefaultItemAnimator()
//                        discoverSquareLFRecyclerView!!.adapter = DiscoverAdapter(context!!,list)
//                        discoverSquareLFRecyclerView!!.visibility=View.VISIBLE
//                        discoverSquareImage!!.visibility=View.GONE
//                    }
//                    3->{
//                        discoverFriendsLFRecyclerView!!.setLoadMore(true)
//                        discoverFriendsLFRecyclerView!!.setRefresh(true)
//                        discoverFriendsLFRecyclerView!!.setNoDateShow()
//                        discoverFriendsLFRecyclerView!!.setAutoLoadMore(true)
//                        discoverFriendsLFRecyclerView!!.setLFRecyclerViewListener(this@PlaceholderFragment)
//                        discoverFriendsLFRecyclerView!!.setScrollChangeListener(this@PlaceholderFragment)
//                        discoverFriendsLFRecyclerView!!.itemAnimator = DefaultItemAnimator()
//                        discoverFriendsLFRecyclerView!!.adapter = DiscoverAdapter(context!!,list)
//                        discoverFriendsLFRecyclerView!!.visibility=View.VISIBLE
//                        discoverFriendsImage!!.visibility=View.GONE
//                    }
//                    4->{
//                        discoverNearbyLFRecyclerView!!.setLoadMore(true)
//                        discoverNearbyLFRecyclerView!!.setRefresh(true)
//                        discoverNearbyLFRecyclerView!!.setNoDateShow()
//                        discoverNearbyLFRecyclerView!!.setAutoLoadMore(true)
//                        discoverNearbyLFRecyclerView!!.setLFRecyclerViewListener(this@PlaceholderFragment)
//                        discoverNearbyLFRecyclerView!!.setScrollChangeListener(this@PlaceholderFragment)
//                        discoverNearbyLFRecyclerView!!.itemAnimator = DefaultItemAnimator()
//                        discoverNearbyLFRecyclerView!!.adapter = DiscoverAdapter(context!!,list)
//                        discoverNearbyLFRecyclerView!!.visibility=View.VISIBLE
//                        discoverNearbyImage!!.visibility=View.GONE
//                    }
//                }
//            }
//        }
//        companion object {
//            private const val SOLAR_TERMS_RESULT = "SOLAR_TERMS_RESULT"
//            fun newInstance(position: Int) = PlaceholderFragment().apply {
//                arguments = Bundle().apply {
//                    putInt(SOLAR_TERMS_RESULT, position)
//                }
//            }
//        }
//
//        override fun onRecyclerViewScrollChange(view: View?, i: Int, i1: Int) {
//            Log.d("DiscoverFragment", "onRecyclerViewScrollChange:$i-$i1")
//
//        }
//        var square = false
//        var friends = false
//        var nearby = false
//        override fun onRefresh() {
//            Handler().postDelayed({
//                try {
//                    when (ReuseUtil.page) {
//                        0 ->  {
//                            squareRefresh=true
//                            square = !square
//                            squarePage=1
//                            discoverSquareLFRecyclerView!!.stopRefresh(true)
//                            getDiscoverSquare(2,squarePage)
//                        }
//                        1 ->  {
//                            friendsRefresh=true
//                            friends = !friends
//                            friendsPage=1
//                            discoverFriendsLFRecyclerView!!.stopRefresh(true)
//                            getDiscoverSquare(3,friendsPage)
//
//                        }
//                        2 -> {
//                            nearbyRefresh=true
//                            nearby = !nearby
//                            nearbyPage=1
//                            discoverNearbyLFRecyclerView!!.stopRefresh(true)
//                            getDiscoverSquare(4,friendsPage)
//
//                        }
//                    }
//                } catch (e: RuntimeException) {
//                    e.printStackTrace()
//                }
//            }, 500)
//        }
//
//        override fun onLoadMore() {
//            Handler().postDelayed({
//                when (ReuseUtil.page) {
//                    0 -> {
//                        squareRefresh=false
//                        squarePage++
//                        discoverSquareLFRecyclerView!!.stopLoadMore()
//                        getDiscoverSquare(2,squarePage)
//                    }
//                    1 -> {
//                        friendsRefresh=false
//                        friendsPage++
//                        discoverFriendsLFRecyclerView!!.stopLoadMore()
//                        getDiscoverSquare(3,friendsPage)
//                    }
//                    2 -> {
//                        nearbyRefresh=false
//                        nearbyPage++
//                        discoverNearbyLFRecyclerView!!.stopLoadMore()
//                        getDiscoverSquare(4,nearbyPage)
//                    }
//                    else -> {
//                    }
//                }
//            }, 500)
//        }
//
//        private open class DiscoverAdapter(var context: Context, var datas:List<DiscoverData>):RecyclerView.Adapter<GridHolder>(){
//            var lastClickTime: Long = 0 //定义上一次单击的时间
//
//            class GridHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//                lateinit var discoverItemCommentText: TextView//评论数量
//                lateinit var discoverItemMore: FrameLayout//更多
//                lateinit var discoverItemText: TextView//动态内容
//                lateinit var discoverItemTime: TextView//发布动态的时间
//                lateinit var discoverItemImage: CircleImageView//发布动态用户的头像
//                lateinit var discoverItemUsername: TextView//发布动态用户的名字
//                lateinit var discoverItemGiveText: TextView//点赞数
//                lateinit var discoverItemGiveLayout: FrameLayout//当前登录用户是否点赞
//                lateinit var discoverItemGive: ImageView//当前登录用户是否点赞，未点赞
//                lateinit var discoverItemGiveJson: LottieAnimationView//当前登录用户是否点赞，已点赞
//                lateinit var discoverItemWhetherFriend: ImageView//	是否为好友；2：不是；3：是
//                lateinit var discoverItemIvNineGridLayout: NineGridlayout//发布的图片,多张
//                lateinit var discoverItemIvOneImage: CustomImageView//发布的图片，单张
//                lateinit var discoverItemCommentImage: ImageView//评论图片
//                lateinit var discoverItemAllContent: LinearLayout//动态内容区
//            }
//
//            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridHolder {
//                val view = LayoutInflater.from(parent.context).inflate(R.layout.discover_item, parent, false)
//                val gridHolder=GridHolder(view)
//                gridHolder.discoverItemCommentText=view.findViewById(R.id.discover_item_comment_text)
//                gridHolder.discoverItemMore=view.findViewById(R.id.discover_item_more)
//                gridHolder.discoverItemText=view.findViewById(R.id.discover_item_text)
//                gridHolder.discoverItemTime=view.findViewById(R.id.discover_item_time)
//                gridHolder.discoverItemImage=view.findViewById(R.id.discover_item_image)
//                gridHolder.discoverItemUsername=view.findViewById(R.id.discover_item_username)
//                gridHolder.discoverItemGiveText=view.findViewById(R.id.discover_item_give_text)
//                gridHolder.discoverItemGiveLayout=view.findViewById(R.id.discover_item_give_layout)
//                gridHolder.discoverItemGive=view.findViewById(R.id.discover_item_give)
//                gridHolder.discoverItemGiveJson=view.findViewById(R.id.discover_item_give_json)
//                gridHolder.discoverItemWhetherFriend=view.findViewById(R.id.discover_item_whether_friend)
//                gridHolder.discoverItemIvNineGridLayout=view.findViewById(R.id.discover_item_iv_nine_grid_layout)
//                gridHolder.discoverItemIvOneImage=view.findViewById(R.id.discover_item_iv_one_image)
//                gridHolder.discoverItemCommentImage=view.findViewById(R.id.discover_item_comment_image)
//                gridHolder.discoverItemAllContent=view.findViewById(R.id.discover_item_all_content)
//                return gridHolder
//            }
//
//            override fun onBindViewHolder(holder: GridHolder, position: Int) {
//                val discover = datas[position]
//                Glide.with(context).load(discover.dynamic_user_headImg).centerCrop().into(holder.discoverItemImage) //设置头像
//                holder.discoverItemUsername.text=discover.dynamic_user_name
//                holder.discoverItemGiveText.text=discover.favor_num
//                holder.discoverItemTime.text=discover.create_time
//                if(discover.content!=null){
//                    holder.discoverItemText.text= discover.content.toString()
//                }
//                holder.discoverItemCommentText.text=discover.comment_num
//                if (discover.favor_type==2) { //已点赞
//                    holder.discoverItemGive.visibility = View.GONE
//                    holder.discoverItemGiveJson.visibility = View.VISIBLE
//                } else if (discover.favor_type==3) { //未点赞
//                    holder.discoverItemGive.visibility = View.VISIBLE
//                    holder.discoverItemGiveJson.visibility = View.GONE
//                }
//                if (discover.friend_type==2) { //不是好友
//                    holder.discoverItemWhetherFriend.visibility = View.VISIBLE
//                } else if (discover.friend_type==3) {
//                    holder.discoverItemWhetherFriend.visibility = View.GONE
//                }
//                holder.discoverItemWhetherFriend.setOnClickListener {
//                    //进入个人信息页面
//                    val intent = Intent(context, PersonalActivity1::class.java)
//                    intent.putExtra("toId", discover.user_id.toString())
//                    context.startActivity(intent)
//                }
//                //需要进入第二个页面
//                holder.discoverItemAllContent.setOnClickListener {
//                    val intent = Intent(context, DiscoverItemParticularsActivity()::class.java)
//                    val gson = Gson()
//                    val codeUserGson = gson.toJson(discover)
//                    intent.putExtra("discover", codeUserGson)
//                    context.startActivity(intent)
//                }
//                //点赞
//                holder.discoverItemGiveLayout.setOnClickListener {
//                    Log.d("DiscoverFragment", "点击了点赞")
//                    val curTime = Date().time //本地单击的时间
//                    if (curTime - lastClickTime > 1000) {
//                        lastClickTime = curTime
//                        val type = if (discover.favor_type == 2) 3 else 2
//                        val map = HiRetrofit.getParameter(
//                            hashMapOf(
//                                "dynamic_id" to discover.dynamic_id.toString(),
//                                "dynamic_user_id" to discover.user_id.toString(),
//                                "favor_status" to type.toString()
//                            )
//                        )
//                        val apiService: ApiService = HiRetrofit.create(ApiService::class.java)
//                        apiService.dynamicFavor(map)
//                            .enqueue(object : Callback<Response2> {
//                                override fun onResponse(
//                                    call: Call<Response2>,
//                                    response: Response<Response2>
//                                ) {
//                                    Log.d(
//                                        "PlaceholderFragment->discoverItemGiveLayout",
//                                        response.body()?.toString() ?: "response is null"
//                                    )
//                                    val body = response.body()
//                                    if (body != null) {
//                                        if (body.status == 1) {//操作成功
//                                            if (discover.favor_type == 3) {//未点赞，点赞成功
//                                                discover.favor_type = 2
//                                                holder.discoverItemGive.visibility = View.GONE
//                                                holder.discoverItemGiveJson.visibility =
//                                                    View.VISIBLE
//                                                holder.discoverItemGiveJson.playAnimation()
//                                            } else if (discover.favor_type == 2) {//已点赞，取消点赞成功
//                                                discover.favor_type = 3
//                                                holder.discoverItemGive.visibility = View.VISIBLE
//                                                holder.discoverItemGiveJson.visibility = View.GONE
//                                            }
//                                            discover.favor_num = body.result.toString()
//                                            holder.discoverItemGiveText.text = discover.favor_num
//                                        }
//                                    }
//                                }
//
//                                override fun onFailure(call: Call<Response2>, t: Throwable) {
//                                    Log.d("setLocation->onFailure", t.message ?: "unknown reason")
//                                }
//                            })
//                    }
//                }
//                //点击了评论(显示评论控件，发布评论监听在DiscoverFragment)
//                holder.discoverItemCommentImage.setOnClickListener {
//                    (context as HomeActivity).homeShade?.alpha = 0.7f
//                    @SuppressLint("InflateParams")
//                    val commentView: View = LayoutInflater.from(context).inflate(R.layout.discover_comment, null, false)
//                    val sharePop = PopupWindow(commentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true)
//                    sharePop.setBackgroundDrawable(null)
//                    sharePop.isFocusable = true
//                    sharePop.inputMethodMode = PopupWindow.INPUT_METHOD_NEEDED
//                    sharePop.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
//                    sharePop.showAtLocation((context as HomeActivity).window.decorView, Gravity.BOTTOM, 0, 0)
//                    val commentEdit = commentView.findViewById<EditText>(R.id.comment_edit)
//                    val commentSendBtn = commentView.findViewById<Button>(R.id.comment_send_btn)
//                    commentSendBtn.setOnClickListener {
//                        //调用评论接口
//                        println("点击了发送按钮：" + commentEdit.text.toString().trim { it <= ' ' })
//                        val edit = commentEdit.text.toString().trim { it <= ' ' }
//                        if ("" == edit) {
//                            Toast.makeText(context, "评论内容不能为空！", Toast.LENGTH_SHORT).show()
//                        } else {
//                            val map = HiRetrofit.getParameter(
//                                hashMapOf(
//                                    "dynamic_id" to discover.dynamic_id.toString(),
//                                    "content" to edit
//                                )
//                            )
//                            val apiService: ApiService = HiRetrofit.create(ApiService::class.java)
//                            apiService.dynamicComment(map)
//                                .enqueue(object : Callback<wuge.social.com.model.Response> {
//                                    override fun onResponse(
//                                        call: Call<wuge.social.com.model.Response>,
//                                        response: Response<wuge.social.com.model.Response>
//                                    ) {
//                                        Log.d("PlaceholderFragment->discoverItemGiveLayout", response.body()?.toString() ?: "response is null")
//                                        val body = response.body()
//                                        if (body != null) {
//                                            if (body.status == 1) {//评论成功
////                                                discover.favor_num=body.result.toString()
////                                                holder.discoverItemGiveText.text=discover.favor_num
//                                                sharePop.dismiss()
//                                            }
//                                        }
//                                    }
//
//                                    override fun onFailure(call: Call<wuge.social.com.model.Response>, t: Throwable) {
//                                        Log.d("setLocation->onFailure", t.message ?: "unknown reason")
//                                    }
//                                })
//                        }
//                    }
//                    sharePop.setOnDismissListener {
//                        (context as HomeActivity).homeShade?.alpha = 0f
//                    }
//                }
//                //渲染图片
//                val list=discover.picture_text
//                if(list==null){
//                    holder.discoverItemIvNineGridLayout.visibility = View.GONE
//                    holder.discoverItemIvOneImage.visibility = View.GONE
//                }else if(discover.picture_text.size==1){
//                    object : Thread() {
//                        override fun run() {
//                            //这儿是耗时操作，完成之后更新UI；
//                            val i: IntArray = getImgWH(discover.picture_text[0])
//                            val image=Image()
//                            image.url=discover.picture_text[0]
//                            image.width=i[0]
//                            image.height=i[1]
//                            (context as Activity).runOnUiThread {
//                                holder.discoverItemIvNineGridLayout.visibility = View.GONE
//                                holder.discoverItemIvOneImage.visibility = View.VISIBLE
//                                handlerOneImage(holder, image, context)
//                            }
//                        }
//                    }.start()
//                }else{
//                    object : Thread() {
//                        override fun run() {
//                            //这儿是耗时操作，完成之后更新UI；
//                            val itemList: MutableList<Image> = java.util.ArrayList()
//                            for(s in list){
//                                val image=Image()
//                                image.url=s
//                                image.width=250
//                                image.height=250
//                                itemList.add(image)
//                            }
//                            (context as Activity).runOnUiThread {
//                                holder.discoverItemIvNineGridLayout.visibility = View.VISIBLE
//                                holder.discoverItemIvOneImage.visibility = View.GONE
//                                holder.discoverItemIvNineGridLayout.setImagesData(itemList)
//                            }
//                        }
//                    }.start()
//                }
//            }
//
//            override fun getItemCount(): Int {
//                return datas.size
//            }
//            private fun handlerOneImage(
//                viewHolder: GridHolder,
//                image: Image,
//                context: Context
//            ) {
//                val totalWidth: Int
//                var imageWidth: Int
//                var imageHeight: Int
//                val screenTools: ScreenTools = ScreenTools.instance(context)
//                totalWidth = screenTools.screenWidth - screenTools.dip2px(80)
//                imageWidth = screenTools.dip2px(image.width)
//                imageHeight = screenTools.dip2px(image.height)
//                if (image.width <= image.height) {
//                    if (imageHeight > totalWidth) {
//                        imageHeight = totalWidth
//                        imageWidth = imageHeight * image.width / image.height
//                    }
//                } else {
//                    if (imageWidth > totalWidth) {
//                        imageWidth = totalWidth
//                        imageHeight = imageWidth * image.height / image.width
//                    }
//                }
//                val layoutParams: ViewGroup.LayoutParams = viewHolder.discoverItemIvOneImage.layoutParams
//                layoutParams.height = imageHeight
//                layoutParams.width = imageWidth
//                viewHolder.discoverItemIvOneImage.layoutParams = layoutParams
//                viewHolder.discoverItemIvOneImage.isClickable = false
//                viewHolder.discoverItemIvOneImage.scaleType = ImageView.ScaleType.FIT_XY
//                viewHolder.discoverItemIvOneImage.setImageUrl(image.url)
//            }
//            /**
//             * 获取服务器上的图片尺寸
//             */
//            open fun getImgWH(urls: String?): IntArray {
//                val imgSize = IntArray(2)
//                imgSize[0] = 0
//                imgSize[1] = 0
//                try{
//                    val url = URL(urls)
//                    val conn = url.openConnection() as HttpURLConnection
//                    conn.doInput = true
//                    conn.connect()
//                    val isp = conn.inputStream
//                    val image = BitmapFactory.decodeStream(isp)
//                    val srcWidth = image.width // 源图宽度
//                    val srcHeight = image.height // 源图高度
//                    imgSize[0] = srcWidth
//                    imgSize[1] = srcHeight
//
//                    //释放资源
//                    image.recycle()
//                    isp.close()
//                    conn.disconnect()
//
//                }catch (e:Exception){
//                    e.printStackTrace()
//                }
//                return imgSize
//            }
//        }
//    }
//    private fun setMessageInitMagicIndicator(context: Context) {
//        val commonNavigator = CommonNavigator(context)
//        commonNavigator.isAdjustMode = true
//        commonNavigator.adapter = object : CommonNavigatorAdapter() {
//            override fun getCount(): Int {
//                return discoverDataList.size
//            }
//
//            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
//                val simplePagerTitleView: SimplePagerTitleView =
//                    ScaleTransitionPagerTitleView(context)
//                simplePagerTitleView.normalColor = UNSELECTED
//                simplePagerTitleView.selectedColor = Color.BLACK
//                simplePagerTitleView.text = discoverDataList.get(index)
//                simplePagerTitleView.textSize = 24f
//                simplePagerTitleView.setOnClickListener { discoverViewpager!!.setCurrentItem(index) }
//                return simplePagerTitleView
//            }
//
//            override fun getIndicator(context: Context): IPagerIndicator {
//                val linePagerIndicator = LinePagerIndicator(context)
//                linePagerIndicator.endInterpolator = DecelerateInterpolator(1.6f)
//                linePagerIndicator.mode = LinePagerIndicator.MODE_WRAP_CONTENT
//                linePagerIndicator.setColors(UNDERLINE)
//                return linePagerIndicator
//            }
//        }
//        magicIndicator.navigator = commonNavigator
//        ViewPagerHelper.bind(magicIndicator, discoverViewpager)
//    }
//    private class PageAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
//        override fun getItem(position: Int): Fragment = PlaceholderFragment.newInstance(position )
//        override fun getCount(): Int { return 3 }
//        override fun getPageTitle(position: Int): CharSequence? { //设置标题
//            when (position) {
//                0 -> return "广场"
//                1 -> return "好友"
//                2 -> return "附近"
//            }
//            return null
//        }
//    }
//
//    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//        Log.d("DiscoverFragment", "onPageScrolled->position=$position+,positionOffset=$positionOffset+,positionOffsetPixels=$positionOffsetPixels")
//    }
//
//    //Viewpager当前页面监听
//    override fun onPageSelected(position: Int) {
//        Log.d("DiscoverFragment", "onPageSelected=$position")
//        ReuseUtil.page=position
//    }
//
//    //Viewpager滑动监听  state ==1的时辰默示正在滑动，state==2的时辰默示滑动完毕了，state==0的时辰默示什么都没做。
//    override fun onPageScrollStateChanged(state: Int) {
//        Log.d("DiscoverFragment", "onPageScrollStateChanged=$state")
//    }
}