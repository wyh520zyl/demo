package wuge.social.com.model

data class Response1(
    val message: String,
    val result: ResponseResult,
    val status: Int
)
data class Response(
    val message: String,
    val result: List<Any>,
    val status: Int
)
data class ResponseResult(
    val `data`: String
)
data class Response2(
    val message: String,
    val result: Int,
    val status: Int
)
