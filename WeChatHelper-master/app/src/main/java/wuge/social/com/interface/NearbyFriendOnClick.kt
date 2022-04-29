package wuge.social.com.`interface`

import android.content.Context
import wuge.social.com.model.NearbyResult

interface NearbyFriendOnClick {
    fun onClick(context: Context,nearbyResult: NearbyResult)
}