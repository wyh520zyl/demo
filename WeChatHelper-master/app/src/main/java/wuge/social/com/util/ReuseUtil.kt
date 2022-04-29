package wuge.social.com.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

//重复使用的方法
object ReuseUtil {
     var page=0//发现页面的当前页


    /**
     * 通过广播告诉RecyclerViewActivity需要更新数据了
     */
    fun refreshRecyclerViewActivity(actionUpdateUi:String,activity: Activity) {
        val intent = Intent()
        intent.action = actionUpdateUi

        /*
         * 不要删除，intent.putExtra()可能用得上
         */
        activity.sendBroadcast(intent)
    }
    /**
     * 通过广播告诉RecyclerViewActivity需要更新数据了
     * 可以使用 intent.putExtra() 传递数据
     */
    fun refreshRecyclerViewActivity(actionUpdateUi:String,activity: Activity,map: HashMap<String, String>?) {
        val intent = Intent()
        intent.action = actionUpdateUi
        if (map != null) {
            for (m in map){
                intent.putExtra(m.key,m.value)
            }
        }
        activity.sendBroadcast(intent)
    }

    /**
     * 显示键盘
     *
     * @param et 输入焦点
     */
    fun showInput(et: EditText,context: Context) {
        et.requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT)
    }
    fun mLog(text:String){
        Log.e("物格社交",text)
    }
}