package wuge.social.com.model

data class UserModel(
    var code: Int,
    var `data`: Data

) {
    override fun toString(): String {
        return "UserModel(code=$code, `data`=$`data`)"
    }
}

data class Data(
    var address: String,
    var adolescent_status: String,
    var age: String,
    var birthday: String,
    var charm_value: String,
    var client_id: String,
    var constellation: String,
    var exp: String,
    var game_id: String,
    var gold: String,
    var headImg: String,
    var idCard: String,
    var is_im: String,
    var is_online: String,
    var is_sys: String,
    var latitude: String,
    var levels: String,
    var longitude: String,
    var mch_id: String,
    var nearby_status: String,
    var nickname: String,
    var pwd: String,
    var sex: String,
    var signature: String,
    var social_value: String,
    var source: String,
    var szr_id: String,
    var user_id: String,
    var wall_url: String

) {
    override fun toString(): String {
        return "Data(address='$address', adolescent_status='$adolescent_status', age='$age', birthday='$birthday', charm_value='$charm_value', client_id='$client_id', constellation='$constellation', exp='$exp', game_id='$game_id', gold='$gold', headImg='$headImg', idCard='$idCard', is_im='$is_im', is_online='$is_online', is_sys='$is_sys', latitude='$latitude', levels='$levels', longitude='$longitude', mch_id='$mch_id', nearby_status='$nearby_status', nickname='$nickname', pwd='$pwd', sex='$sex', signature='$signature', social_value='$social_value', source='$source', szr_id='$szr_id', user_id='$user_id', wall_url='$wall_url')"
    }
}