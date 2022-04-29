package wuge.social.com.homepage

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.*
import wuge.social.com.adapter.ListAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.util.Util
import com.tencent.imsdk.v2.V2TIMConversation
import com.tencent.qcloud.tim.uikit.base.ITitleBarLayout
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout
import wuge.social.com.adapter.ContactAdapter
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationLayout
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import wuge.social.com.R
import wuge.social.com.`interface`.NearbyFriendOnClick
import wuge.social.com.activity.ImActivity
import wuge.social.com.http.ApiService
import wuge.social.com.http.HiRetrofit
import wuge.social.com.model.Friends
import wuge.social.com.model.Nearby
import wuge.social.com.model.NearbyResult
import wuge.social.com.realize.NearbyFriend
import wuge.social.com.util.DividerItemDecorationUtil
import wuge.social.com.util.ReuseUtil
import wuge.social.com.util.ScaleTransitionPagerTitleView
import wuge.social.com.view.LetterView
import wuge.social.com.view.MessageChatListView

class MessageFragment :Fragment(){
    lateinit var v:View
    private lateinit var magicIndicator:MagicIndicator
    private val messageDataList = listOf("聊天", "附近")
    private val UNSELECTED = -0x555556 //未选择的字体颜色
    private val UNDERLINE = -0xcd9409 //下划线的字体颜色
    private var messageViewpager: ViewPager? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        v=inflater.inflate(R.layout.home_tab_message, container, false)
        messageViewpager = v.findViewById(R.id.message_viewpager)
        magicIndicator = v.findViewById<View>(R.id.message_indicator) as MagicIndicator
        return v
    }
    fun setViewPager(fragmentManager: FragmentManager) {
        messageViewpager?.adapter = messageViewpager?.let { PageAdapter(fragmentManager) }
        setMessageInitMagicIndicator()
    }
    private fun setMessageInitMagicIndicator() {
        val commonNavigator = CommonNavigator(context)
        commonNavigator.isAdjustMode = true
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return messageDataList.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val simplePagerTitleView: SimplePagerTitleView =
                    ScaleTransitionPagerTitleView(context)
                simplePagerTitleView.normalColor = UNSELECTED
                simplePagerTitleView.selectedColor = Color.BLACK
                simplePagerTitleView.text = messageDataList.get(index)
                simplePagerTitleView.textSize = 24f
                simplePagerTitleView.setOnClickListener { messageViewpager!!.setCurrentItem(index) }
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
        magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(magicIndicator, messageViewpager)
    }
    private class PageAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment = PlaceholderFragment.newInstance(position )
        override fun getCount(): Int { return 2 }
        override fun getPageTitle(position: Int): CharSequence? { //设置标题
            when (position) {
                0 -> return "聊天"
                1 -> return "附近"
            }
            return null
        }
    }
    class PlaceholderFragment : Fragment(), View.OnClickListener{
        private var mParam = 0 //用来表示当前需要展示的是哪一页
        private lateinit var taskGridView: View
        private var conversationLayout: ConversationLayout? = null
        private var titleBarLayout: TitleBarLayout? = null
        //private var messageChatNearbyListview: MessageChatListView? = null
        //private  var messageChatDefault:ImageView? = null
       // private var messageChatNearby: RelativeLayout? = null
        private var messageChatNullInformation: ImageView? = null
        //private var informationChatAccCon: ImageView? = null
        private var recommendGridView:GridView?=null
        private var recommendDefault:ImageView?=null
        private var friendsDefaultImage:ImageView?=null
        private var friendsEditFront:ImageView?=null
        private var itemScreenImg:FrameLayout?=null
        private var itemScreenLayout:LinearLayout?=null
        private var itemSeeSchoolgirl:TextView?=null
        private var itemSeeSchoolboy:TextView?=null
        private var itemSeeAll:TextView?=null
        private var friendsList: RecyclerView?=null
        private var friendsSearchLayout: RelativeLayout?=null
        private var friendsLetter: LetterView?=null
        private var friendsAddLayout: LinearLayout?=null
        private var friendsEdit: EditText?=null
        private var layoutManager: LinearLayoutManager? = null
        private var adapter: ContactAdapter? = null
        private var mNearby: Nearby?=null
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            if (arguments != null) {
                mParam = arguments!!.getInt(SOLAR_TERMS_RESULT)
            }
        }
        @SuppressLint("SetTextI18n")
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
            when (mParam) {
                0 -> {
                    taskGridView= inflater.inflate(R.layout.message_chat, container, false)
                    //chatBinding()
                }
                1 -> {
                    taskGridView= inflater.inflate(R.layout.message_recommend, container, false)
                    messageBinding()
                }
//                2 -> {
//                    taskGridView= inflater.inflate(R.layout.message_friends, container, false)
//                    friendsBinding()
//                }
            }
            return taskGridView
        }

        //聊天页面的控件
        private fun chatBinding() {
          //  titleBarLayout = taskGridView.findViewById(R.id.w_title_bar_layout)
//            conversationLayout = taskGridView.findViewById(R.id.conversation_layout)
//            messageChatNearbyListview  = taskGridView.findViewById<View>(R.id.message_chat_nearby_listview) as MessageChatListView
//            messageChatDefault = taskGridView.findViewById(R.id.message_chat_default)
//            messageChatNearby = taskGridView.findViewById(R.id.message_chat_nearby)
       //     messageChatNullInformation = taskGridView.findViewById(R.id.message_chat_null_information)
            //informationChatAccCon = taskGridView.findViewById<View>(R.id.information_chat_acc_con) as ImageView
            if(mNearby==null){
                getNearby("all",0)
            }
            setIm()
//            informationChatAccCon!!.setOnClickListener {
//                Log.d("MessageFragment->chatBinding","{$messageChatNearby}}")
//
//                if (messageChatNearby!!.visibility == View.GONE) {
//                    informationChatAccCon!!.animate().rotation(0f)
//                    messageChatNearby!!.visibility = View.VISIBLE
//                } else {
//                    informationChatAccCon!!.animate().rotation(180f)
//                    messageChatNearby!!.visibility = View.GONE
//                }
//            }
        }

        //附近页面的控件
        private fun messageBinding(){
            recommendGridView = taskGridView.findViewById(R.id.recommend_gridView)
            recommendDefault = taskGridView.findViewById(R.id.recommend_default)
            itemScreenImg = taskGridView.findViewById(R.id.item_screen_img)
            itemScreenLayout = taskGridView.findViewById(R.id.message_recommend_screen_layout)
            itemSeeSchoolgirl = taskGridView.findViewById(R.id.message_recommend_see_schoolgirl)
            itemSeeSchoolboy = taskGridView.findViewById(R.id.message_recommend_see_schoolboy)
            itemSeeAll = taskGridView.findViewById(R.id.message_recommend_see_all)
            itemScreenImg!!.setOnClickListener(this)
            itemSeeSchoolgirl!!.setOnClickListener(this)
            itemSeeSchoolboy!!.setOnClickListener(this)
            itemSeeAll!!.setOnClickListener(this)

            if(mNearby==null){
                getNearby("all",1)
            }
//            recommendGridView!!.setOnItemClickListener { parent, view, position, id ->
//                Log.e("物格社交","点击了推荐列表中的人："+ nearbys?.result?.get(position));
//
//            }
        }

        //好友页面的控件
        private fun friendsBinding(){
            friendsDefaultImage = taskGridView.findViewById(R.id.friends_default_image)
            friendsList = taskGridView.findViewById(R.id.friends_list)
            friendsLetter = taskGridView.findViewById(R.id.friends_letter)
            friendsAddLayout = taskGridView.findViewById(R.id.friends_add_layout)
            friendsEditFront = taskGridView.findViewById(R.id.friends_edit_front)
            friendsSearchLayout = taskGridView.findViewById(R.id.friends_search_layout)
            friendsEdit = taskGridView.findViewById(R.id.friends_edit)
            getFriends()
        }

        //获取好友列表并渲染数据
        private fun getFriends() {
            friendsDefaultImage!!.visibility=View.GONE
            friendsList!!.visibility=View.VISIBLE
            val map=HiRetrofit.getParameter(hashMapOf("note_name" to ""))
            val apiService : ApiService = HiRetrofit.create(ApiService::class.java)
            apiService.getMyFriend(map)
                .enqueue(object : Callback<Friends> {
                    @SuppressLint("UseCompatLoadingForColorStateLists")
                    override fun onResponse(call: Call<Friends>, response: Response<Friends>){
                        val res : Friends=response.body()!!
                        Log.e("wang", "好友列表：$res")
                        if(res.status==1){
                            layoutManager = LinearLayoutManager(context)
                            val contactAdapter=
                                ContactAdapter(context!!, res.result)
                            contactAdapter.handleContact()
                            adapter = contactAdapter
                            friendsList!!.layoutManager = layoutManager
                            val dividerItemDecorationUtil=  DividerItemDecorationUtil(context!!, LinearLayoutManager.VERTICAL)
                            friendsList!!.addItemDecoration(dividerItemDecorationUtil)
                            friendsList!!.adapter = adapter
                            friendsLetter!!.setCharacterListener(object :
                                LetterView.CharacterClickListener {
                                override fun clickCharacter(character: String?) {
                                    character?.let {
                                        adapter!!.getScrollPosition(
                                            it
                                        )
                                    }?.let {
                                        layoutManager!!.scrollToPositionWithOffset(
                                            it, 0
                                        )
                                    }
                                }

                                override fun clickArrow() {
                                    layoutManager!!.scrollToPositionWithOffset(0, 0)
                                }
                            })
                            friendsEdit!!.onFocusChangeListener =
                                View.OnFocusChangeListener { v, hasFocus ->
                                    if (hasFocus) {
                                        // 此处为得到焦点时的处理内容
                                        friendsEditFront!!.visibility = View.VISIBLE
                                        friendsSearchLayout!!.visibility = View.GONE
                                        friendsEdit!!.hint = "搜索"

                                    } else {
                                        // 此处为失去焦点时的处理内容
                                        friendsEditFront!!.visibility = View.GONE
                                        friendsSearchLayout!!.visibility = View.VISIBLE
                                        friendsEdit!!.hint = ""
                                    }
                                }
                        }
                    }
                    override fun onFailure(call: Call<Friends>, t: Throwable) {
                        Log.d("getFriends_onFailure",t.message?:"unknown reason")
                    }
                })
            friendsAddLayout!!.setOnClickListener(View.OnClickListener {
//                val intent = Intent(context, AddFriendActivity::class.java)
//                startActivity(intent)
            })
        }


        companion object {
            private const val SOLAR_TERMS_RESULT = "SOLAR_TERMS_RESULT"
            fun newInstance(position: Int) = PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(SOLAR_TERMS_RESULT, position)
                }
            }
        }
        private fun setIm(){
            ReuseUtil.mLog("titleBarLayout:"+titleBarLayout)
            titleBarLayout!!.setTitle(context!!.resources.getString(R.string.app_name), ITitleBarLayout.POSITION.MIDDLE);
            titleBarLayout!!.getLeftGroup().setVisibility(View.GONE);
            titleBarLayout!!.getRightGroup().setVisibility(View.VISIBLE);
            titleBarLayout!!.setRightIcon(R.drawable.share);
            titleBarLayout!!.visibility=View.GONE
            conversationLayout!!.initDefault()
            conversationLayout!!.titleBar.visibility = View.GONE
            conversationLayout!!.conversationList.setOnItemClickListener { _, _, messageInfo ->
                //进入聊天界面
                val intent = Intent(context, ImActivity::class.java)
                intent.putExtra("type", messageInfo.type)
                intent.putExtra("id", messageInfo.id)
                intent.putExtra("name", messageInfo.title)
                context!!.startActivity(intent)
            }
        }
        private fun getNearby(sex:String,page:Int){
            val map=HiRetrofit.getParameter(hashMapOf("sex" to sex))
            val apiService : ApiService = HiRetrofit.create(ApiService::class.java)
            apiService.getNearbyUser(map)
                .enqueue(object : Callback<Nearby> {
                    override fun onResponse(call: Call<Nearby>, response: Response<Nearby>) {
                        mNearby=response.body()
                        when(page){
//                            0->{
//                                if(mNearby!!.result.isNotEmpty()&&context!=null){
//                                   // messageChatDefault!!.visibility=View.GONE
//                                    Log.d("wang","mNearby:"+mNearby)
//                                    Log.d("wang","result:"+mNearby?.result)
//                                    Log.d("wang","context:"+context)
//                                    Log.d("wang","page:"+page)
//                                    val listAdapter= ListAdapter(mNearby!!.result, context!!,page)
//                                    messageChatNearbyListview!!.adapter= context?.let { listAdapter }
//                                    messageChatNearbyListview!!.setOnItemClickListener { parent, view, position, id ->
//                                        NearbyFriend().onClick(context!!, mNearby!!.result[position])
//                                    }
//                                }else{
//                                    messageChatDefault!!.visibility=View.VISIBLE
//                                }
//                            }
                            1->{
                                if(mNearby!!.result.isNotEmpty()&&context!=null){
                                    recommendDefault!!.visibility=View.GONE
                                    recommendGridView!!.adapter= context?.let { ListAdapter(mNearby!!.result, it,page) }
                                    recommendGridView!!.setOnItemClickListener { parent, view, position, id ->
                                        NearbyFriend().onClick(context!!, mNearby!!.result[position])
                                    }
                                }else{
                                    recommendDefault!!.visibility=View.VISIBLE
                                }
                            }
                        }


                    }
                    override fun onFailure(call: Call<Nearby>, t: Throwable) {
                    }
                })
        }

        override fun onClick(v: View?) {
            when(v!!.id){
                R.id.item_screen_img->{
                    if (itemScreenLayout!!.visibility == View.VISIBLE) {
                        itemScreenLayout!!.visibility = View.GONE
                    } else {
                        itemScreenLayout!!.visibility = View.VISIBLE
                    }
                }
                R.id.message_recommend_see_schoolgirl->{
                    recommendGridView!!.adapter=null

                    if(mNearby==null){
                        getNearby("all",1)
                    }
                    itemScreenLayout!!.visibility = View.GONE
                }
                R.id.message_recommend_see_schoolboy->{
                    recommendGridView!!.adapter=null
                    if(mNearby==null){
                        getNearby("all",1)
                    }
                    itemScreenLayout!!.visibility = View.GONE
                }
                R.id.message_recommend_see_all->{
                    recommendGridView!!.adapter=null
                    if(mNearby==null){
                        getNearby("all",1)
                    }
                    itemScreenLayout!!.visibility = View.GONE
                }
            }
        }
    }
}