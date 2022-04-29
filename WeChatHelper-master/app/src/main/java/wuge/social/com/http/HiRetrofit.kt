package wuge.social.com.http

import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import wuge.social.com.model.*
import wuge.social.com.util.ReuseUtil
import wuge.social.com.util.Sha1Util
import wuge.social.com.util.Timestamp
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap

/**
  * 用object修饰是为在程序启动之前初始化，类中的方法可以供全局调用
  */
object HiRetrofit {
     private val client = OkHttpClient.Builder() //builder构造者设计模式
    .connectTimeout(10, TimeUnit.SECONDS) //连接超时时间
    .readTimeout(10,TimeUnit.SECONDS)//读取超时
    .writeTimeout(10,TimeUnit.SECONDS)//写超时
    .addInterceptor(LoggingInterceptor())
    .build()
    private val retrofit:Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl("http://tp6shejiao.wgzy01.com/")//此的域名处必须以'/'结尾，不然会报ExceptionInInitializerError
        .addConverterFactory(GsonConverterFactory.create())//数据转换适配器
        .build()



    /**
     * java 方法不能直接使用，需要改写未kotlin格式的方法
     */
//    public <T> T create(final Class<T> Service){
//
//    }

    fun <T>create(clazz: Class<T>):T{
        return retrofit.create(clazz)
    }

    fun getParameter(map: HashMap<String, String>?):Map<String,String>{
        ReuseUtil.mLog("user_id:"+User.user_id)
        ReuseUtil.mLog("token:"+User.token)
        ReuseUtil.mLog("timeStamp:"+Timestamp.getSecondTimestamp(Date()).toString())
        ReuseUtil.mLog("apiSign:"+Sha1Util.shaEncrypt(Timestamp.getTimeStamp()).toString())
        var hashMap = hashMapOf(
            "user_id" to User.user_id,
            "token" to User.token,
            "timeStamp" to Timestamp.getSecondTimestamp(Date()).toString(),
            "apiSign" to Sha1Util.shaEncrypt(Timestamp.getTimeStamp()).toString()
        )
        if (map != null) {
            for (m in map){
                hashMap[m.key] = m.value
            }
        }
       return hashMap
    }
}

/**
 * 多个参数时议使用@QueryMap
 * @GET 请求方式
 * user/userGet 接口名
 * User 返回的实体类
 * @QueryMap 接口参数
 */
interface  ApiService{
    @GET(value = "api/getUserDynamic")
    // fun queryUser(@Query(value = "user_id",encoded = true) user_id:String,@Query(value = "token",encoded = true) token:String):Call<User>
     fun queryUser(@QueryMap map: Map<String,String>):Call<CodeUser>

    @POST(value = "index/wxlogin")
    @FormUrlEncoded
    fun getToken(@FieldMap map:Map<String, String>):Call<WxLogin>

    @POST(value = "api/getTenImSing")
    @FormUrlEncoded
    fun getImSign(@FieldMap map: Map<String,String>):Call<ImSign>

    @GET(value = "api/getCarouselMap")
    fun getSlideshow(@QueryMap map: Map<String,String>):Call<Slideshow>//轮播图

    @POST(value = "api/userLocation")
    @FormUrlEncoded
    fun setLocation(@FieldMap map:Map<String, String>):Call<Response>//定位

    @GET(value = "api/getCoverPhoto")
    fun getCoverPhoto(@QueryMap map: Map<String,String>):Call<CoverPhoto>//圈地和地图图片

    @GET(value = "api/uerSignGet")
    fun getUerSignGet(@QueryMap map: Map<String,String>):Call<SolarTerms>//获取24节气

    @POST(value = "api/userSign")
    @FormUrlEncoded
    fun setUserSign(@FieldMap map:Map<String, String>):Call<Response>

    @POST(value = "api/tasklist")
    @FormUrlEncoded
    fun getTaskList(@FieldMap map:Map<String, String>):Call<Task>

    @POST(value = "api/getNearbyUser")
    @FormUrlEncoded
    fun getNearbyUser(@FieldMap map:Map<String, String>):Call<Nearby>

    @POST(value = "api/addUserFriend")
    @FormUrlEncoded
    fun addUserFriend(@FieldMap map:Map<String, String>):Call<Response>//添加好友

    @POST(value = "api/getMyFriend")
    @FormUrlEncoded
    fun getMyFriend(@FieldMap map:Map<String, String>):Call<Friends>//获取好友列表

    @Multipart
    @POST(value = "api/userInfoUpd")
    fun userInfoUpd(//重载修改个人信息
        @Part("user_id") user_id: RequestBody,
        @Part("token") token:RequestBody,
        @Part("timeStamp") timeStamp:RequestBody,
        @Part("apiSign") apiSign:RequestBody,
        @Part part:MultipartBody.Part
    ):Call<Response1>

    @POST(value = "api/userInfoUpd")
    @FormUrlEncoded
    fun userInfoUpd(@FieldMap map:Map<String, String>):Call<Response>//重载修改个人信息

    @POST(value = "api/updFriendInfo")
    @FormUrlEncoded
    fun updFriendInfo(@FieldMap map:Map<String, String>):Call<Response>//修改好友备注

    @POST(value = "api/delUserFriend")
    @FormUrlEncoded
    fun delUserFriend(@FieldMap map:Map<String, String>):Call<Response>//删除好友

    @Multipart
    @POST(value = "api/releaseDynamic")
    fun releaseDynamic(//发布动态
        @Part("user_id") user_id: RequestBody,
        @Part("token") token:RequestBody,
        @Part("timeStamp") timeStamp:RequestBody,
        @Part("apiSign") apiSign:RequestBody,
        @Part("content") content:RequestBody,
        //@FieldMap map:Map<String, String>,
        @Part parts: Array<MultipartBody.Part?>
    ):Call<Response>

    @Multipart
    @POST(value = "api/releaseDynamic")
    fun releaseDynamic(//发布动态
        @Part("user_id") user_id: RequestBody,
        @Part("token") token:RequestBody,
        @Part("timeStamp") timeStamp:RequestBody,
        @Part("apiSign") apiSign:RequestBody,
        @Part("content") content:RequestBody,
        @Part video_text:MultipartBody.Part
    ):Call<Response>

    @GET(value = "api/getDynamicList")
    fun getDynamicList(@QueryMap map: Map<String,String>):Call<Discover>//获取动态列表

    @POST(value = "api/dynamicFavor")
    @FormUrlEncoded
    fun dynamicFavor(@FieldMap map:Map<String, String>):Call<Response2>//点赞/取消点赞动态

     @POST(value = "api/dynamicCommentDel")
    @FormUrlEncoded
    fun dynamicCommentDel(@FieldMap map:Map<String, String>):Call<Response>//删除评论

    @POST(value = "api/dynamicComment")
    @FormUrlEncoded
    fun dynamicComment(@FieldMap map:Map<String, String>):Call<Response>//点赞/取消点赞动态

    @GET(value = "api/getDynamicCommentList")
    fun getDynamicCommentList(@QueryMap map: Map<String,String>):Call<Comment>//获取动态评论详情

     @GET(value = "user/userGet")
    fun getUser(@QueryMap map: Map<String,String>):Call<UserModel>//获取动态评论详情
}