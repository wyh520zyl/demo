package wuge.social.com.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import wuge.social.com.R
import wuge.social.com.model.Contact
import wuge.social.com.model.FriendsResult
import wuge.social.com.util.ContactComparatorUtil
import wuge.social.com.util.ContactUtil
import java.util.*

class ContactAdapter( context: Context,contactNames :List<FriendsResult> ) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var mLayoutInflater: LayoutInflater? = LayoutInflater.from(context)
    private var mContext: Context? = context
    private var list=contactNames
    private var resultList: MutableList<Contact>? = null// 最终结果（包含分组的字母）
    private var characterList: ArrayList<String>? = null
    @SuppressLint("StaticFieldLeak")
    var contactAdapter: ContactAdapter? = null

    enum class TELETYPE {
        ITEM_TYPE_CHARACTER, ITEM_TYPE_CONTACT
    }


     fun handleContact() {
        // 联系人名称List（转换成拼音）
        val mContactList: MutableList<String> = ArrayList()
        val map: MutableMap<String, String> = HashMap()
        val ids: MutableMap<String, String> = HashMap()
        val urls: MutableMap<String, String> = HashMap()
        val sexs: MutableMap<String, String> = HashMap()
        for (i in list.indices) {
            val pinyin: String = ContactUtil.getPingYin(list[i].note_name)
            map[pinyin] = list[i].note_name
            urls[pinyin] = list[i].headImg
            ids[pinyin] = list[i].friend_id.toString()
            sexs[pinyin] = list[i].sex.toString()
            mContactList.add(pinyin)
        }
        Collections.sort(mContactList, ContactComparatorUtil())
        resultList = ArrayList<Contact>()
        characterList = ArrayList()
        for (i in mContactList.indices) {
            val name = mContactList[i]
            val character = (name[0].toString() + "").toUpperCase(Locale.ENGLISH)
            if (!characterList!!.contains(character)) {
                if (character.hashCode() >= "A".hashCode() && character.hashCode() <= "Z".hashCode()) { // 是字母
                    characterList!!.add(character)
                    val contact=Contact()
                    contact.mName=character
                    contact.mType=TELETYPE.ITEM_TYPE_CHARACTER.ordinal
                    resultList!!.add(contact)
                } else {
                    if (!characterList!!.contains("#")) {
                        characterList!!.add("#")
                        val contact=Contact()
                        contact.mName="#"
                        contact.mType= TELETYPE.ITEM_TYPE_CHARACTER.ordinal
                        resultList!!.add(contact)
                    }
                }
            }
            val contact = Contact()
            contact.mName=map[name]
            contact.mType=TELETYPE.ITEM_TYPE_CONTACT.ordinal
            contact.id=ids[name]
            contact.url=urls[name]
            contact.sex=sexs[name]
            resultList!!.add(contact)
        }
    }

 override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TELETYPE.ITEM_TYPE_CHARACTER.ordinal) {
            CharacterHolder(mLayoutInflater!!.inflate(R.layout.item_character, parent, false))
        } else {
            ContactHolder(mLayoutInflater!!.inflate(R.layout.item_contact, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CharacterHolder) {
            holder.mTextView.text = resultList!![position].mName
        } else if (holder is ContactHolder) {
            holder.mTextView.text = resultList!![position].mName
            Glide.with(mContext!!).load(resultList!![position].url).into(holder.contactImage)
            val sex: String = resultList!![position].sex.toString()
            println("当前好友的性别：$sex")
            if ("1" == sex) {
                val icon = BitmapFactory.decodeResource(
                    mContext!!.resources,
                    R.mipmap.wuge_friend_play_item_boy
                )
                holder.contactSex.setImageBitmap(icon)
            } else if ("2" == sex) {
                val icon = BitmapFactory.decodeResource(
                    mContext!!.resources,
                    R.mipmap.wuge_friend_play_item_girl
                )
                holder.contactSex.setImageBitmap(icon)
            }
            holder.itemView.setOnClickListener {
                val contact: Contact = resultList!![position]
//                val intent = Intent(mContext!!, PersonalActivity1::class.java)
//                intent.putExtra("toId", contact.id)
//                mContext!!.startActivity(intent)
                // }
                //                    HomeMain.homeMain.discovernum=2;
                //进入聊天界面
                //                    Intent intent = new Intent(v.getContext(), ImActivity.class);
                //                    Contact contact = resultList.get(position);
                //                    //contact.getId();
                //                    intent.putExtra("type", V2TIMConversation.V2TIM_C2C);//代表私聊
                //                    intent.putExtra("id",contact.id);
                //                    intent.putExtra("name",contact.mName);
                //                    v.getContext().startActivity(intent);
            }
        }
        println("当前好友的性别1：")
    }

    override fun getItemViewType(position: Int): Int {
        return resultList!![position].mType
    }

    override fun getItemCount(): Int {
        return if (resultList == null) 0 else resultList!!.size
    }

    class CharacterHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        var mTextView: TextView

        init {
            mTextView = view.findViewById<View>(R.id.character) as TextView
        }
    }

    class ContactHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        var mTextView: TextView = view.findViewById<View>(R.id.contact_name) as TextView
        var contactImage: ImageView = view.findViewById(R.id.contact_image)
        var contactSex: ImageView = view.findViewById(R.id.contact_sex)

    }

    fun getScrollPosition(character: String): Int {
        if (characterList!!.contains(character)) {
            for (i in resultList!!.indices) {
                if (resultList!![i].mName.equals(character)) {
                    return i
                }
            }
        }
        return -1 // -1不会滑动
    }
}
