package wuge.social.com.realize

import android.content.Context
import android.content.Intent
import android.util.Log
import com.tencent.imsdk.v2.V2TIMConversation
import wuge.social.com.`interface`.NearbyFriendOnClick
import wuge.social.com.activity.ImActivity
import wuge.social.com.model.NearbyResult

class NearbyFriend :NearbyFriendOnClick {
    override fun onClick(context: Context, nearbyResult: NearbyResult) {
        Log.d("wang", "nearbyResult:$nearbyResult")
        val intent = Intent(context, ImActivity::class.java)
        intent.putExtra("type", V2TIMConversation.V2TIM_C2C)
        intent.putExtra("id", nearbyResult.user_id.toString())
        intent.putExtra("name", nearbyResult.nickname)
        context.startActivity(intent)
    }

}