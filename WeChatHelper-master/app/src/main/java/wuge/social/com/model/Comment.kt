package wuge.social.com.model

import java.util.*

//动态评论详情
data class Comment(
    val message: String,
    val result: ArrayList<CommentResult>,
    val status: Int
)

data class CommentResult(
    val commont_username: String,
    val commont_userstatus: Int,
    val content: String,
    val creat_time: String,
    val dynamic_id: Int,
    val headImg: String,
    val id: Int,
    val user_id: Int
)