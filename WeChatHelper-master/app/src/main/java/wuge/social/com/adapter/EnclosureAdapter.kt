package wuge.social.com.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import wuge.social.com.R
import wuge.social.com.model.CoverPhotoResult


class EnclosureAdapter(var context: Context, private var lists:List<CoverPhotoResult>):BaseAdapter(){
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
            view = LayoutInflater.from(context).inflate(R.layout.wuge_game_item,null)
            Log.d("EnclosureAdapter", lists[position].toString())
            imageView=view.findViewById(R.id.enclosure_image)
            view.tag=imageView
        }else{
            view =convertView
            imageView= view.tag as ImageView?
        }
        imageView?.let {
            Glide.with(context)
                .load(lists[position].cover_url)
                .thumbnail(Glide.with(context).load(R.mipmap.map_default))
                .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
                .into(imageView!!)
        }
        return view!!
    }
}