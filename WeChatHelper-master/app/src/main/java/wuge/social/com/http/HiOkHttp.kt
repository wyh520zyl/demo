package wuge.social.com.http

import android.content.Context
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import wuge.social.com.model.User
import wuge.social.com.util.Sha1Util
import wuge.social.com.util.Timestamp
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

object HiOkHttp {
//    private const val BASE_URL="https://circle.wgzy69.com/v1/interface"
//    private const val BASE_URL="http://tp6shejiao.wgzy01.com"//正式域名
    private const val BASE_URL="http://wzt.wgzy01.com"//测试域名
    private val client: OkHttpClient = OkHttpClient.Builder() //builder构造者设计模式
        .connectTimeout(10,TimeUnit.SECONDS) //连接超时时间
        .readTimeout(10,TimeUnit.SECONDS)//读取超时
        .writeTimeout(10,TimeUnit.SECONDS)//写超时
        .addInterceptor(LoggingInterceptor())//可以传入httpLoggingInterceptor或者LoggingInterceptor()
        .build()



    fun get(){
        Thread(Runnable  {
            //构造请求体
            val request:Request=Request.Builder()
                .url("$BASE_URL/user/userGet?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJzb2wiLCJleHAiOjE2MzA1NjQzNTAsImF1ZCI6ImFkbWluIiwibmJmIjoxNjI5MjY4MzUwLCJpYXQiOjE2MjkyNjgzNTAsInd4X3VpZCI6IjIyNTgzIn0.N75m9PxbrALWsGKSb8Vv2FdguniDfyIcVDjhUD9n2f8&user_id=22583")
                .build()
            //构造请求对象
            val call: Call = client.newCall(request)
            //发起同步请求execute--同步执行
            val response=call.execute()
            val body=response.body?.string()
            Log.d("OKHTTP","onResponse:${body}")
        }).start()
    }

    fun getAsync(){
        //构造请求体
        val request:Request=Request.Builder()
            .url("$BASE_URL/user/userGet?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJzb2wiLCJleHAiOjE2MzA1NjQzNTAsImF1ZCI6ImFkbWluIiwibmJmIjoxNjI5MjY4MzUwLCJpYXQiOjE2MjkyNjgzNTAsInd4X3VpZCI6IjIyNTgzIn0.N75m9PxbrALWsGKSb8Vv2FdguniDfyIcVDjhUD9n2f8&user_id=22583")
            .build()
        //构造请求对象
        val call: Call = client.newCall(request)
        //发起异步请求execute--同步执行
        call.enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.d("OKHTTP","onFailure:${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val body=response.body?.string()
                Log.d("OKHTTP","onResponse:${body}")
            }
        })
    }


    /**
     * post同步请求（表单提交）
     *
     */
    fun post(){
        val  body =FormBody.Builder()
            .add("user_id","22583")
            .add("token","eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJzb2wiLCJleHAiOjE2MzA1NjQzNTAsImF1ZCI6ImFkbWluIiwibmJmIjoxNjI5MjY4MzUwLCJpYXQiOjE2MjkyNjgzNTAsInd4X3VpZCI6IjIyNTgzIn0.N75m9PxbrALWsGKSb8Vv2FdguniDfyIcVDjhUD9n2f8")
            .add("type","Wg_User_Nick")
            .add("Value","测试")
            .build()
        val request = Request.Builder()
            .url("$BASE_URL/user/userSet")
            .post(body)
            .build()
        val call= client.newCall(request)
        Thread(Runnable {
            val response=call.execute()
            Log.d("OKHTTP","response:${response.body?.string()}")
        }).start()
    }


    /**
     * post异步请求（表单提交）
     */
    fun postAsync(nickname:String,headimgurl:String,openid:String,unionid:String){
        val  body =FormBody.Builder()
            .add("nickname",nickname)
            .add("headimgurl",headimgurl)
            .add("openid",openid)
            .add("unionid",unionid)
            .build()
        val request = Request.Builder()
            .url("$BASE_URL/wxLogin")
            .post(body)
            .build()
        val call= client.newCall(request)

        call.enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.d("OKHTTP","onFailure:${e.message}")

            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("OKHTTP","response:${response.body?.string()}")

            }
        })
    }

    /**
     *post 异步请求（多表单文件上传）,在android6.0及以后，读取外部储存卡的文件都是需要动态申请权限
     */
//    fun postAsyncMultipart(context:Context){
//
//        val file = File(Environment.getExternalStorageDirectory(),"1.png")
//        if(!file.exists()){
//            Toast.makeText(context,"文件不存在",Toast.LENGTH_SHORT).show()
//        }
//        val body:MultipartBody = MultipartBody.Builder()
//            .addFormDataPart("key","value")
//            .addFormDataPart("key","value")
//            .addFormDataPart("file","1.png",RequestBody.create("application/octet-stream".toMediaTypeOrNull(),file))
//            .build()
//        val request =
//            Request.Builder().url("接口是需要支持文件上传才可以使用的")
//                .post(body)
//                .build()
//        val call:Call = client.newCall(request);
//        call.enqueue(object :Callback{
//            override fun onFailure(call: Call, e: IOException) {
//                Log.d("OKHTTP","postAsyncMultipart->onFailure:${e.message}")
//
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                Log.d("OKHTTP","postAsyncMultipart->response:${response.body?.string()}")
//            }
//        })
//    }
    private fun getSuffix(file: File?): String? {
        if (file == null || !file.exists() || file.isDirectory) {
            return null
        }
        val fileName = file.name
        if (fileName == "" || fileName.endsWith(".")) {
            return null
        }
        val index = fileName.lastIndexOf(".")
        return if (index != -1) {
            fileName.substring(index + 1).toLowerCase(Locale.US)
        } else {
            null
        }
    }
     fun getMimeType(file: File?): String {
        val suffix: String = getSuffix(file)
            ?: return "file/*"
        val type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix)
        return type ?: "file/*"
    }

    fun postAsyncMultipart(uri:String){
        //val file = File(Environment.getDownloadCacheDirectory(),uri)
        val file: File = File(uri)
        val mimeType: String = getMimeType(file)
        if(!file.exists()){
//            Toast.makeText(context,"文件不存在",Toast.LENGTH_SHORT).show()
            Log.d("postAsyncMultipart","文件不存在")
        }
        Log.d("postAsyncMultipart","user_id:${User.user_id}")
        Log.d("postAsyncMultipart","token:${User.token}")
        Log.d("postAsyncMultipart","timeStamp:${Timestamp.getSecondTimestamp(Date()).toString()}")
        Log.d("postAsyncMultipart","apiSign:${Sha1Util.shaEncrypt(Timestamp.getTimeStamp()).toString()}")
        val body:MultipartBody = MultipartBody.Builder()
            .addFormDataPart("user_id", User.user_id)
            .addFormDataPart("token", User.token)
            .addFormDataPart("timeStamp", Timestamp.getSecondTimestamp(Date()).toString())
            .addFormDataPart("apiSign", Sha1Util.shaEncrypt(Timestamp.getTimeStamp()).toString())
            .addFormDataPart("headImg", file.path, RequestBody.create(mimeType.toMediaTypeOrNull(), file)
            )
        .build()
        val request =
            // Request.Builder().url("接口是需要支持文件上传才可以使用的")
            Request.Builder().url("http://tp6shejiao.wgzy01.com/api/userInfoUpd")
                .post(body)
                .build()
        val call:Call = client.newCall(request);
        call.enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.d("postAsyncMultipart","postAsyncMultipart->onFailure:${e.message}")

            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("postAsyncMultipart","postAsyncMultipart->response:${response.body?.string()}")
            }
        })
    }


    /**
     * 异步post请求（提交字符串）
     */
    fun postString(){
        val textPlain = "text/plain;charset=utf-8".toMediaType()//普通字符串
        val applicationJSON = "application/json;charset=utf-8".toMediaType()//json格式的字符串
        val jsonObj = JSONObject()
        jsonObj.put("key","value")
        jsonObj.put("key1",100)

        val textObj="username:username;password:password"

        val textBody = RequestBody.create(textPlain,textObj)
        val jsonBody = RequestBody.create(applicationJSON,jsonObj.toString())

        val request =
            Request.Builder().url("*****")
                .post(jsonBody)//textBody或者jsonBody
                .build()
        val call = client.newCall(request)
        call.enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.d("OKHTTP","postString->onFailure:${e.message}")

            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("OKHTTP","postString->response:${response.body?.string()}")
            }
        })
    }
}