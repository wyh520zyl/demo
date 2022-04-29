package wuge.social.com.model

//好友列表
data class Friends(
    val message: String,
    val result: List<FriendsResult>,
    val status: Int//返回状态；1：成功;0:失败

) {
    override fun toString(): String {
        return "Friends(message='$message', result=$result, status=$status)"
    }
}

data class FriendsResult(
    val friend_id: Int,//好友id
    val headImg: String,//好友头像
    val note_name: String,//好友昵称
    val sex: Int//好像性别

) {
    override fun toString(): String {
        return "FriendsResult(friend_id=$friend_id, headImg='$headImg', note_name='$note_name', sex=$sex)"
    }
}