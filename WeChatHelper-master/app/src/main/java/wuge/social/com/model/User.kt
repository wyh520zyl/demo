package wuge.social.com.model


object User{
    var user_id: String=""
    var token:String=""
    var address: String=""
    var age: String=""
    var birthday: String=""
    var charm_value: String=""
    var client_id: String=""
    var constellation: String=""
    var exp: String=""
    var game_id: String=""
    var gold: String=""
    var headImg: String=""
    var idCard: String=""
    var is_im: String=""
    var is_online: String=""
    var latitude: String=""
    var levels: String=""
    var longitude: String=""
    var mch_id: String=""
    var nearby_status: String=""
    var nickname: String=""
    var pwd: String=""
    var sex: String=""
    var signature: String=""
    var social_value: String=""
    var source: String=""
    var szr_id: String=""
    var wall_image: String=""
    var province: String="" //省
    var city: String="" //市
    var district: String="" //区
    lateinit var enclosureList:List<Game>
    lateinit var solarTermsResults: List<SolarTermsResult>//签到历史信息
    var taskList=arrayOfNulls<List<TaskX>>(2)//底部导航栏的4个导航按钮
//    lateinit var taskEverydayList: List<TaskX>
//    lateinit var taskGrowList: List<TaskX>



}









