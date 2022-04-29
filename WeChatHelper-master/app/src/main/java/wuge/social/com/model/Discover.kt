package wuge.social.com.model

data class Discover(
    val message: String,
    val result: DiscoverResult,
    val status: Int
)

data class DiscoverResult(
    val current_page: Int,//当前页码
    val `data`: List<DiscoverData>,//动态列表信息，二维
    val last_page: Int,//last_page
    val per_page: Int,//当前页显示条数
    val total: Int//总条数
)

data class DiscoverData(
    val comment_num: String,//评论数量
    val content: String,//动态内容
    val create_time: String,//发布动态的时间
    val dynamic_id: Int,//动态列表id
    val dynamic_user_headImg: String,//发布动态用户的头像
    val dynamic_user_name: String,//发布动态用户的名字
    var favor_num: String,//点赞数
    var favor_type: Int,//当前登录用户是否点赞，2：点赞了；3：未点赞
    val friend_type: Int,//	是否为好友；2：不是；3：是
    val picture_text: List<String>,//二维，发布的图片
    val user_id: Int,//发布动态的用户id
    val video_text: String//发布的视频
)