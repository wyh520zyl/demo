package wuge.social.com.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import wuge.social.com.R

class GridViewAdapter : BaseAdapter() {

    var mContext:Context? = null
    var mDate:String? = null
    var list: List<String>? = null

    /**
     * 获取列表数据
     * @param list
     */
    fun setLists(list: List<String>,context: Context,date:String) {
        this.list = list
        mContext=context
        mDate=date
        //this.notifyDataSetChanged()
        Log.e(" 3333 ", this.list!!.size.toString() + "")
    }

    override fun getCount(): Int {
        Log.e("  ", list!!.size.toString() + "")
        return if (list!!.size == 9) {
            9
        } else {
            list!!.size + 1
        }
    }

    override fun getItem(position: Int): Any? {
        return if (list != null
            && list!!.size == 9
        ) {
            list!![position]
        } else if (list == null || position - 1 < 0 || position > list!!.size) {
            null
        } else {
            list!![position - 1]
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView = convertView
        val holder: ViewHolder
        if (convertView == null) {
            holder = ViewHolder()
            convertView = LayoutInflater.from(mContext).inflate(R.layout.discover_item_image, null)
            holder.discoverItemImage = convertView.findViewById<View>(R.id.discover_item_image) as ImageView
            holder.discoverItemDate = convertView.findViewById<View>(R.id.discover_item_date) as TextView
            holder.discoverItemImage?.scaleType = ImageView.ScaleType.CENTER_CROP
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        Log.e(" listsize ", this.list!!.size.toString() + "")
        Log.e(" listsize ", "$position")

        if(list?.size==0){
            holder.discoverItemImage!!.setImageResource(R.drawable.add_photo)
            holder.discoverItemImage!!.setBackgroundResource(R.color.gray1)
            holder.discoverItemDate?.visibility=View.INVISIBLE
        }else{
            if(list?.size==position){
                holder.discoverItemImage!!.setImageResource(R.drawable.add_photo)
                holder.discoverItemImage!!.setBackgroundResource(R.color.gray1)
                holder.discoverItemDate?.visibility=View.INVISIBLE
            }else{
                Glide.with(mContext!!).load(list!![position]).into(holder.discoverItemImage!!)
                holder.discoverItemDate?.text=mDate
                holder.discoverItemDate?.visibility=View.VISIBLE
                holder.discoverItemImage!!.setBackgroundResource(R.color.gray1)
            }
        }
        return convertView
    }

    /**
     * 判断当前下标是否是最大值
     * @param position  当前下标
     */
    private fun isShowAddItem(position: Int): Boolean {
        val size = list?.size ?: 0
        return position == size
    }

    internal class ViewHolder {
        var discoverItemImage: ImageView? = null
        var discoverItemDate: TextView? = null

    }
}