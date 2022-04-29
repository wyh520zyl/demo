package wuge.social.com.http


/**
 *普通的实体类需要给成员变量赋值（类似于java的实体类）
 */
class Account {
    val uid:String = "123"
    val username:String = "十世亡剑"
    val password:String = "password"
    val telNumber:String = "13000000000"

    override fun toString(): String {
        return "Account(uid='$uid', username='$username', password='$password', telNumber='$telNumber')"
    }
}

/**
 * 使用data修饰就可以不用赋值
 */
data class Account2(
    val uid:String ,
    val username:String ,
    val password:String ,
    val telNumber:String
)
/**
 * 右键鼠标选择Generate->kotlin data classes from GSON
 * */
data class CodeUser(
    val message: String,
    val result: CodeUserResult,
    val status: Int
)

data class CodeUserResult(
    val address: String,
    val age: Int,
    val birthday: String,
    val charm_value: String,
    val constellation: String,
    val exp: String,
    var friend_type: Int,
    val gold: String,
    val headImg: String,
    val id: String,
    val imgs: List<String>,
    val levels: String,
    val nearby_status: String,
    val nickname: String,
    var note_name: String,
    val sex: String,
    val signature: String,
    val social_value: String,
    var wall_image: String

) {
    override fun toString(): String {
        return "CodeUserResult(address='$address', age=$age, birthday='$birthday', charm_value='$charm_value', constellation='$constellation', exp='$exp', friend_type=$friend_type, gold='$gold', headImg='$headImg', id='$id', imgs=$imgs, levels='$levels', nearby_status='$nearby_status', nickname='$nickname', note_name='$note_name', sex='$sex', signature='$signature', social_value='$social_value', wall_image='$wall_image')"
    }
}
