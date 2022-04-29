package wuge.social.com.util

import wuge.social.com.model.User
import java.util.*

//加密需要的时间戳
object Timestamp {
    const val APIKEY = "wuge123456shejiao987654"
    fun getSecondTimestamp(date: Date?): Int {
        if (null == date) {
            return 0
        }
        val timestamp = date.time.toString()
        val length = timestamp.length
        return if (length > 3) {
            timestamp.substring(0, length - 3).toInt()
        } else {
            0
        }
    }
    fun getTimeStamp():String{
        return if(User.user_id==""){
            ""
        }else{
            APIKEY + getSecondTimestamp( Date()) + User.user_id
        }
    }
}