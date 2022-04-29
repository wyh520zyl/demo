package wuge.social.com.util

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.os.Build
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.annotation.RequiresApi
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import wuge.social.com.activity.DiscoverIssueActivity
import wuge.social.com.model.User
import wuge.social.com.util.Sha1Util
import wuge.social.com.util.Timestamp

import java.io.File
import java.io.IOException
import java.util.*

//发布朋友圈的工具类
class Upload(var picPath: List<String?>, var content: String, context: Context) {
    var str = ""
    private var mContext: Context =context
    fun setUpload() {
        val url = "http://tp6shejiao.wgzy01.com/api/releaseDynamic" //上传图片的接口路径
        //http://happy.wgzy69.com/api/CircleUrlUploadFile
        //http://happy.wgzy69.com/api/CircleUrlUploadFile
        UpLoadFile(url, picPath, object : Callback {
            override fun onFailure(call: Call, e: IOException) { //上传图片失败
                //这儿是耗时操作，完成之后更新UI；
                try {
                    Toast.makeText(mContext, "发布失败，请重新发布！", Toast.LENGTH_LONG).show()
                } catch (a: RuntimeException) {
                    e.printStackTrace()
                }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) { //上传图片成功
                println("上传成功："+response.body)
                (mContext as Activity).finish()
            }
        })
    }

    @TargetApi(Build.VERSION_CODES.O)
    fun UpLoadFile(url: String, name: List<String?>, callback: Callback?) {
        val okHttpClient = OkHttpClient()
        okHttpClient.newCall(getRequest(url, name)).enqueue(callback!!)
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun getRequest(
        url: String,
        name: List<String?>
    ): Request {
        return Request.Builder()
            .url(url)
            .post(getRequsetBody(name))
            .build()
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun getRequsetBody(name: List<String?>): RequestBody {
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        for (i in name.indices) {
            val file = File(name[i])
            val mimeType = getMimeType(file)
            builder.addFormDataPart("video_text[]", file.path, RequestBody.create(mimeType!!.toMediaTypeOrNull(), file))
        }
        builder.addFormDataPart("user_id", User.user_id)
        builder.addFormDataPart("token", User.token)
        builder.addFormDataPart("content", content)
        builder.addFormDataPart("timeStamp", Timestamp.getSecondTimestamp(Date()).toString())
        builder.addFormDataPart("apiSign", Sha1Util.shaEncrypt(Timestamp.getTimeStamp()).toString())
        return builder.build()
    }



    companion object {
        var upload: Upload? = null
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

        fun getMimeType(file: File?): String? {
            val suffix = getSuffix(file)
                ?: return "file/*"
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix)!!
        }
    }


}