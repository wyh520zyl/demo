package wuge.social.com.model
data class WxLogin(
    val message: String,
    val result: Result,
    val status: Int

) {
    override fun toString(): String {
        return "WxLogin(message='$message', result=$result, status=$status)"
    }
}

data class Result(
    val token: String,
    val user_id: String

) {
    override fun toString(): String {
        return "Result(token='$token', user_id='$user_id')"
    }
}