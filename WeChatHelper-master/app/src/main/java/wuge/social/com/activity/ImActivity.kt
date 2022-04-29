package wuge.social.com.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.tencent.imsdk.v2.V2TIMConversation
import com.tencent.qcloud.tim.uikit.modules.chat.ChatLayout
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo
import wuge.social.com.R
import wuge.social.com.util.Constant

/**
 * 聊天界面
 */
class ImActivity : BaseActivity() {
    private val info = ChatInfo()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_im)
        println("加入到了i吗")
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        verifyAudioPermissions(this)
        val chatLayout = findViewById<ChatLayout>(R.id.chat_layout)
        chatLayout.initDefault()
        val input = chatLayout.inputLayout.findViewById<EditText>(R.id.chat_message_input)
        input.setTextColor(Color.BLACK)
        val id = intent.getStringExtra("id")
        val name = intent.getStringExtra("name")
        val type = intent.getIntExtra("type", V2TIMConversation.V2TIM_C2C) //V2TIMConversation.V2TIM_C2C表示1v1聊天
        val tag = "ImActivity"
        Log.i(tag, "id:$id")
        Log.i(tag, "name:$name")
        Log.i(tag, "type:$type")
        //设置聊天对象信息
        info.chatName = name
        info.id = id
        info.type = type
        info.isTopChat = false
        info.type = V2TIMConversation.V2TIM_C2C
        chatLayout.chatInfo = info
        //设置右上角图标
        chatLayout.titleBar.setOnRightClickListener {
            Log.i("ImActivity", "type:" + "id")
            val intent = Intent(this@ImActivity, PersonalActivity1::class.java)
            intent.putExtra("toId", id)
            startActivity(intent)
        }
    }

    /*
     * 申请录音权限*/
    private fun verifyAudioPermissions(activity: Activity?) {
        val permission = ActivityCompat.checkSelfPermission(
            activity!!,
            Manifest.permission.RECORD_AUDIO
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity, PERMISSION_AUDIO,
                GET_RECODE_AUDIO
            )
        }
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.CAMERA
                )
            ) {
                Toast.makeText(this, "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_LONG).show()
            }
            // 申请权限
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                Constant.REQ_PERM_CAMERA
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    public override fun onResume() {
        super.onResume()
    }

    companion object {
        private const val GET_RECODE_AUDIO = 1
        private val PERMISSION_AUDIO = arrayOf(
            Manifest.permission.RECORD_AUDIO
        )
    }
}
