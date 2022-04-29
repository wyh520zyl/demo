package wuge.social.com.model


data class CoverPhoto(
    val message: String,
    val result: List<CoverPhotoResult>,
    val status: Int

) {
    override fun toString(): String {
        return "CoverPhoto(message='$message', result=$result, status=$status)"
    }
}

data class CoverPhotoResult(
    val cover_url: String,
    val gameType: Int,
    val jump_address: String

) {
    override fun toString(): String {
        return "CoverPhotoResult(cover_url='$cover_url', gameType=$gameType, jump_address='$jump_address')"
    }
}