package wuge.social.com.model

data class Nearby(
    val message: String,
    val result: List<NearbyResult>,
    val status: Int

) {
    override fun toString(): String {
        return "Nearby(message='$message', result=$result, status=$status)"
    }
}


data class NearbyResult(
    val distance: String,
    val headImg: String,
    val nickname: String,
    val sex: Int,
    val user_id: Int

) {
    override fun toString(): String {
        return "NearbyResult(distance='$distance', headImg='$headImg', nickname='$nickname', sex=$sex, user_id=$user_id)"
    }
}