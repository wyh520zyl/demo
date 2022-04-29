package wuge.social.com.model

data class ImSign(
    val message: String,
    val result: ImSignResult,
    val status: Int

) {
    override fun toString(): String {
        return "ImSign(message='$message', result=$result, status=$status)"
    }
}

data class ImSignResult(
    val UserSig: String,
    val creat_time: String,
    val term_of_validity: String

) {
    override fun toString(): String {
        return "ImSignResult(UserSig='$UserSig', creat_time='$creat_time', term_of_validity='$term_of_validity')"
    }
}