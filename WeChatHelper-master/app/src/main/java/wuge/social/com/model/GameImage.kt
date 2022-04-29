package wuge.social.com.model
data class Slideshow(
    val message: String,
    val result: List<SlideshowResult>,
    val status: Int

) {
    override fun toString(): String {
        return "Slideshow(message='$message', result=$result, status=$status)"
    }
}

data class SlideshowResult(
    val second: Int,
    val slideshow_url: String

) {
    override fun toString(): String {
        return "SlideshowResult(second=$second, slideshow_url='$slideshow_url')"
    }
}
//
//data class GameImage(
//    val code: Int,
//    val game: List<Game>,
//    val msg: String,
//    val slideshow: Slideshow
//
//) {
//    override fun toString(): String {
//        return "GameImage(code=$code, game=$game, msg='$msg', slideshow=$slideshow)"
//    }
//}
//
data class Game(
    val cover_url: String,
    val gameName: String,
    val id: Int,
    val jump_address: String,
    val type: String

) {
    override fun toString(): String {
        return "Game(cover_url='$cover_url', gameName='$gameName', id=$id, jump_address='$jump_address', type='$type')"
    }
}
//
data class SlideshowImage(
    val slideshow_url: List<String>,
    val time: Int

) {
    override fun toString(): String {
        return "Slideshow(slideshow_url=$slideshow_url, time=$time)"
    }
}
