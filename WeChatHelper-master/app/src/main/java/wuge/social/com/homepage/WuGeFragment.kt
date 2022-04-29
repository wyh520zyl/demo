package wuge.social.com.homepage

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import wuge.social.com.R
import wuge.social.com.adapter.EnclosureAdapter
import wuge.social.com.model.CoverPhotoResult
import wuge.social.com.model.User
import wuge.social.com.util.RUtil
import wuge.social.com.util.ReuseUtil

class WuGeFragment : Fragment() {
    lateinit var v:View

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        v = inflater.inflate(R.layout.home_tab_wuge, container, false)
        v.findViewById<TextView>(R.id.wuge_nickname).text= User.nickname
        v.findViewById<TextView>(R.id.wuge_grade).text= User.levels+ "级"
        v.findViewById<TextView>(R.id.wuge_money_num).text= User.gold
        if(User.sex=="1"){
            //失败
            v.findViewById<ImageView>(R.id.wu_ge_sex).setBackgroundResource(R.mipmap.wuge_friend_play_item_boy)
        }else if(User.sex=="2"){
            v.findViewById<ImageView>(R.id.wu_ge_sex).setBackgroundResource(R.mipmap.wuge_friend_play_item_girl)
        }else{
            v.findViewById<ImageView>(R.id.wu_ge_sex).visibility=View.GONE
        }
        ReuseUtil.mLog(User.nickname.toString())
        context?.let { Glide.with(it)
            .load(User.headImg)
            .thumbnail(Glide.with(context!!).load(R.mipmap.message_friend_head))
            .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
            .into(v.findViewById(R.id.wuge_headimg))
        }//设置物格页面的头像
        v.findViewById<ImageView>(R.id.wu_ge_add_gold).setOnClickListener{
            RUtil.log("点击了金币的添加按钮")
        }
        return v
    }

    fun setEnclosureList(context: Context,games: List<CoverPhotoResult>){
        val list = arrayListOf<CoverPhotoResult>()
        var i=0
        for(game in games) {
            if (game.gameType == 1) {
                list.add(i,game)
                i++
            } else if (game.gameType == 2) {
                context.let {
                    Glide.with(it)
                        .load(game.cover_url)
                        .thumbnail(Glide.with(context).load(R.mipmap.message_friend_head))
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
                        .into(v.findViewById(R.id.map_image))
                }
            }
        }
        val gridView:GridView=v.findViewById (R.id.wuge_game_gridview)
        gridView.adapter= EnclosureAdapter(context,list)
        setListViewHeightBasedOnChildren(gridView)//动态设置GridView高度
        setOnItemClick(gridView,list)//设置点击事件
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
    private fun setOnItemClick(gridView : GridView,list : List<CoverPhotoResult>){
//        gridView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
//            if(list[position].cover_url.startsWith("http://webtest.wgzy01")){
//                val intent = Intent(context, GameWebViewActivity::class.java)
//                val url = list[position].cover_url + "?userid="+
//                        User.game_id +
//                        "&cityname=" +
//                        User.city +
//                        User.district  +
//                        "&headImg=" +
//                        User.headImg +
//                        "&nickname=" +
//                        User.nickname +
//                        "&ios_and_type=android"
//                intent.putExtra("game_url",url)
//                startActivity(intent)
//            }else{
//                if (list[position].cover_url.startsWith("http://gobang.wgzy69.com")) { //两极困杀
//                    Log.d("WuGeFragment->setOnItemClick", "点击了两极困杀")
//
//                } else if (list[position].cover_url.startsWith("http://answer.wgzy69.com")) { //书山血海
//                    Log.d("WuGeFragment->setOnItemClick", "点击了书山血海")
//
//                }
//            }
//        }
    }
}