package wuge.social.com.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import wuge.social.com.R
import wuge.social.com.model.NearbyResult

class ListAdapter(private var lists:List<NearbyResult>, private var context: Context, private val page:Int):
    BaseAdapter(){
    @SuppressLint("InflateParams")
    override fun getView(p0:Int, convertView: View?, p2: ViewGroup): View {
        var view : View? = convertView
        val holder : ViewHolder
        if(view == null){
            holder=ViewHolder()
            when(page){
                0->{
                    view=
                        LayoutInflater.from(context).inflate(R.layout.message_chat_nearby_item,null)
                    holder.itemNearbyLayout = view.findViewById(R.id.item_nearby_layout)
                }
                1->{
                    view= LayoutInflater.from(context).inflate(R.layout.information_recommend_item,null)
                    holder.itemGender = view.findViewById(R.id.item_gender)
                }
            }
            holder.itemHeading = view!!.findViewById(R.id.item_heading)
            holder.itemNickname = view.findViewById(R.id.item_nickname)
            holder.itemDistance = view.findViewById(R.id.item_distance)
            view.tag = holder
        }else{
            holder = view.tag as ViewHolder
        }
        if (p0 < lists.size) {
            val nearbyResult: NearbyResult =lists[p0]
            holder.itemHeading?.let {
                Glide.with(context)
                    .load(nearbyResult.headImg)
                    .thumbnail(Glide.with(context).load(R.mipmap.carousel_default))
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(3)))
                    .into(it)
            }
            holder.itemNickname!!.text= nearbyResult.nickname
            holder.itemDistance!!.text = nearbyResult.distance
            if(page==1){
                when(page){
                    0->{
                        holder.itemGender!!.visibility= View.INVISIBLE
                    }
                    1->{
                        holder.itemGender!!.visibility= View.VISIBLE
                        holder.itemGender!!.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.wuge_friend_play_item_boy))
                    }
                    2->{
                        holder.itemGender!!.visibility= View.VISIBLE
                        holder.itemGender!!.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.wuge_friend_play_item_girl))
                    }
                }
            }
//            view.setOnClickListener(View.OnClickListener {
//                Log.e("物格社交", "点击了推荐列表中的人：$nearbyResult")
//                NearbyFriend().onClick(context,nearbyResult)
//            })
        }
        return view
    }

    override fun getCount(): Int {
        return lists.size
    }

    override fun getItem(position: Int): Any {
        return lists[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    private class ViewHolder {
        var itemHeading: ImageView? = null
        var itemGender: ImageView? = null
        var itemNickname: TextView? = null
        var itemDistance: TextView? = null
        var itemNearbyLayout: LinearLayout? = null
    }

}